package com.aechkae.organizer.Agile;

/**
 * Storage classes for data used in the agile activity
 */


class Task{
    String idCode;
    String description;
    double percentageDone;

    Task(String id, String desc, double perc){
        idCode = id;
        description = desc;
        percentageDone = perc;
    }
}
