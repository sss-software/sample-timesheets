/*
 * Copyright (c) ${YEAR} ${PACKAGE_NAME}
 */

package com.haulmont.timesheets.web.toolkit.ui;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.timesheets.entity.WeeklyReportEntry;
import com.haulmont.timesheets.global.WorkConfigBean;
import com.haulmont.timesheets.web.calendar.TimeSheetsCalendarEventProvider;
import com.haulmont.timesheets.web.toolkit.ui.client.calendar.TimeSheetsCalendarState;
import com.vaadin.shared.ui.calendar.DateConstants;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author gorelov
 * @version $Id$
 */
public class TimeSheetsCalendar extends Calendar {

    public TimeSheetsCalendar(CalendarEventProvider eventProvider) {
        super(eventProvider);

        getState().weekends = getWeekends();
    }

    @Override
    public TimeSheetsCalendarState getState() {
        return (TimeSheetsCalendarState) super.getState();
    }

    @Override
    public TimeSheetsCalendarEventProvider getEventProvider() {
        return (TimeSheetsCalendarEventProvider) super.getEventProvider();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        getState().holidays = getHolidays();
    }

    protected Set<String> getHolidays() {
        int durationInDays = (int) (((endDate.getTime()) - startDate.getTime()) / DateConstants.DAYINMILLIS);
        durationInDays++;
        if (durationInDays > 60) {
            throw new RuntimeException("Daterange is too big (max 60) = "
                    + durationInDays);
        }

        Date firstDateToShow = expandStartDate(startDate, durationInDays > 7);
        Date lastDateToShow = expandEndDate(endDate, durationInDays > 7);

        return getEventProvider().getHolidays(firstDateToShow, lastDateToShow);
    }

    protected List<Integer> getWeekends() {
        WorkConfigBean workConfigBean = AppBeans.get(WorkConfigBean.NAME);
        UserSession userSession = AppBeans.get(UserSession.class);
        WeeklyReportEntry.DayOfWeek[] weekends = workConfigBean.getWeekends();
        List<Integer> dayNumbers = new ArrayList<>(weekends.length);
        for (WeeklyReportEntry.DayOfWeek day : weekends) {
            int number = WeeklyReportEntry.DayOfWeek.convertToDayOfWeekNumber(day, userSession.getLocale());
            if (number > 0) {
                dayNumbers.add(number);
            }
        }
        return dayNumbers;
    }
}