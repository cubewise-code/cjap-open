package OpenCJAPAuth.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    public static String getCnDayWeekday(Date date) {
        String formatDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        return formatDate;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            return false;
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return false;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameInstant(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return false;
        return date1.getTime() == date2.getTime();
    }

    public static boolean isSameInstant(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null)
            return false;
        return cal1.getTime().getTime() == cal2.getTime().getTime();
    }

    public static Date addYears(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if (date == null)
            throw new IllegalArgumentException("Date cannot be empty");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    public static String today() {
        return today("yyyy-MM-dd");
    }

    public static String now() {
        return today("yyyy-MM-dd HH:mm:ss SSS");
    }

    public static String nowTime() {
        return today("yyyyMMddHHmmss");
    }

    public static String today(String pattern) {
        if (pattern == null)
            throw new IllegalArgumentException("Date format cannot be empty");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dt = sdf.format(new Date());
        return dt;
    }

    public static Date parseDate(String src) {
        return parse(src, "yyyy-MM-dd");
    }

    public static Date parseDatetime(String src) {
        return parse(src, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parse(String src, String pattern) {
        if (pattern == null)
            throw new IllegalArgumentException("Date format cannot be empty");
        if (src == null || "".equals(src))
            return null;
        try {
            return new SimpleDateFormat(pattern).parse(src);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Date format error,src=" + src + ",pattern=" + pattern);
        }
    }

    public static String formatDate(Date src) {
        return format(src, "yyyy-MM-dd");
    }

    public static String formatDatetime(Date src) {
        return format(src, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date src, String pattern) {
        if (pattern == null)
            throw new IllegalArgumentException("Date format cannot be empty");
        if (src == null)
            return null;
        return new SimpleDateFormat(pattern).format(src);
    }

    public static int getLastDayOfMonth(int y, int m) {
        boolean IsLeapYear = (y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0);
        int days = 0;
        switch (m) {
            case 2:
                if (IsLeapYear) {
                    days = 29;
                } else
                    days = 28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            default:
                days = 31;
                break;
        }
        return days;
    }

    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dt = sdf.format(new Date());
        return dt;
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dt = sdf.format(new Date());
        return dt;
    }

    public static String getYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dt = sdf.format(new Date());
        return dt.substring(0, 4);
    }

    public static String getDateTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dt = sdf.format(new Date());
        return dt;
    }

    public static String shortFmt(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }

    public static String dateTimeFmt(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    public static String dateFmt(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String dateFmt(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String shortFmt(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parse(String param) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(param);
        } catch (ParseException ex) {
        }
        return date;
    }

    public static Date parseShort(String param) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(param);
        } catch (ParseException ex) {
        }
        return date;
    }

    public static int getDays(int year, int month) {
        int[] numberMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int numberOfMonth = numberMonth[month - 1];
        if (month == 2 && ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)))) {
            numberOfMonth++;
        }
        return numberOfMonth;
    }

    public static int[] getLastDay(int year, int month, int day) {
        if (year < 1000 || year > 3000 || month <= 0 || day <= 0 || day > 31 || month > 12)
            return null;
        int[] date = new int[3];
        if (day == 1) {
            if (month == 1) {
                date[0] = year - 1;
                date[1] = 12;
                date[2] = getDays(date[0], date[1]);
            } else {
                date[0] = year;
                date[1] = month - 1;
                date[2] = getDays(date[0], date[1]);
            }
        } else {
            date[0] = year;
            date[1] = month;
            date[2] = day - 1;
        }
        return date;
    }

    public static int[] getNextDay(int year, int month, int day) {
        if (year < 1000 || year > 3000 || month <= 0 || day <= 0 || day > 31 || month > 12)
            return null;
        int[] date = new int[3];
        if (isLastDayOftheMonth(year, month, day)) { // �?后一�?
            if (month == 12) {
                date[0] = year + 1;
                date[1] = 1;
                date[2] = 1;
            } else {
                date[0] = year;
                date[1] = month + 1;
                date[2] = 1;
            }
        } else {
            date[0] = year;
            date[1] = month;
            date[2] = day + 1;
        }
        return date;
    }

    public static int getYear(String date) throws NumberFormatException {
        String m = date.substring(0, 4);
        return Integer.parseInt(m);
    }

    public static int getMonth(String date) throws NumberFormatException {
        String m = date.substring(date.indexOf("-") + 1, date.indexOf("-") + 3);
        return Integer.parseInt(m);
    }

    public static int getDay(String date) throws NumberFormatException {
        String m = date.substring(date.lastIndexOf("-") + 1);
        return Integer.parseInt(m);
    }

    public static int getQuarter(String date) throws NumberFormatException {
        String q = date.substring(date.lastIndexOf("#") + 1);
        return Integer.parseInt(q);
    }

    public static int getPeriod(String date) throws NumberFormatException {
        String p = date.substring(date.lastIndexOf("$") + 1);
        return Integer.parseInt(p);
    }

    public static Date monthAdd(Date src, int add) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(src);
        ca.set(Calendar.MONTH, ca.get(Calendar.MONTH) + add);
        return ca.getTime();
    }

    public static Date dayAdd(Date src, int add) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(src);
        ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) + add);
        return ca.getTime();
    }

    public static int[] getLastMonth(int year, int month) {
        if (year < 1000 || year > 3000 || month <= 0 || month > 12)
            return null;
        final int YEAR = 0;
        final int MONTH = 1;
        int[] date = new int[2];
        if (month == 1) {
            date[MONTH] = 12;
            date[YEAR] = year - 1;
        } else {
            date[MONTH] = month - 1;
            date[YEAR] = year;
        }
        return date;
    }

    public static String formatHourMinite(float hm) {
        String s = hm + "";
        s = s.substring(0, s.indexOf(".")) + ":" + s.substring(s.indexOf(".") + 1, s.length());
        return s;
    }

    public static boolean isLastDayOftheMonth(int y, int m, int d) {
        return (getLastDayOfMonth(y, m) == d);
    }

    public static boolean isDayIn(Date min, Date max, Date dt) {
        return max.getTime() - dt.getTime() >= 0 && min.getTime() - dt.getTime() <= 0;
    }

    public static java.sql.Date utilDateToSqlDate(Date dDate) {
        if (dDate != null) {
            java.sql.Date sqlDate = new java.sql.Date(dDate.getTime());
            return sqlDate;
        }
        return null;
    }

    public static String fmtDate(Date src, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(src);
    }

    public static String fmtDate(Date src) {
        return fmtDate(src, "yyyy-MM-dd");
    }

    public static String fmtDateTime(Date src) {
        return fmtDate(src, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(String src, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(src);
        } catch (ParseException e) {
            throw new RuntimeException("解析日期出错：src=" + src + ",pattern=" + pattern);
        }
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date getCurrentDay() {
        return parseDate(fmtDate(new Date()), "yyyy-MM-dd");
    }

    public static Date getCurrentDayTime() {
        return parseDate(fmtDateTime(new Date()), "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatMonthForQuartz(String TimeUnit) {
        String str = "";
        int a = 12 / Integer.parseInt(TimeUnit);
        for (int i = 0; i < a; i++) {
            if (i != a - 1) {
                str += 1 + i * Integer.parseInt(TimeUnit) + ",";
            } else {
                str += 1 + i * Integer.parseInt(TimeUnit);
            }
        }
        return str;
    }

    public static String getScheduleTime(String timeType, String TimeUnit) {
        String str = null;
        if ("h".equals(timeType)) {
            str = "0 0 0/" + TimeUnit + " * * ?";
        } else if ("d".equals(timeType)) {
            int day = Integer.parseInt(TimeUnit.trim()) * 24;
            String dayStr = Integer.toString(day);
            str = "0 0 0/" + dayStr + " * * ?";
        } else if ("m".equals(timeType)) {
            String unit = formatMonthForQuartz(TimeUnit);
            str = "0 0 0 0 " + unit + " ?";
        }
        return str;
    }

    public static String getScheduleTime(String timeType, String TimeUnit, Date date) {
        String str = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int second = calendar.get(Calendar.SECOND);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if ("h".equals(timeType)) {
            str = second + " " + minute + " 0/" + TimeUnit + " * * ?";
        } else if ("d".equals(timeType)) {
            int day = Integer.parseInt(TimeUnit.trim()) * 24;
            String dayStr = Integer.toString(day);
            str = second + " " + minute + " 0/" + dayStr + " * * ?";
        } else if ("m".equals(timeType)) {
            String unit = formatMonthForQuartz(TimeUnit);
            str = second + " " + minute + " " + hour + " " + dayOfMonth + " " + unit + " ?";
        }
        return str;
    }
}
