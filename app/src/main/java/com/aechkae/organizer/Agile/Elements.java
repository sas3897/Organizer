package com.aechkae.organizer.Agile;

/**
 * Storage classes for data used in the agile activity
 */


//TODO If there's not more differentiating these, maybe it's better to have them carry an enum of what type they are?
abstract class Task{
    String idCode;
    String description;
    double percentageDone;

    Task(String id, String desc, double perc){
        idCode = id;
        description = desc;
        percentageDone = perc;
    }
}

class ActiveTask extends Task{
    ActiveTask(String id, String desc, double perc){
        super(id, desc, perc);
    }
}

class BacklogTask extends Task{

    BacklogTask(String id, String desc, double perc){
        super(id, desc, perc);
    }
}

class BackburnerTask extends Task{

    BackburnerTask(String id, String desc, double perc){
        super(id, desc, perc);
    }
}
