package com.raz.method.processors.controller;

import com.raz.model.DayTime;
import com.raz.model.Order;
import com.raz.utils.Utils;

import javax.rmi.CORBA.Util;
import java.util.*;

public class ShortestFirst extends BaseController {

    public ShortestFirst() {
        super(true);
    }

    /**
     * shortest first strategy:
     * if there are orders waiting, do the shortest one,
     * else do the next one
     * @param data
     */
    @Override
    public void dispatch(List<Order> data) {
        super.dispatch(data);

        List<Order> copy = new ArrayList<>(data);
        PriorityQueue<Order> wait = new PriorityQueue<>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return (Math.abs(o1.getVerCoor()) + Math.abs(o1.getHorCoor())) -
                        (Math.abs(o2.getHorCoor()) + Math.abs(o2.getVerCoor()));
            }
        });

        while(copy.size()!=0 || wait.size() != 0) {
            // first step fetch available orders to wait
            DayTime cur = this.drone.getCurTime();
            if(cur == null) return;

            int ptr = 0;
            while(ptr<copy.size()) {
                DayTime orderTime = copy.get(ptr).getDay();
                if(DayTime.getComparator().compare(orderTime, cur) <= 0) {
                    wait.add(copy.get(ptr));
                    copy.remove(ptr);
                } else {
                    ptr++;
                }
            }

            // find a order to go
            if(wait.size() != 0) {
                gotoNext(wait.poll());
            } else {
                Order next = copy.get(0);
                copy.remove(0);
                gotoNext(next);
            }
        }
    }
}
