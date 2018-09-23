package com.raz.method.processors.controller;

import com.raz.method.processors.Drone;
import com.raz.model.DayTime;
import com.raz.model.Order;
import com.raz.utils.Utils;

import javax.rmi.CORBA.Util;
import java.util.*;

public class DFSController extends BaseController {
    Map<Order, DayTime> storeMap;
    double maxNPS;
    int storePro, storeNeu;

    int depth;

    @Override
    public void dispatch(List<Order> data) {
        super.dispatch(data);
        List<Order> copy = new ArrayList<Order>(data);
        maxNPS = -Double.MAX_VALUE;

        storeMap = new LinkedHashMap<>();

        depth = 0;
        dfsHelper(copy);

        map = storeMap;
        promoter = storePro;
        neutral = storeNeu;

        Utils.debug(storePro + "," + storeNeu + "," + total);
    }


    public void dfsHelper(List<Order> data) {
        depth++;
        Utils.debug(depth);
        if(!droneAvailable() || data.size() == 0) {
            if(getNPS() > maxNPS) {
                storeMap = new LinkedHashMap<>(map);
                maxNPS = getNPS();
                storePro = promoter;
                storeNeu = neutral;
            }
            depth--;
            return;
        }

        Drone storeDrone = new Drone(this.drone);
        int storePro = promoter, storeNeu = neutral;
        for(int i=0; i<data.size(); i++) {
            Order storeOrder = data.get(i);

            gotoNext(data.get(i));
            data.remove(i);
            dfsHelper(data);

            data.add(i, storeOrder);
            drone = new Drone(storeDrone);
            promoter = storePro;
            neutral = storeNeu;
            map.remove(storeOrder);
        }
        depth--;
    }
}
