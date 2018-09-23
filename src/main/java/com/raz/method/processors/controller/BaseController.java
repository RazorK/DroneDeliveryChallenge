package com.raz.method.processors.controller;

import com.raz.method.processors.Drone;
import com.raz.model.DayTime;
import com.raz.model.Order;
import com.raz.utils.Utils;

import javax.rmi.CORBA.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseController {
    public static final int DELIVER_TYPE_PROMOTER = 0;
    public static final int DELIVER_TYPE_NEUTRAL = 1;
    public static final int DELIVER_TYPE_DETRACTOR = 2;

    public static DayTime PROMOTER_STANDARD = new DayTime(2,0,0);
    public static DayTime NEUTRAL_STANDARD = new DayTime(4, 0, 0);

    Map<Order, DayTime> map;
    int promoter, neutral;
    int total;
    Drone drone;
    boolean tricky;

    public BaseController() {
        this(false);
    }

    public BaseController(boolean tricky) {
        map = new LinkedHashMap<>();
        promoter = 0;
        neutral = 0;
        total = 0;
        drone = new Drone();
        this.tricky = tricky;
    }

    public void addPromoter(int i) {
        promoter += i;
    }

    public void addNeutral(int i) {
        neutral += i;
    }

    public double getNPS() {
        if(total <= 0) throw new RuntimeException("Total order shouldn't be zero, maybe not initialized");
        double res = (promoter + 0.0) * 100 / total - (0.0 + total - promoter - neutral) * 100 / total;
        return res;
    }

    public void exportToPath(String path) throws IOException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        if(map!=null && map.size()!=0) {
            for(Map.Entry<Order, DayTime> en : map.entrySet()) {
                writer.println("WM" + String.format("%03d",en.getKey().getId()) + " " + en.getValue().toString());
            }
        }
        writer.println("NPS " + String.format("%.0f",getNPS()));
        writer.close();
    }

    public void dispatch(List<Order> data) {
        total = data.size();
    }

    /**
     * functionalities:
     * 1. update promoter / neutral
     * 2. update map & drone cur time
     * 3. return cur time
     * @param o next order
     * @return cur time
     */
    public DayTime gotoNext(Order o) {
        DayTime startTime;
        if(droneAvailable()) {
            startTime = DayTime.getComparator().compare(o.getDay(), drone.getCurTime()) > 0 ? o.getDay() : drone.getCurTime();
        } else {
            return null;
        }

        Drone store = null;
        if(tricky) {
            store = new Drone(drone);
        }

        DayTime deliverTime = drone.gotoNext(o);
        if(deliverTime == null) return null;

        if(tricky) {
            int type = getDeliverType(o, deliverTime);
            if(type == DELIVER_TYPE_DETRACTOR) {
                drone = new Drone(store);
            } else {
                processDeliverType(type);
                map.put(o, startTime);
            }
        } else {
            processDeliverType(getDeliverType(o, deliverTime));
            map.put(o, startTime);
        }

        return drone.getCurTime();
    }

    public static int getDeliverType(Order o, DayTime deliverTime) {
        if(deliverTime == null) return DELIVER_TYPE_DETRACTOR;

        DayTime gap = DayTime.minus(deliverTime, o.getDay());
        if(gap == null) return DELIVER_TYPE_DETRACTOR;

        if(DayTime.getComparator().compare(PROMOTER_STANDARD, gap) >= 0) return DELIVER_TYPE_PROMOTER;
        if(DayTime.getComparator().compare(NEUTRAL_STANDARD, gap) >= 0) return DELIVER_TYPE_NEUTRAL;
        return DELIVER_TYPE_DETRACTOR;
    }

    public void processDeliverType(int type) {
        switch (type) {
            case DELIVER_TYPE_PROMOTER:
                this.promoter ++;
                break;
            case DELIVER_TYPE_NEUTRAL:
                this.neutral ++;
                break;
            case DELIVER_TYPE_DETRACTOR:
            default:
        }
    }

    public boolean droneAvailable() {
        return drone != null && drone.available();
    }

    public void reportRes(String tag) {
        StringBuilder res = new StringBuilder();
        res.append("====================Reporting==================\n");
        res.append("Reporting for ").append(tag).append('\n');
        res.append("Pos: ").append(promoter).append(", Neu: ").append(neutral).append(", Neg: ").append(total - promoter - neutral).append('\n');
        res.append("NPS: ").append(getNPS());
        System.out.println(res.toString());
    }
}
