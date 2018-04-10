package edu.cmu.adroitness.comm.calendar.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.commons.control.Constants;
import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarr on 8/4/15.
 */
public class CalendarNotificationEvent extends BaseEvent{
    private String notification;
    private boolean isError = false;
    private Object params;
    private List<CalendarEventVO> events;
    private List<CalendarEventVO> allEvents;
    private List<CalendarEventVO> newEvents = new ArrayList<>();
    private List<CalendarEventVO> updatedEvents = new ArrayList<>();
    private List<CalendarEventVO> deletedEvents = new ArrayList<>();

    private CalendarNotificationEvent(){ super(); }
    private CalendarNotificationEvent(int mbRequestId){ super( mbRequestId); }

    public static CalendarNotificationEvent build(){
        return new CalendarNotificationEvent();
    }
    public static CalendarNotificationEvent build(int mbRequestId){
        return new CalendarNotificationEvent(mbRequestId);
    }

    public String getNotification() {
        return notification;
    }

    public CalendarNotificationEvent setNotification(String notification) {
        this.notification = notification;
        return this;
    }

    public boolean isError() {
        return isError;
    }

    public CalendarNotificationEvent setIsError(boolean isError) {
        this.isError = isError;
        return this;
    }

    /**
     * Returns a list of calendar events filtered by a specific date (if it has been provided in
     * getCalendarEvents() method).
     * @return
     */
    public List<CalendarEventVO> getEvents() {
        return events;
    }


    public CalendarNotificationEvent setEvents(List<CalendarEventVO> events) {
        this.events = events;
        return this;
    }

    /**
     * Returns the whole set of calendar events, that is, ALL calendar events.
     * @return
     */
    public List<CalendarEventVO> getAllEvents() {
        return allEvents;
    }


    public CalendarNotificationEvent setAllEvents(List<CalendarEventVO> events) {
        this.allEvents = events;
        return this;
    }

    public Object getParams() {
        return params;
    }

    public CalendarNotificationEvent setParams(Object o) {
        this.params = o;
        return this;
    }

    /**
     * Returns a list of new calendar events that have been created during the last request.
     * @return
     */
    public List<CalendarEventVO> getNewEvents() {
        return newEvents;
    }

    public CalendarNotificationEvent setNewEvents(List<CalendarEventVO> newEvents) {
        this.newEvents = newEvents;
        return this;
    }

    public CalendarNotificationEvent setNewEvent(CalendarEventVO newEvent) {
        this.newEvents.add( newEvent );
        return this;
    }

    /**
     * Returns a list of deleted calendar events that have been permanently removed from Calendar
     * component during the last request.
     * @return
     */
    public List<CalendarEventVO> getDeletedEvents() {
        return deletedEvents;
    }

    public CalendarNotificationEvent setDeletedEvents(List<CalendarEventVO> deletedEvents) {
        this.deletedEvents = deletedEvents;
        return this;
    }

    /**
     * Returns a list of updated calendar events that have been modified during the last request.
     * @return
     */
    public List<CalendarEventVO> getUpdatedEvents() {
        return updatedEvents;
    }

    public CalendarNotificationEvent setUpdatedEvents(List<CalendarEventVO> updatedEvents) {
        this.updatedEvents = updatedEvents;
        return this;
    }

    public CalendarNotificationEvent setUpdatedEvent(CalendarEventVO updatedEvent) {
        this.updatedEvents.add(updatedEvent );
        return this;
    }

    public CalendarNotificationEvent setSyncEvents(List<CalendarEventVO> modifiedEvents) {
        if( notification == null || notification.equals(Constants.CALENDAR_AFTER_UPDATE_EVENT) ) {
            updatedEvents = modifiedEvents;
        } else if( notification.equals(Constants.CALENDAR_AFTER_CREATE_EVENT) ) {
            newEvents = modifiedEvents;
        } else if( notification.equals(Constants.CALENDAR_AFTER_DELETE_EVENTS) ){
            deletedEvents = modifiedEvents;
        } else{
            events = modifiedEvents;
        }
        return this;
    }
}
