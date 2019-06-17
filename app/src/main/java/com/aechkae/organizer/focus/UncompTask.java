package com.aechkae.organizer.focus;

public class UncompTask extends Task{
    public String perc_done;

    public UncompTask(String idCode, String desc, String perc_done){
        super(idCode, desc);
        this.perc_done = perc_done;
    }
}
