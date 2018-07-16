package com.aechkae.organizer.Focus.schemas;

/** Analogous POJO for tasks stored in the SQLite database. */
public final class Task{
    public String idCode;
    public String description;
    public double percentageDone;

    public static final int CUTOFF_LENGTH = 16;

    public Task(String id, String desc, double perc){
        idCode = id;
        description = desc;
        percentageDone = perc;
    }
}
