package com.aechkae.organizer.database;

// TODO Meant to represent a table, this solution is unworkable, but will be obsolesced later
// TODO by remote database queries
public enum Schema {
    UNCOMP_TASKS("Uncompleted_Tasks"),
    COMP_TASKS("Completed_Tasks");

    private String table_name;

    Schema(String name){
        table_name = name;
    }

    public String getTable_name() {
        return table_name;
    }
}
