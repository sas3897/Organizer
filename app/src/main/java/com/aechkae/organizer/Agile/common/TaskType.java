package com.aechkae.organizer.Agile.common;

public enum TaskType {
    ACTIVE(0),
    BACKBURNER(1),
    BACKLOG(2);


    private final int type;

    TaskType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
