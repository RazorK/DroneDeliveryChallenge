package com.raz.model;

import com.raz.utils.Utils;

import java.util.Comparator;

import static com.raz.utils.Utils.generateRandomInt;

public class DayTime {
    private int hour, minute, second;

    public DayTime() {
    }

    public DayTime(int minute) {
        if(minute >= 24 * 60) {
            throw new IndexOutOfBoundsException();
        } else {
            this.hour = minute/60;
            this.minute = minute - this.hour * 60;
            this.second = 0;
        }
    }

    public DayTime(int hour, int minute, int second) {
        if(hour >= 24 || minute >= 60 || second >= 60) {
            throw new IndexOutOfBoundsException();
        }
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int compareTo(DayTime o) {
        return getComparator().compare(this, o);
    }

    public boolean equalTo(DayTime o) {
        return getComparator().compare(this, o) == 0;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("%02d", hour));
        res.append(':');
        res.append(String.format("%02d", minute));
        res.append(':');
        res.append(String.format("%02d", second));
        return res.toString();
    }

    public static DayTime randomeGenerate() {
        int hour = generateRandomInt(0,23);
        int minute = generateRandomInt(0, 59);
        int second = generateRandomInt(0,59);
        return new DayTime(hour, minute, second);
    }

    public static DayTime parse(String str) {
        if(str == null || str.length() == 0) return null;
        String [] times = str.split(":");
        return new DayTime(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));
    }

    public static DayTime plus(DayTime o1, DayTime o2) {
        int second = o1.getSecond() + o2.getSecond();
        int minute = o1.getMinute() + o2.getMinute();
        int hour = o1.getHour() + o2.getHour();
        minute += second >= 60 ? 1 : 0;
        hour += minute >= 60 ? 1 : 0;
        if(hour >= 24) return null;
        return new DayTime(hour, minute%60, second%60);
    }

    /**
     * a minus function for daytime, minuend should greater than subtrahend.
     * @param minuend
     * @param subtrahend
     * @return null if minuend smaller than subtrahend, or return the result1 of minus
     */
    public static DayTime minus(DayTime minuend, DayTime subtrahend) {
        if(minuend == null || subtrahend == null) return null;
        if(getComparator().compare(minuend, subtrahend) < 0) return null;
        else {
            int second = minuend.getSecond() - subtrahend.getSecond();
            int minute = minuend.getMinute() - subtrahend.getMinute();
            int hour = minuend.getHour() - subtrahend.getHour();

            minute -= second <0 ? 1 : 0;
            hour -= minute < 0 ? 1 : 0;

            if(hour < 0) return null;
            return new DayTime(hour, minute < 0 ? minute + 60 : minute, second < 0 ? second + 60 : second);
        }
    }

    public static Comparator<DayTime> getComparator() {
        return com;
    }

    public static Comparator<DayTime> com = (o1, o2) -> {
        if(o1.getHour() != o2.getHour()) {
            return o1.getHour() - o2.getHour();
        } else if(o1.getMinute() != o2.getMinute()) {
            return o1.getMinute() - o2.getMinute();
        } else if(o1.getSecond() != o2.getSecond()) {
            return o1.getSecond() - o2.getSecond();
        } else {
            return 0;
        }
    };

    public static DayTime copyOf(DayTime o) {
        if(o == null) return null;
        return new DayTime(o.getHour(), o.getMinute(), o.getSecond());
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
