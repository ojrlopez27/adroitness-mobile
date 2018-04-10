package edu.cmu.adroitness.client.services.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.OverScroller;

import edu.cmu.adroitness.client.commons.control.Util;
import edu.cmu.adroitness.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


class CompactCalendarController {

    private int paddingWidth = 40;
    private int paddingHeight = 40;
    private Paint dayPaint = new Paint();
    private Rect rect;
    private int textHeight;
    private static final int DAYS_IN_WEEK = 7;
    private int widthPerDay;
    private String[] dayColumnNames;
    private float distanceX;
    private PointF accumulatedScrollOffset = new PointF();
    private OverScroller scroller;
    private int monthsScrolledSoFar;
    private Date currentDate = new Date();
    private Locale locale = Locale.getDefault();
    private Calendar currentCalendar = Calendar.getInstance(locale);
    private Calendar todayCalendar = Calendar.getInstance(locale);
    private Calendar calendarWithFirstDayOfMonth = Calendar.getInstance(locale);
    private Calendar eventsCalendar = Calendar.getInstance(locale);
    private Direction currentDirection = Direction.NONE;
    private int heightPerDay;
    private int currentDayBackgroundColor;
    private int calendarTextColor;
    private int currentSelectedDayBackgroundColor;
    private int calendarBackgroundColor = Color.WHITE;
    private int textSize = 30;
    private int width;
    private int height;
    private int paddingRight;
    private int paddingLeft;
    private boolean shouldDrawDaysHeader = true;
    private Map<String, List<CalendarDayEvent>> events = new HashMap<>();
    private boolean showSmallIndicator;
    private float bigCircleIndicatorRadius;
    private float smallIndicatorRadius;
    private boolean shouldShowMondayAsFirstDay = true;
    private boolean useThreeLetterAbbreviation = false;
    private float screenDensity = 1;
    private int dayWithEventsColor;

    private enum Direction {
        NONE, HORIZONTAL, VERTICAL
    }

    CompactCalendarController(Paint dayPaint, OverScroller scroller, Rect rect, AttributeSet attrs,
                              Context context, int currentDayBackgroundColor, int calendarTextColor,
                              int currentSelectedDayBackgroundColor) {
        this.dayPaint = dayPaint;
        this.scroller = scroller;
        this.rect = rect;
        this.currentDayBackgroundColor = currentDayBackgroundColor;
        this.calendarTextColor = calendarTextColor;
        this.currentSelectedDayBackgroundColor = currentSelectedDayBackgroundColor;
        loadAttributes(attrs, context);
        init(context);
    }

