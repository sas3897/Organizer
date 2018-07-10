package com.aechkae.organizer.Agile.common;

/**
 * Analogous POJO for tasks stored in the SQLite database.
 */

public class Task{
    public String idCode;
    public String description;
    public double percentageDone;

    public Task(String id, String desc, double perc){
        idCode = id;
        description = desc;
        percentageDone = perc;
    }
}
