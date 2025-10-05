package com.dates;

import java.time.Year;
import java.time.YearMonth;

public class MyDate {

    /* This private data member holds the calculated Julian number for this MyDate */
    private int julianNumber;

    /* If no arguments were provided then default the date January 1st, 1970 (epoch time). */
    public MyDate()
    {
        int month = 1;
        int day = 1;
        int year = 1970;

        // self instance to convert our default values using our private method below
        this.julianNumber = toJulianNumber(day,month,year);

    }
    /* Creates a new MyDate from an existing MyDate */
    public MyDate( MyDate date)
    {
        this.julianNumber = date.julianNumber;
    }
    /* Creates a new MyDate from a day, month, and year */
    public MyDate( int day, int month, int year)
    {
        this.julianNumber = toJulianNumber(day,month,year);
    }

    /* Returns the day of the month for this MyDate */
    public int getDay()
    {
        return fromJulianNumber()[0];
    }
    /* Returns the month of the year for this MyDate */
    public int getMonth()
    {
        return fromJulianNumber()[1];
    }
    /* Returns the year for this MyDate */
    public int getYear()
    {
        return fromJulianNumber()[2];
    }
    /* Returns true if this MyDate represents a date in a leap year */
    public static boolean isLeapYear( int year )
    {
        return Year.isLeap( year );
    }

    public static int getLastDayOfMonth( int month, int year )
    {
        // returns the length of the month, which is the last day of the month
        return YearMonth.of( year, month ).lengthOfMonth();
    }

    /* This internal method returns the calculated Julian number for the provided day, month, year
     * This method is static (belongs to the class not objects),
     * as it does not require a MyDate object to perform its computation
     */
    private static int toJulianNumber(int day, int month, int year)
    {
        JulianDateConvertor julian = new JulianDateConvertor(day,month,year);
        return julian.convertDateToJulian();
    }
    /* This internal method returns a 3-integer array
     * containing the day, month, and year of this MyDate
     */
    private int[] fromJulianNumber()
    {
        JulianDateConvertor myNewDate = new JulianDateConvertor(julianNumber);
        String calendarDate = myNewDate.convertJulianToDate();
        String[] dateArray = calendarDate.split("-");
        int[] dateArrayInt = new int[3];
        dateArrayInt[0] = Integer.parseInt(dateArray[0]);
        dateArrayInt[1] = Integer.parseInt(dateArray[1]);
        dateArrayInt[2] = Integer.parseInt(dateArray[2]);
        return dateArrayInt;
    }

}