    private void loadAttributes(AttributeSet attrs, Context context) {
        if (attrs != null && context != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CompactCalendarView, 0, 0);
            try {
                currentDayBackgroundColor = typedArray.getColor(R.styleable.CompactCalendarView_compactCalendarCurrentDayBackgroundColor, currentDayBackgroundColor);
                calendarTextColor = typedArray.getColor(R.styleable.CompactCalendarView_compactCalendarTextColor, calendarTextColor);
                currentSelectedDayBackgroundColor = typedArray.getColor(R.styleable.CompactCalendarView_compactCalendarCurrentSelectedDayBackgroundColor, currentSelectedDayBackgroundColor);
                calendarBackgroundColor = typedArray.getColor(R.styleable.CompactCalendarView_compactCalendarBackgroundColor, calendarBackgroundColor);
                textSize = typedArray.getDimensionPixelSize(R.styleable.CompactCalendarView_compactCalendarTextSize,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, context.getResources().getDisplayMetrics()));
                dayWithEventsColor = Util.hex2Rgb("#CFD4EC");
            } finally {
                typedArray.recycle();
            }
        }
    }

    private void init(Context context) {
        setUseWeekDayAbbreviation(false);
        dayPaint.setTextAlign(Paint.Align.CENTER);
        dayPaint.setStyle(Paint.Style.STROKE);
        dayPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        dayPaint.setTypeface(Typeface.SANS_SERIF);
        dayPaint.setTextSize(textSize);
        dayPaint.setColor(calendarTextColor);
        dayPaint.getTextBounds("31", 0, "31".length(), rect);
        textHeight = rect.height() * 3;

        todayCalendar.setTime(currentDate);
        setToMidnight(todayCalendar);

        currentCalendar.setTime(currentDate);
        setCalendarToFirstDayOfMonth(calendarWithFirstDayOfMonth, currentDate, -monthsScrolledSoFar, 0);

        eventsCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        if(context != null){
             screenDensity =  context.getResources().getDisplayMetrics().density;
        }
    }

    private void setCalendarToFirstDayOfMonth(Calendar calendarWithFirstDayOfMonth, Date currentDate, int scrollOffset, int monthOffset) {
        setMonthOffset(calendarWithFirstDayOfMonth, currentDate, scrollOffset, monthOffset);
        calendarWithFirstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);
    }

    private void setMonthOffset(Calendar calendarWithFirstDayOfMonth, Date currentDate, int scrollOffset, int monthOffset) {
        calendarWithFirstDayOfMonth.setTime(currentDate);
        calendarWithFirstDayOfMonth.add(Calendar.MONTH, scrollOffset + monthOffset);
        calendarWithFirstDayOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        calendarWithFirstDayOfMonth.set(Calendar.MINUTE, 0);
        calendarWithFirstDayOfMonth.set(Calendar.SECOND, 0);
        calendarWithFirstDayOfMonth.set(Calendar.MILLISECOND, 0);
    }

    void removeAllEvents(){
        events.clear();
    }

    void setShouldShowMondayAsFirstDay(boolean shouldShowMondayAsFirstDay) {
        this.shouldShowMondayAsFirstDay = shouldShowMondayAsFirstDay;
        setUseWeekDayAbbreviation(useThreeLetterAbbreviation);
        if (shouldShowMondayAsFirstDay) {
            eventsCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        } else {
            eventsCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        }
    }

    void setCurrentSelectedDayBackgroundColor(int currentSelectedDayBackgroundColor) {
        this.currentSelectedDayBackgroundColor = currentSelectedDayBackgroundColor;
    }

    void setCalendarBackgroundColor(int calendarBackgroundColor) {
        this.calendarBackgroundColor = calendarBackgroundColor;
    }

    void setCurrentDayBackgroundColor(int currentDayBackgroundColor) {
        this.currentDayBackgroundColor = currentDayBackgroundColor;
    }

    void showNextMonth() {
        setCalendarToFirstDayOfMonth(calendarWithFirstDayOfMonth, currentCalendar.getTime(), 0, 1);
        setCurrentDate(calendarWithFirstDayOfMonth.getTime());
    }

    void showPreviousMonth() {
        setCalendarToFirstDayOfMonth(calendarWithFirstDayOfMonth, currentCalendar.getTime(), 0, -1);
        setCurrentDate(calendarWithFirstDayOfMonth.getTime());
    }

    void setLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }
        this.locale = locale;
    }

    void setUseWeekDayAbbreviation(boolean useThreeLetterAbbreviation) {
        this.useThreeLetterAbbreviation = useThreeLetterAbbreviation;
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        String[] dayNames = dateFormatSymbols.getShortWeekdays();
        if (dayNames == null) {
            throw new IllegalStateException("Unable to determine weekday names from default locale");
        }
        if (dayNames.length != 8) {
            throw new IllegalStateException("Expected weekday names from default locale to be of size 7 but: "
                    + Arrays.toString(dayNames) + " with size " + dayNames.length + " was returned.");
        }

        if (useThreeLetterAbbreviation) {
            if (!shouldShowMondayAsFirstDay) {
                this.dayColumnNames = new String[]{dayNames[1], dayNames[2], dayNames[3], dayNames[4], dayNames[5], dayNames[6], dayNames[7]};
            } else {
                this.dayColumnNames = new String[]{dayNames[2], dayNames[3], dayNames[4], dayNames[5], dayNames[6], dayNames[7], dayNames[1]};
            }
        } else {
            if (!shouldShowMondayAsFirstDay) {
                this.dayColumnNames = new String[]{dayNames[1].substring(0, 1), dayNames[2].substring(0, 1),
                        dayNames[3].substring(0, 1), dayNames[4].substring(0, 1), dayNames[5].substring(0, 1), dayNames[6].substring(0, 1), dayNames[7].substring(0, 1)};
            } else {
                this.dayColumnNames = new String[]{dayNames[2].substring(0, 1), dayNames[3].substring(0, 1),
                        dayNames[4].substring(0, 1), dayNames[5].substring(0, 1), dayNames[6].substring(0, 1), dayNames[7].substring(0, 1), dayNames[1].substring(0, 1)};
            }
        }
    }

    void setDayColumnNames(String[] dayColumnNames) {
        if (dayColumnNames == null || dayColumnNames.length != 7) {
            throw new IllegalArgumentException("Column names cannot be null and must contain a value for each day of the week");
        }
        this.dayColumnNames = dayColumnNames;
    }


    void setShouldDrawDaysHeader(boolean shouldDrawDaysHeader) {
        this.shouldDrawDaysHeader = shouldDrawDaysHeader;
    }

    void showSmallIndicator(boolean showSmallIndicator) {
        this.showSmallIndicator = showSmallIndicator;
    }

    void onMeasure(int width, int height, int paddingRight, int paddingLeft) {
        widthPerDay = (width) / DAYS_IN_WEEK;
        heightPerDay = height / 7;
        this.width = width;
        this.height = height;
        this.paddingRight = paddingRight;
        this.paddingLeft = paddingLeft;

        //scale small indicator by screen density
        smallIndicatorRadius = 2.5f * screenDensity;

        //assume square around each day of width and height = heightPerDay and get diagonal line length
        //makes easier to find radius
        double radiusAroundDay = 0.5 * Math.sqrt((heightPerDay * heightPerDay) + (heightPerDay * heightPerDay));
        //make radius based on screen density
        bigCircleIndicatorRadius = (float) radiusAroundDay /  ((1.8f) - 0.5f / screenDensity) ;
    }

    void onDraw(Canvas canvas) {
        paddingWidth = widthPerDay / 2;
        paddingHeight = heightPerDay / 2;
        calculateXPositionOffset();
        drawCalendarBackground(canvas);
        drawScrollableCalendar(canvas);
    }

    boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (currentDirection == Direction.HORIZONTAL) {
                monthsScrolledSoFar = Math.round(accumulatedScrollOffset.x / width);
                float remainingScrollAfterFingerLifted = (accumulatedScrollOffset.x - monthsScrolledSoFar * width);
                scroller.startScroll((int) accumulatedScrollOffset.x, 0, (int) -remainingScrollAfterFingerLifted, 0);
                currentDirection = Direction.NONE;
                setCalendarToFirstDayOfMonth(calendarWithFirstDayOfMonth, currentDate, -monthsScrolledSoFar, 0);

                if (calendarWithFirstDayOfMonth.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)) {
                    setCalendarToFirstDayOfMonth(currentCalendar, currentDate, -monthsScrolledSoFar, 0);
                }

                return true;
            }
            currentDirection = Direction.NONE;
        }
        return false;
    }

    int getHeightPerDay() {
        return heightPerDay;
    }

    int getWeekNumberForCurrentMonth() {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(currentDate);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    Date getFirstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, -monthsScrolledSoFar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setToMidnight(calendar);
        return calendar.getTime();
    }

    void setCurrentDate(Date dateTimeMonth) {
        distanceX = 0;
        monthsScrolledSoFar = 0;
        accumulatedScrollOffset.x = 0;
        scroller.startScroll(0, 0, 0, 0);
        currentDate = new Date(dateTimeMonth.getTime());
        currentCalendar.setTime(currentDate);
        setToMidnight(currentCalendar);
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    void addEvent(CalendarDayEvent event) {
        eventsCalendar.setTimeInMillis(event.getTimeInMillis());
        String key = getKeyForCalendarEvent(eventsCalendar);
        List<CalendarDayEvent> uniqCalendarDayEvents = events.get(key);
        if (uniqCalendarDayEvents == null) {
            uniqCalendarDayEvents = new ArrayList<>();
        }
        if (!uniqCalendarDayEvents.contains(event)) {
            uniqCalendarDayEvents.add(event);
        }
        events.put(key, uniqCalendarDayEvents);
    }

    void addEvents(List<CalendarDayEvent> events) {
        int count = events.size();
        for (int i = 0; i < count; i++) {
            addEvent(events.get(i));
        }
    }

    void removeEvent(CalendarDayEvent event) {
        eventsCalendar.setTimeInMillis(event.getTimeInMillis());
        String key = getKeyForCalendarEvent(eventsCalendar);
        List<CalendarDayEvent> uniqCalendarDayEvents = events.get(key);
        if (uniqCalendarDayEvents != null) {
            uniqCalendarDayEvents.remove(event);
        }
    }

    void removeEvents(List<CalendarDayEvent> events) {
        int count = events.size();
        for (int i = 0; i < count; i++) {
            removeEvent(events.get(i));
        }
    }

    List<CalendarDayEvent> getEvents(Date date) {
        eventsCalendar.setTimeInMillis(date.getTime());
        String key = getKeyForCalendarEvent(eventsCalendar);
        List<CalendarDayEvent> uniqEvents = events.get(key);
        if (uniqEvents != null) {
            return uniqEvents;
        } else {
            return new ArrayList<>();
        }
    }

    //E.g. 4 2016 becomes 2016_4
    private String getKeyForCalendarEvent(Calendar cal) {
        return cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.MONTH);
    }

    Date onSingleTapConfirmed(MotionEvent e) {
        monthsScrolledSoFar = Math.round(accumulatedScrollOffset.x / width);
        int dayColumn = Math.round((paddingLeft + e.getX() - paddingWidth - paddingRight) / widthPerDay);
        int dayRow = Math.round((e.getY() - paddingHeight) / heightPerDay);

        setCalendarToFirstDayOfMonth(calendarWithFirstDayOfMonth, currentDate, -monthsScrolledSoFar, 0);

        //Start Monday as day 1 and Sunday as day 7. Not Sunday as day 1 and Monday as day 2
        int firstDayOfMonth = getDayOfWeek(calendarWithFirstDayOfMonth);

        int dayOfMonth = ((dayRow - 1) * 7 + dayColumn + 1) - firstDayOfMonth;

        if (dayOfMonth < calendarWithFirstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
                && dayOfMonth >= 0) {
            calendarWithFirstDayOfMonth.add(Calendar.DATE, dayOfMonth);

            currentCalendar.setTimeInMillis(calendarWithFirstDayOfMonth.getTimeInMillis());
            return currentCalendar.getTime();
        } else {
            return null;
        }
    }

    boolean onDown(MotionEvent e) {
        scroller.forceFinished(true);
        return true;
    }

    boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        scroller.forceFinished(true);
        return true;
    }

    boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (currentDirection == Direction.NONE) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                currentDirection = Direction.HORIZONTAL;
            } else {
                currentDirection = Direction.VERTICAL;
            }
        }

        this.distanceX = distanceX;
        return true;
    }

    boolean computeScroll() {
        if (scroller.computeScrollOffset()) {
            accumulatedScrollOffset.x = scroller.getCurrX();
            return true;
        }
        return false;
    }

    private void drawScrollableCalendar(Canvas canvas) {
        monthsScrolledSoFar = (int) (accumulatedScrollOffset.x / width);
        drawPreviousMonth(canvas);
        drawCurrentMonth(canvas);
        drawNextMonth(canvas);
    }

    private void drawNextMonth(Canvas canvas) {
        setCalendarToFirstDayOfMonth(calendarWithFirstDayOfMonth, currentDate, -monthsScrolledSoFar, 1);
        drawMonth(canvas, calendarWithFirstDayOfMonth, (width * (-monthsScrolledSoFar + 1)));
    }

    private void drawCurrentMonth(Canvas canvas) {
        setCalendarToFirstDayOfMonth(calendarWithFirstDayOfMonth, currentDate, -monthsScrolledSoFar, 0);
        drawMonth(canvas, calendarWithFirstDayOfMonth, width * -monthsScrolledSoFar);
    }

    private void drawPreviousMonth(Canvas canvas) {
        setCalendarToFirstDayOfMonth(calendarWithFirstDayOfMonth, currentDate, -monthsScrolledSoFar, -1);
        drawMonth(canvas, calendarWithFirstDayOfMonth, (width * (-monthsScrolledSoFar - 1)));
    }

    private void calculateXPositionOffset() {
        if (currentDirection == Direction.HORIZONTAL) {
            accumulatedScrollOffset.x -= distanceX;
        }
    }

    private void drawCalendarBackground(Canvas canvas) {
        dayPaint.setColor(calendarBackgroundColor);
        dayPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, width, height, dayPaint);
        dayPaint.setStyle(Paint.Style.STROKE);
        dayPaint.setColor(calendarTextColor);
    }

    List<Integer> drawEvents(Canvas canvas, Calendar currentMonthToDrawCalendar, int offset) {
        List<CalendarDayEvent> uniqCalendarDayEvents =
                events.get(getKeyForCalendarEvent(currentMonthToDrawCalendar));
        List<Integer> days = new ArrayList<>();

        boolean shouldDrawCurrentDayCircle = currentMonthToDrawCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH);
        int todayDayOfMonth = todayCalendar.get(Calendar.DAY_OF_MONTH);

        if (uniqCalendarDayEvents != null) {
            for (int i = 0; i < uniqCalendarDayEvents.size(); i++) {
                CalendarDayEvent event = uniqCalendarDayEvents.get(i);
                long timeMillis = event.getTimeInMillis();
                eventsCalendar.setTimeInMillis(timeMillis);

                int dayOfWeek = getDayOfWeek(eventsCalendar) - 1;

                int weekNumberForMonth = eventsCalendar.get(Calendar.WEEK_OF_MONTH);
                float xPosition = widthPerDay * dayOfWeek + paddingWidth + paddingLeft + accumulatedScrollOffset.x + offset - paddingRight;
                float yPosition = weekNumberForMonth * heightPerDay + paddingHeight;

                int dayOfMonth = eventsCalendar.get(Calendar.DAY_OF_MONTH);
                boolean isSameDayAsCurrentDay = (todayDayOfMonth == dayOfMonth && shouldDrawCurrentDayCircle);
                if (!isSameDayAsCurrentDay) {
                    if (showSmallIndicator) {
                        //draw small indicators below the day in the calendar
                        drawSmallIndicatorCircle(canvas, xPosition, yPosition + 15, event.getColor());
                    } else {
                        drawCircle(canvas, xPosition, yPosition, dayWithEventsColor); //event.getColor());
                        days.add( dayOfMonth );
                    }
                }
            }
        }
        return days;
    }

    private int getDayOfWeek(Calendar calendar) {
        int dayOfWeek;
        if (!shouldShowMondayAsFirstDay) {
            return calendar.get(Calendar.DAY_OF_WEEK);
        } else {
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            dayOfWeek = dayOfWeek <= 0 ? 7 : dayOfWeek;
        }
        return dayOfWeek;
    }

    void drawMonth(Canvas canvas, Calendar monthToDrawCalendar, int offset) {
        List days = drawEvents(canvas, monthToDrawCalendar, offset);
        //offset by one because we want to start from Monday
        int firstDayOfMonth = getDayOfWeek(monthToDrawCalendar);

        //offset by one because of 0 index based calculations
        firstDayOfMonth = firstDayOfMonth - 1;
        boolean isSameMonthAsToday = monthToDrawCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH);
        boolean isSameYearAsToday = monthToDrawCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR);
        boolean isSameMonthAsCurrentCalendar = monthToDrawCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH);
        int todayDayOfMonth = todayCalendar.get(Calendar.DAY_OF_MONTH);

        for (int dayColumn = 0, dayRow = 0; dayColumn <= 6; dayRow++) {
            if (dayRow == 7) {
                dayRow = 0;
                if (dayColumn <= 6) {
                    dayColumn++;
                }
            }
            if (dayColumn == dayColumnNames.length) {
                break;
            }
            float xPosition = widthPerDay * dayColumn + paddingWidth + paddingLeft + accumulatedScrollOffset.x + offset - paddingRight;
            if (dayRow == 0) {
                // first row, so draw the first letter of the day
                if (shouldDrawDaysHeader) {
                    dayPaint.setTypeface(Typeface.DEFAULT_BOLD);
                    canvas.drawText(dayColumnNames[dayColumn], xPosition, paddingHeight, dayPaint);
                    dayPaint.setTypeface(Typeface.DEFAULT);
                }
            } else {
                int day = ((dayRow - 1) * 7 + dayColumn + 1) - firstDayOfMonth;
                float yPosition = dayRow * heightPerDay + paddingHeight;
                if (isSameYearAsToday && isSameMonthAsToday && todayDayOfMonth == day) {
                    // TODO calculate position of circle in a more reliable way
                    drawCircle(canvas, xPosition, yPosition, currentDayBackgroundColor);
                } else if (currentCalendar.get(Calendar.DAY_OF_MONTH) == day && isSameMonthAsCurrentCalendar) {
                    drawCircle(canvas, xPosition, yPosition, currentSelectedDayBackgroundColor);
                } else if (day == 1 && !isSameMonthAsCurrentCalendar) {
                    drawCircle(canvas, xPosition, yPosition, currentSelectedDayBackgroundColor);
                }
                if (day <= monthToDrawCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) && day > 0) {
                    if( days.contains( day ) ){
                        dayPaint.setColor( Color.BLACK );
                    }
                    canvas.drawText(String.valueOf(day), xPosition, yPosition, dayPaint);
                    dayPaint.setColor( calendarTextColor );
                }
            }
        }
    }

    // Draw Circle on certain days to highlight them
    private void drawCircle(Canvas canvas, float x, float y, int color) {
        dayPaint.setColor(color);
        drawCircle(canvas, bigCircleIndicatorRadius, x, y - (textHeight / 6));
    }

    private void drawSmallIndicatorCircle(Canvas canvas, float x, float y, int color) {
        dayPaint.setColor(color);
        drawCircle(canvas, smallIndicatorRadius, x, y);
    }

    private void drawCircle(Canvas canvas, float radius, float x, float y) {
        dayPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, radius, dayPaint);
        dayPaint.setStyle(Paint.Style.STROKE);
        dayPaint.setColor(calendarTextColor);
    }

}
