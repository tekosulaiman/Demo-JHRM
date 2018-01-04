/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.app.portofolio.common.zul.util.Enum;

/**
 *
 * @author achmadsy
 */
public enum DayType {
    FullDay("Full Day",0),
    HalfDay("Half Day",1),
    NonWorkingDay("Non-working day",2);
    
    public String dayTypeDesc;
    public int dayType;

    DayType(String dayTypeDesc, int dayType) {
        this.dayTypeDesc = dayTypeDesc;
        this.dayType = dayType;
    }

    public String getDayTypeDesc() {
        return this.dayTypeDesc;
    }

    public int getDayType() {
        return dayType;
    }
}
