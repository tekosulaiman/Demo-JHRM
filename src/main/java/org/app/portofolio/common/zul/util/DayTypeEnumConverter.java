/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.app.portofolio.common.zul.util;

import org.app.portofolio.common.zul.util.Enum.DayType;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

/**
 *
 * @author achmadsy
 */
public class DayTypeEnumConverter implements Converter {

    @Override
    public Object coerceToUi(Object val, Component comp, BindContext ctx) {

        Combobox box = (Combobox) comp;
        Integer value = null;
        String enumDesc = null;
        if (box.getSelectedItem() == null) {
            if (val != null) {
                value = (Integer) val;

                for (DayType type : DayType.values()) {
                    if (type.getDayType() == value) {
                        enumDesc = type.getDayTypeDesc();
                        break;
                    }
                }
            }
            return enumDesc;
        } else {
            return box.getSelectedItem().getLabel();
        }

    }

    @Override
    public Object coerceToBean(Object val, Component comp, BindContext ctx) {

        Combobox box = (Combobox) comp;

        if (box.getSelectedItem() == null) {
            return val;
        } else {
            return box.getSelectedItem().getValue();
        }

    }

}
