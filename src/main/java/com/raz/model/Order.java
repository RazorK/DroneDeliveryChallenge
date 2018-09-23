package com.raz.model;


import com.raz.utils.Utils;

import javax.rmi.CORBA.Util;

public class Order {
    /**
     * id: id for this order
     * verCoor: vertical coordinate, positive for North, negative for South
     * horCoor: horizontal coordinate, positive for east, negative for west
     */
    private int id, verCoor, horCoor;
    private DayTime day;
    private DayTime preferFinishTime;

    public Order() {
    }

    public Order(int id, int verCoor, int horCoor, DayTime day) {
        this.id = id;
        this.verCoor = verCoor;
        this.horCoor = horCoor;
        this.day = day;
    }

    public static Order randomGenerate(int id, int range) {
        int verCoor = Utils.generateRandomInt(-range, range);
        int horCoor = Utils.generateRandomInt(-range, range);
        return new Order(id, verCoor, horCoor, DayTime.randomeGenerate());
    }

    public static Order parse(String str) {
        if(str == null || str.length() == 0) return null;
        String [] data = str.split(" ");

        int id = Integer.parseInt(data[0].substring(2));

        int [] coorNum = parseCoor(data[1]);
        int verCoor, horCoor;
        if((data[1].contains("N") || data[1].contains("S")) && (data[1].contains("E") || data[1].contains("W"))) {
            verCoor = data[1].contains("N") ? coorNum[0] : -coorNum[0];
            horCoor = data[1].contains("E") ? coorNum[1] : -coorNum[1];
        } else {
            return null;
        }
        DayTime day = DayTime.parse(data[2]);

        return new Order(id, verCoor, horCoor, day);
    }

    public static int [] parseCoor(String coor) {
        int [] res = new int[2];
        int num = 0;
        for(int i=0; i<coor.length(); i++) {
            char cur = coor.charAt(i);
            if(Character.isDigit(cur)) {
                num = num*10 + (cur - '0');
            } else {
                res[0] = num;
                num = 0;
            }
        }
        res[1] = num;
        return res;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("WM");
        res.append(String.format("%03d", id));
        res.append(' ');

        res.append(this.verCoor >= 0? 'N' : 'S');
        res.append(Math.abs(this.verCoor));
        res.append(this.horCoor >=0? 'E':'W');
        res.append(Math.abs(this.horCoor));
        res.append(' ');

        res.append(this.day.toString());
        return res.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public DayTime getDay() {
        return day;
    }

    public void setDay(DayTime day) {
        this.day = day;
    }
}
