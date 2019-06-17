package com.aechkae.organizer.focus;

public class CompTask extends Task {
    public String compDate;

    public CompTask(String idCode, String desc, String compDate){
        super(idCode, desc);
        this.compDate = compDate;
    }
}
