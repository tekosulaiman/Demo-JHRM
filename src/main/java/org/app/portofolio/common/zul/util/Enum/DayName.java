/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.app.portofolio.common.zul.util.Enum;

import org.zkoss.util.resource.Labels;

/**
 *
 * @author achmadsy
 */
public enum DayName {
    Day1(Labels.getLabel("menu.leave.leaveparameter.workweek.day1")),
    Day2(Labels.getLabel("menu.leave.leaveparameter.workweek.day2")),
    Day3(Labels.getLabel("menu.leave.leaveparameter.workweek.day3")),
    Day4(Labels.getLabel("menu.leave.leaveparameter.workweek.day4")),
    Day5(Labels.getLabel("menu.leave.leaveparameter.workweek.day5")),
    Day6(Labels.getLabel("menu.leave.leaveparameter.workweek.day6")),
    Day7(Labels.getLabel("menu.leave.leaveparameter.workweek.day7"));
    
    public String label;

    DayName(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
}
