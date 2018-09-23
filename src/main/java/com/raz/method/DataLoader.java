package com.raz.method;

import com.raz.model.DayTime;
import com.raz.model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataLoader {

    List<Order> data;
    boolean generated;
    String inPath;

    public DataLoader(int num, int coorRange) {
        data = new ArrayList<>();
        generated = true;

        for(int i=0; i<num; i++) {
            data.add(Order.randomGenerate(i, coorRange));
        }

        Collections.sort(data, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return DayTime.getComparator().compare(o1.getDay(), o2.getDay());
            }
        });
    }

    public DataLoader(String path) throws IOException {
        loadFile(path);
        this.inPath = path;
        this.generated = false;

        Collections.sort(data, (o1, o2) -> DayTime.getComparator().compare(o1.getDay(), o2.getDay()));
    }

    public void loadFile(String path) throws IOException {
        data = new ArrayList<>();

        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            Order parse = Order.parse(line);
            if(parse != null) {
                data.add(parse);
            }
        }
        br.close();
    }

    public void exportFile(String path) throws IOException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        if(data!=null && data.size()!=0) {
            for(Order cur : data) {
                writer.println(cur.toString());
            }
        }
        writer.close();
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        if(!generated) {
            res.append("Data Loaded from Path: ");
            res.append(this.inPath);
            res.append('\n');
        } else {
            res.append("Data is random generated: ");
            res.append('\n');
        }

        if(data!=null && data.size()!=0) {
            for(Order cur : data) {
                res.append(cur.toString());
                res.append('\n');
            }
        }
        return res.toString();
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
