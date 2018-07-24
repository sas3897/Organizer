package com.aechkae.organizer.focus.schemas;

/** Analogous POJO for tasks stored in the SQLite database. */
public final class Task{
    public String idCode;
    public String description;
    public double percentageDone;

    public static final int UNCOMP_DESC_CUTOFF_LENGTH = 16;
    public static final int COMP_DESC_CUTOFF_LENGTH = 32;

    public Task(String id, String desc, double perc){
        idCode = id;
        description = desc;
        percentageDone = perc;
    }
}
