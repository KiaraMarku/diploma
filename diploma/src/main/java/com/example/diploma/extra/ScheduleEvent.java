package com.example.diploma.extra;

import lombok.Data;

@Data
public class ScheduleEvent {
    private String title;
    private String start;
    private String end;
    private Integer dow;
    private String lecturer;
    private String hall;
    private String type;

    public ScheduleEvent(String title, String start, String end, Integer dow, String lecturer, String hall, String type) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.dow = dow;
        this.lecturer = lecturer;
        this.hall = hall;
        this.type = type;
    }
}


