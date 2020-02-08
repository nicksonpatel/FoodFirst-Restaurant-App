package com.friendlyitsolution.meet.rest_app;

/**
 * Created by Meet on 16-10-2017.
 */

public class plan_model
{
    String name,dec,fees,capacity,eprice,key;

    public plan_model(String name, String fees, String dec,String capacity,String eprice,String key)
    {
        this.key=key;
        this.name=name;
        this.dec=dec;
        this.eprice=eprice;
        this.capacity=capacity;
        this.fees=fees;
    }

}
