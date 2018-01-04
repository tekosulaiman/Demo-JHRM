/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.app.portofolio.common.zul.util;

import org.app.portofolio.common.zul.util.Enum.DayName;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

/**
 *
 * @author achmadsy
 */
public class DayNameEnumConverter implements Converter {

    @Override
    public Object coerceToUi(Object val, Component comp, BindContext ctx) {

        String value = null;
        if (val != null) {
            value = (String) val;
        }
        return DayName.valueOf(value).getLabel();

    }

    @Override
    public Object coerceToBean(Object val, Component comp, BindContext ctx) {

        return DayName.valueOf((String) val).name();

    }

}
