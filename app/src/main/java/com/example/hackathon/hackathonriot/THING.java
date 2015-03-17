package com.example.hackathon.hackathonriot;

/**
 * Created by Emilio-Emilio on 3/15/2015.
 */
public class THING {
    /*"id": 1,
            "activated": false,
            "name": "RFID1234567890",
            "serial": "RFID1234567890"*/

    public String id;
    public String activated;
    public String name;
    public String serial;
    public THING(String id,String  act,String name,String seria){
        this.id = id;
        this.activated = act;
        this.name = name;
        this.serial = seria;
    }
}
