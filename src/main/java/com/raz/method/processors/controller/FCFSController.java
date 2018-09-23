package com.raz.method.processors.controller;

import com.raz.model.DayTime;
import com.raz.model.Order;

import java.util.List;

public class FCFSController extends BaseController {
    public FCFSController() {
        super(true);
    }

    /**
     * FCFS method
     * @param data list of order, should be sorted according to time
     */
    @Override
    public void dispatch(List<Order> data) {
        super.dispatch(data);

        for(int i=0; i<data.size(); i++) {
            Order cur = data.get(i);
            DayTime curTime = this.gotoNext(cur);
            if(curTime == null) break;
        }
    }
}
