package com.raz.method.processors;

import com.raz.model.DayTime;
import com.raz.model.Order;

import java.util.List;

/**
 * TODO
 * Assumptions:
 *
 */
public class Drone {
    private int verCoor, horCoor;
    private DayTime curTime;

    public static DayTime START_TIME = new DayTime(6, 0, 0);
    public static DayTime END_TIME = new DayTime(22, 0, 0);

    public Drone() {
        verCoor = 0;
        horCoor = 0;
        curTime = DayTime.copyOf(START_TIME);
    }

    public Drone(Drone pre) {
        verCoor = pre.getVerCoor();
        horCoor = pre.getHorCoor();
        curTime = DayTime.copyOf(pre.getCurTime());
    }

    /**
     *  get an order to go, return delivery time and store the time come back to center
     */
    public DayTime gotoNext(Order o) {
        if(!available()) return null;
        DayTime start = DayTime.getComparator().compare(curTime, o.getDay()) >= 0 ? curTime : o.getDay();
        int minute = Math.abs(o.getHorCoor()) + Math.abs(o.getVerCoor());
        int delay = minute / 5;

        minute += delay;

        // get too much time
        if(minute > 24 * 60) return null;
        DayTime single = new DayTime(minute);
        DayTime res = DayTime.plus(start, single);

        if(res == null || DayTime.getComparator().compare(END_TIME, res) <= 0) {
            curTime = null;
            return null;
        }

        curTime = DayTime.plus(res, single);
        if(curTime == null) return null;
        if(DayTime.getComparator().compare(END_TIME, curTime) <= 0) {
            curTime = null;
        }
        return res;
    }

    public DayTime getExpectedDelieverTime(Order o) {
        if(!available()) return null;
        DayTime start = DayTime.getComparator().compare(curTime, o.getDay()) >= 0 ? curTime : o.getDay();
        int minute = Math.abs(o.getHorCoor()) + Math.abs(o.getVerCoor());

        // get too much time
        if(minute > 24 * 60) return null;

        DayTime single = new DayTime(minute);
        DayTime res = DayTime.plus(start, single);

        return res;
    }

    public boolean available() {
        return curTime != null && DayTime.getComparator().compare(curTime, END_TIME) < 0;
    }

    public int getVerCoor() {
        return verCoor;
    }

    public void setVerCoor(int verCoor) {
        this.verCoor = verCoor;
    }

    public int getHorCoor() {
        return horCoor;
    }

    public void setHorCoor(int horCoor) {
        this.horCoor = horCoor;
    }

    public DayTime getCurTime() {
        return curTime;
    }

    public void setCurTime(DayTime curTime) {
        this.curTime = curTime;
    }
}
