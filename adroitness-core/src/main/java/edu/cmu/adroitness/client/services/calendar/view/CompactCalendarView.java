package edu.cmu.adroitness.client.services.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;


import edu.cmu.adroitness.client.services.calendar.model.CalendarEventVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CompactCalendarView extends View {

    private CompactCalendarController monthCalendarController;
    private GestureDetectorCompat gestureDetector;
    private CompactCalendarViewListener listener;
    private boolean shouldScroll = true;

    public interface CompactCalendarViewListener {
        void onDayClick(Date dateClicked);
        void onMonthScroll(Date firstDayOfNewMonth);
    }

    private final GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Date onDateClicked = monthCalendarController.onSingleTapConfirmed(e);
            invalidate();
            if(listener != null && onDateClicked != null){
                listener.onDayClick(onDateClicked);
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return monthCalendarController.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            monthCalendarController.onFling(e1, e2, velocityX, velocityY);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(shouldScroll) {
                monthCalendarController.onScroll(e1, e2, distanceX, distanceY);
                invalidate();
            }
            return true;
        }
    };

    public CompactCalendarView(Context context) {
        this(context, null);
    }

    public CompactCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompactCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        monthCalendarController = new CompactCalendarController(new Paint(), new OverScroller(getContext()),
                new Rect(), attrs, getContext(),  Color.argb(255, 207, 212, 236), Color.argb(255, 64, 64, 64), Color.argb(255, 219, 219, 219));
        gestureDetector = new GestureDetectorCompat(getContext(), gestureListener);
    }

    /**
     * Use a custom locale for compact calendar.
     * @param locale
     */
    public void setLocale(Locale locale){
        monthCalendarController.setLocale(locale);
        invalidate();
    }

    /**
     * Compact calendar will use the locale to determine the abbreviation to use as the day column names.
     * The default is to use the default locale and to abbreviate the day names to one character.
     * Setting this to true will displace the short weekday string provided by java.
     * @param useThreeLetterAbbreviation
     */
    public void setUseThreeLetterAbbreviation(boolean useThreeLetterAbbreviation){
        monthCalendarController.setUseWeekDayAbbreviation(useThreeLetterAbbreviation);
        invalidate();
    }

    public void setCalendarBackgroundColor(final int calendarBackgroundColor) {
        monthCalendarController.setCalendarBackgroundColor(calendarBackgroundColor);
        invalidate();
    }

    /**
     *
     Will draw the indicator for events as a small dot under the day rather than a circle behind the day.
     * @param shouldDrawDaysHeader
     */
    public void drawSmallIndicatorForEvents(boolean shouldDrawDaysHeader){
        monthCalendarController.showSmallIndicator(shouldDrawDaysHeader);
    }

    /**
     * Sets the name for each day of the week. No attempt is made to adjust width or text size based
     * on the length of each day name. Works best with 3-4 characters for each day.
     * @param dayColumnNames
     */
    public void setDayColumnNames(String[] dayColumnNames){
       monthCalendarController.setDayColumnNames(dayColumnNames);
    }

    public void setShouldShowMondayAsFirstDay(boolean shouldShowMondayAsFirstDay) {
        monthCalendarController.setShouldShowMondayAsFirstDay(shouldShowMondayAsFirstDay);
        invalidate();
    }

    public void setCurrentSelectedDayBackgroundColor(int currentSelectedDayBackgroundColor) {
        monthCalendarController.setCurrentSelectedDayBackgroundColor(currentSelectedDayBackgroundColor);
        invalidate();
    }

    public void setCurrentDayBackgroundColor(int currentDayBackgroundColor) {
        monthCalendarController.setCurrentDayBackgroundColor(currentDayBackgroundColor);
        invalidate();
    }

    public int getHeightPerDay(){
        return monthCalendarController.getHeightPerDay();
    }

    public void setListener(CompactCalendarViewListener listener){
        this.listener = listener;
    }

    public Date getFirstDayOfCurrentMonth(){
        return monthCalendarController.getFirstDayOfCurrentMonth();
    }

    public void setCurrentDate(Date dateTimeMonth){
        monthCalendarController.setCurrentDate(dateTimeMonth);
        invalidate();
    }

    public int getWeekNumberForCurrentMonth(){
        return monthCalendarController.getWeekNumberForCurrentMonth();
    }

    public void setShouldDrawDaysHeader(boolean shouldDrawDaysHeader){
        monthCalendarController.setShouldDrawDaysHeader(shouldDrawDaysHeader);
    }

    /**
     * see {@link #addEvent(CalendarDayEvent, boolean)} when adding single events
     * or {@link #addDayEvents(java.util.List)}  when adding multiple events
     * @param event
     */
    @Deprecated
    public void addEvent(CalendarDayEvent event){
        addEvent(event, false);
    }

    /**
     *  Adds an event to be drawn as an indicator in the calendar.
     *  If adding multiple events see {@link #addDayEvents(List)}} method.
     * @param event to be added to the calendar
     * @param shouldInvalidate true if the view should invalidate
     */
    public void addEvent(CalendarDayEvent event, boolean shouldInvalidate){
        monthCalendarController.addEvent(event);
        if(shouldInvalidate){
            invalidate();
        }
    }

    /**
    * Adds multiple events to the calendar and invalidates the view once all events are added.
    */
    public void addDayEvents(List<CalendarDayEvent> events){
       monthCalendarController.addEvents(events);
       invalidate();
    }

    public void addEvents(List<CalendarEventVO> events){
        ArrayList<CalendarDayEvent> dayEvents = new ArrayList<>();
        for( CalendarEventVO event : events ){
            dayEvents.add( new CalendarDayEvent(event.getStartDate().getTime() ) );
        }
        addDayEvents(dayEvents);
    }


    /**
     * see {@link #removeEvent(CalendarDayEvent, boolean)} when removing single events
     * or {@link #removeEvents(java.util.List)} (java.util.List)}  when removing multiple events
     * @param event
     */
    @Deprecated
    public void removeEvent(CalendarDayEvent event){
        removeEvent(event, false);
    }

    /**
     * Removes an event from the calendar.
     * If removing multiple events see {@link #removeEvents(List)}
     *
     * @param event event to remove from the calendar
     * @param shouldInvalidate true if the view should invalidate
     */
    public void removeEvent(CalendarDayEvent event, boolean shouldInvalidate){
        monthCalendarController.removeEvent(event);
        if(shouldInvalidate){
            invalidate();
        }
    }

    /**
    * Adds multiple events to the calendar and invalidates the view once all events are added.
    */
    public void removeEvents(List<CalendarDayEvent> events){
        monthCalendarController.removeEvents(events);
        invalidate();
    }

    /**
     * Clears all Events from the calendar.
     */
    public void removeAllEvents() {
        monthCalendarController.removeAllEvents();
    }

    public void showNextMonth(){
        monthCalendarController.showNextMonth();
        invalidate();
        if(listener != null){
             listener.onMonthScroll(monthCalendarController.getFirstDayOfCurrentMonth());
        }
    }

    public void showPreviousMonth(){
        monthCalendarController.showPreviousMonth();
        invalidate();
        if(listener != null){
             listener.onMonthScroll(monthCalendarController.getFirstDayOfCurrentMonth());
        }
    }

    @Override
    protected void onMeasure(int parentWidth, int parentHeight) {
        super.onMeasure(parentWidth, parentHeight);
        int width = MeasureSpec.getSize(parentWidth);
        int height = MeasureSpec.getSize(parentHeight);
        if(width > 0 && height > 0) {
            monthCalendarController.onMeasure(width, height, getPaddingRight(), getPaddingLeft());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        monthCalendarController.onDraw(canvas);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(monthCalendarController.computeScroll()){
            invalidate();
        }
    }

    public void shouldScrollMonth(boolean shouldDisableScroll){
        this.shouldScroll = shouldDisableScroll;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(monthCalendarController.onTouch(event) && shouldScroll){
            invalidate();
            if(listener != null){
                listener.onMonthScroll(monthCalendarController.getFirstDayOfCurrentMonth());
            }
            return true;
        }
        return gestureDetector.onTouchEvent(event);
    }

}
