/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.app.portofolio.common.zul.util;

import java.util.Map;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

/**
 *
 * @author achmadsy
 */
public class MapToBeanConverter implements Converter {

    @Override
    public Object coerceToUi(Object val, Component comp, BindContext ctx) {
        Combobox box = (Combobox) comp;
        if (box.getSelectedItem() == null) {
            String value = null;
            if (val != null) {
                Map map = (Map) box.getModel();
                for (Object obj : map.keySet()) {
                    if (map.get(obj) == val) {
                        value = (String) obj;
                    }
                }
            }
            return value;
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
