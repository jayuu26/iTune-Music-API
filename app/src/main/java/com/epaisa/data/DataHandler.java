package com.epaisa.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ist on 25/8/16.
 */
public class DataHandler {


    private TrackData trackData;


    public enum Single{
        INSTANCE;
        DataHandler s=new DataHandler();
        public DataHandler getInstance(){
            if(s==null)
                return new DataHandler();
            else return s;
        }
    }

    public  TrackData getTrackData() {
        return trackData;
    }

    public void setTrackData(TrackData trackData) {
        this.trackData = trackData;
    }
}
