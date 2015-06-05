/*
 * Copyright (c) ${YEAR} ${PACKAGE_NAME}
 */

package com.haulmont.timesheets.gui;

import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.timesheets.global.HoursAndMinutes;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class HoursAndMinutesFormatter implements Formatter<Integer> {
    @Override
    public String format(Integer value) {
        return new HoursAndMinutes(0, value != null ? value : 0).toString();
    }
}
