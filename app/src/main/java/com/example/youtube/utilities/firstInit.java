//this is just for first init of te users and videos from json.
//-----------------------------------------------------------
package com.example.youtube.utilities;

public class firstInit {

    private static firstInit instance;
    private int flagIfInit = 0;

    // Private constructor to prevent instantiation
    private firstInit() {

    }

    // Public method to provide access to the instance
    public static firstInit getInstance() {
        if (instance == null) {
            synchronized (firstInit.class) {
                if (instance == null) {
                    instance = new firstInit();
                }
            }
        }
        return instance;
    }

    public int isInit() {
        return flagIfInit;
    }

    public void setInited() {
        this.flagIfInit = 1;
    }
}
