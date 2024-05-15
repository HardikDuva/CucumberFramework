package com.amazon.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.amazon.utilities.TestLogger.debug;
import static com.amazon.utilities.TestLogger.info;

public final class DateHelper {

    /**
     * There should not be an instance of this class.
     */
    private DateHelper() { }

    /**
     * Returns the current year.
     * @return A {@link String } containing the current year.
     */
    public static String getCurrentYear() {
        String format = "yyyy";
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Returns the current month.
     * @return A {@link String } containing the current month.
     */
    public static String getCurrentMonth() {
        String format = "MMMMMMMMM";
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Get the Current Date.
     * @param format The Date Format.
     * @return A {@link String } containing the date today in a
     * particular format.
     */
    public static String getCurrentDate(final String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Get Yesterday's Date.
     * @param format The date format.
     * @return A {@link String } containing the date yesterday in a
     * particular format.
     */
    public static String getYesterdayDate(final String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * Get Yesterday's date (numerical day i.e. 6 or 31).
     * @return A {@link String } containing the day of the month yesterday.
     */
    public static String getYesterdaysDayOfTheMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        DateFormat formatter = new SimpleDateFormat("dd");
        System.out.println("Date is " + formatter.format(date));
        // make sure that if the date is a number less than 10,
        // remove the 0 in front of the number i.e. (02 should be 2)
        return formatter.format(date).substring(0, 1)
                .equalsIgnoreCase("0")
                ? formatter.format(date).substring(1, 2)
                : formatter.format(date).substring(0, 2);
    }

    /**
     * Get Tomorrow's Date (numerical day i.e. 6 or 31).
     * @return A {@link String } containing the day of the month.
     */
    public static String getTomorrowsDayOfTheMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        DateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(date).substring(0, 1)
                .equalsIgnoreCase("0")
                ? formatter.format(date).substring(1, 2)
                : formatter.format(date).substring(0, 2);
    }

    /**
     * Get Tomorrow's Date (numerical day i.e. 6 or 31).
     * @return A {@link String } containing the day of the month.
     */
    public static String getTheyDayAfterTomorrowsDayOfTheMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        Date date = calendar.getTime();
        DateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(date).substring(0, 1)
                .equalsIgnoreCase("0")
                ? formatter.format(date).substring(1, 2)
                : formatter.format(date).substring(0, 2);
    }

    /**
     * Get Tomorrow's Date.
     * @param format The date format.
     * @return A {@link String } containing the date tomorrow in a
     * particular format.
     */
    public static String getNextDate(final String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * Return current week day.
     * @return A {@link String } containing today's date.
     */
    public static String getCurrentDayOfWeek() {
        debug("Return current week day");
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    /**
     * Return next week day.
     * @return A {@link String } containing tomorrow's date.
     */
    public static String getNextDayOfWeek() {
        debug("Return next week day");
        LocalDate today = LocalDate.now().plusDays(1);
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

}
