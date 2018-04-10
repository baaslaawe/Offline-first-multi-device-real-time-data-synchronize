package io.moka.syncdemo.util


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateUtil {

    private val dateFormat_yyyyMMdd = "yyyyMMdd"
    private val dateFormat_yyyyMM = "yyyyMM"

    private val dateFormat_getDiffDayCount = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

    /**
     * Today & Current Date / Time / Hour / Month / DayOfWeek .. etc
     */

    val todayDate: Date
        get() = Calendar.getInstance().time

    // return 1 ~ 7 ( 일요일 월 화 수 목 금 토요일 )
    val todayWeekOfDay: Int
        get() = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    // return 1 ~ 7 ( 일요일 월 화 수 목 금 토요일 )
    val tomorrowWeekOfDay: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(GregorianCalendar.DAY_OF_YEAR, 1)
            return calendar.get(Calendar.DAY_OF_WEEK)
        }

    val todayInt_yyyyMMdd: Int
        get() = formatDateToInt_yyyyMMdd(todayDate)

    val todayString_yyyyMMdd: String
        get() {
            val date = Date()
            val dateFormat = SimpleDateFormat(dateFormat_yyyyMMdd, Locale.getDefault())
            return dateFormat.format(date)
        }

    val currentTimeString: String
        get() {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            return String.format("%02d:%02d", hour, minute)
        }

    // 24시간제
    val currentHour: Int
        get() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    fun getTodayString_format(format: String): String {
        val date = Date()
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getTomorrowString_format(format: String): String {
        val calendar = Calendar.getInstance()
        calendar.add(GregorianCalendar.DAY_OF_YEAR, 1)
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    /**
     * Get Month // Jan: 0 ~ Dec: 11 ( +1 해서 return )
     */

    val currentMonth_yyyyMM: Int
        get() {
            val dateFormat = SimpleDateFormat(dateFormat_yyyyMM, Locale.getDefault())
            return Integer.parseInt(dateFormat.format(todayDate))
        }

    fun getMonthFromDate_yyyyMM(date: Date): Int {
        val dateFormat = SimpleDateFormat(dateFormat_yyyyMM, Locale.getDefault())
        return Integer.parseInt(dateFormat.format(date))
    }

    fun getMonthDiff(date: Date, diff: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, diff)
        return calendar.time
    }

    /**  */

    /**
     * Format
     */

    fun formatDateToString(date: Date, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatTimestampToString(timestamp: Long, format: String): String {
        return formatDateToString(timestampToDate(timestamp), format)
    }

    fun formatDateToInt_yyyyMMdd(date: Date): Int {
        val dateFormat = SimpleDateFormat(dateFormat_yyyyMMdd, Locale.getDefault())
        return Integer.parseInt(dateFormat.format(date))
    }

    fun formatDateToInt_yyyyMM(date: Date): Int {
        val dateFormat = SimpleDateFormat(dateFormat_yyyyMM, Locale.getDefault())
        return Integer.parseInt(dateFormat.format(date))
    }

    fun formatStringToString(zTime: String, foramt: String): String {
        val dateFormat_to = SimpleDateFormat(foramt, Locale.getDefault())
        val dateFormat_from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        try {
            return dateFormat_to.format(dateFormat_from.parse(zTime))
        } catch (e: ParseException) {
            return ""
        }
    }

    fun formatIntDateToString_yyyyMMdd(date_yyyyMMdd: Int): String {
        val dateFormat = SimpleDateFormat("MM월 dd일", Locale.getDefault())
        return dateFormat.format(parseDate(date_yyyyMMdd))
    }

    fun formatIntDateToString_format(date_yyyyMMdd: Int, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(parseDate(date_yyyyMMdd))
    }

    /**  */

    /**
     * Parse Date ( should return Date )
     */

    fun parseDate(dateString: String, foramt: String): Date {
        try {
            val dateFormat = SimpleDateFormat(foramt, Locale.getDefault())
            return dateFormat.parse(dateString)
        } catch (parseException: ParseException) {
            parseException.printStackTrace()
            return Date()
        }
    }

    fun parseDate(date_yyyyMMdd: String): Date {
        try {
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            return dateFormat.parse(date_yyyyMMdd)
        } catch (parseException: ParseException) {
            return Date()
        }
    }

    fun parseDate(dateTime_yyyyMMdd: Int): Date {
        return parseDate(dateTime_yyyyMMdd.toString())
    }

    fun parseDate(dateTime_yyyyMMdd: Long): Date {
        return parseDate(dateTime_yyyyMMdd.toString())
    }

    fun parseTodayDate(hour: Int, minute: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        return cal.time
    }

    fun parseTomorrowDate(hour: Int, minute: Int): Date {
        val cal = Calendar.getInstance()
        cal.add(GregorianCalendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        return cal.time
    }

    /**
     */

    private fun getDiffTimeStringFromMinutes(diffMinutes: Int): String {
        val diffHours = diffMinutes / 60

        if (48 < diffHours) {

            if (30 < diffMinutes / 60 / 24)
                return "${diffMinutes / 60 / 24 / 30} 달전"
            else
                return "${diffMinutes / 60 / 24} 일전"
        }
        else if (23 < diffHours && 48 > diffHours)
            return "하루전"
        else if (1 <= diffHours)
            return diffHours.toString() + "시간 전"
        else if (1 < diffMinutes)
            return diffMinutes.toString() + "분 전"
        else
            return "방금 전"
    }

    fun getDiffTimeString(createdAt: Long): String {
        val time = timestampToDate(createdAt)
        val diffMinutes = getDiffTimeMinutes(time, Date())

        return getDiffTimeStringFromMinutes(diffMinutes)
    }

    /**  */

    /**
     * plus Date
     */

    fun plusIntDate(intDate: Int, plusDay: Int): Int {
        val cal = Calendar.getInstance()
        cal.time = parseDate(intDate)
        cal.add(Calendar.DAY_OF_YEAR, plusDay)
        return formatDateToInt_yyyyMMdd(cal.time)
    }

    /**
     * about TimeStamp
     */

    val timestampInSecond: Long
        get() = System.currentTimeMillis() / 1000

    val timestampInMillis: Long
        get() = System.currentTimeMillis()

    private val calendar_timestampToCalendar = Calendar.getInstance()

    fun timestampToCalendar(timestamp: Long): Calendar {
        synchronized(calendar_timestampToCalendar) {

            calendar_timestampToCalendar.timeInMillis = timestamp * 1000
            return calendar_timestampToCalendar
        }
    }

    private val calendar_getTimestampFromDate = Calendar.getInstance()

    fun getTimestampFromDate(date: Date): Long {
        synchronized(calendar_getTimestampFromDate) {

            calendar_getTimestampFromDate.time = date
            return calendar_getTimestampFromDate.timeInMillis
        }
    }

    fun timestampToDate(timestamp: Long): Date {
        return timestampToCalendar(timestamp).time
    }

    /**  */

    /**
     * intDate , diff intDate
     */

    private val calendar_getDateFrom = Calendar.getInstance()

    fun getDateFromDate(date: Date, diff: Int): Date {
        synchronized(calendar_getDateFrom) {
            calendar_getDateFrom.time = date
            calendar_getDateFrom.add(GregorianCalendar.DAY_OF_YEAR, diff)

            return calendar_getDateFrom.time
        }
    }

    fun getDateFromIntDate(dateInt: Int, diff: Int): Date {
        return getDateFromDate(parseDate(dateInt), diff)
    }

    fun getIntDateFromIntDate(dateInt: Int, diff: Int): Int {
        return formatDateToInt_yyyyMMdd(getDateFromIntDate(dateInt, diff))
    }

    private val calendar_getDiffDateFromToday = Calendar.getInstance()

    fun getDiffDateFromToday(diff: Int): Date {
        val todayDate = todayDate

        synchronized(calendar_getDiffDateFromToday) {
            calendar_getDiffDateFromToday.time = todayDate
            calendar_getDiffDateFromToday.add(GregorianCalendar.DAY_OF_YEAR, diff)

            return calendar_getDiffDateFromToday.time
        }
    }

    fun getIntDiffDateFromToday(diff: Int): Int {
        return formatDateToInt_yyyyMMdd(getDiffDateFromToday(diff))
    }

    fun getDiffMinuteEachTimestamp(preTimestamp: Long, timestamp: Long): Double {
        return Math.floor(((timestamp - preTimestamp) / 60).toDouble())
    }

    fun getDiffDayCount(fromDate: String, toDate: String): Int {
        try {
            synchronized(dateFormat_getDiffDayCount) {
                return getDiffDayCount(dateFormat_getDiffDayCount.parse(fromDate), dateFormat_getDiffDayCount.parse(toDate))
            }
        } catch (parseException: ParseException) {
            return -1
        }

    }

    fun getDiffDayCount(fromDate: Int, toDate: Int): Int {
        return getDiffDayCount(fromDate.toString(), toDate.toString())
    }

    fun getDiffDayCount(fromDate: Date, toDate: Date): Int {
        val fromDateInt = formatDateToInt_yyyyMMdd(fromDate)
        val toDateInt = formatDateToInt_yyyyMMdd(toDate)

        val fromDateNew = parseDate(fromDateInt)
        val toDateNew = parseDate(toDateInt)

        return ((toDateNew.time - fromDateNew.time) / (24 * 60 * 60 * 1000)).toInt()
    }

    fun getDiffTimeMinutes(fromDate: Date, toDate: Date): Int {
        return ((toDate.time - fromDate.time) / (60 * 1000)).toInt()
    }

    /**
     * About Calendar
     */

    private val calendar_getDayIndexFrom = Calendar.getInstance()
    private var minCalendar: Calendar = Calendar.getInstance()

    init {
        minCalendar.set(Calendar.YEAR, 1978)
        minCalendar.set(Calendar.MONTH, 0)
        minCalendar.set(Calendar.DAY_OF_MONTH, 1)
        minCalendar.set(Calendar.HOUR_OF_DAY, 0)
        minCalendar.set(Calendar.MINUTE, 0)
        minCalendar.set(Calendar.SECOND, 0)
        minCalendar.set(Calendar.MILLISECOND, 0)
    }

    /* day index */

    fun getCalendarFromDayIndex(dayIndex: Int): Calendar {
        val calendar = minCalendar.clone() as Calendar
        calendar.add(Calendar.DAY_OF_YEAR, dayIndex)
        return calendar
    }

    fun getDayIndexFrom(date: Date): Int {
        calendar_getDayIndexFrom.time = date
        return getDayIndexFrom(calendar_getDayIndexFrom)
    }

    fun getDayIndexFrom(calendar: Calendar): Int {
        return getDiffDayCount(minCalendar, calendar)
    }

    /* week index */

    private val calendar_getWeekIndexFrom = Calendar.getInstance()

    fun getWeekIndexFrom(date: Date): Int {
        calendar_getWeekIndexFrom.time = date
        return getWeekIndexFrom(calendar_getWeekIndexFrom)
    }

    fun getWeekIndexFrom(calendar: Calendar): Int {
        return getDiffDayCount(minCalendar, calendar) / 7
    }

    fun getDateFromWeekIndex(weekIndex: Int): Date {
        return getCalendarFromWeekIndex(weekIndex).time
    }

    fun getCalendarFromWeekIndex(weekIndex: Int): Calendar {
        val calendar = minCalendar.clone() as Calendar
        calendar.add(Calendar.DAY_OF_YEAR, weekIndex * 7)
        return calendar
    }

    fun getDiffDayCount(fromDate: Calendar, toDate: Calendar): Int {
        val fromDateNew = fromDate.clone() as Calendar
        fromDateNew.set(Calendar.HOUR_OF_DAY, 0)
        fromDateNew.set(Calendar.MINUTE, 0)
        fromDateNew.set(Calendar.SECOND, 0)
        fromDateNew.set(Calendar.MILLISECOND, 0)

        val toDateNew = toDate.clone() as Calendar
        toDateNew.set(Calendar.HOUR_OF_DAY, 0)
        toDateNew.set(Calendar.MINUTE, 0)
        toDateNew.set(Calendar.SECOND, 0)
        toDateNew.set(Calendar.MILLISECOND, 0)

        val fromTimeMillis = fromDateNew.timeInMillis
        val toTimeMillis = toDateNew.timeInMillis

        return ((toTimeMillis - fromTimeMillis) / (24 * 60 * 60 * 1000)).toInt()
    }

    /*

    From Server
     */

    fun getDateFromServerDate(serverDate: String): Date {
        return parseDate(serverDate, "yyyyMMddHHmm")
    }

    fun getIntDateFromServerDate(serverDate: String): Int {
        return formatDateToInt_yyyyMMdd(parseDate(serverDate, "yyyyMMddHHmm"))
    }

    fun formatFromServerDate(serverDate: String, format: String): String {
        return formatDateToString(parseDate(serverDate, "yyyyMMddHHmm"), format)
    }

    // return 1 ~ 7 ( 일요일 월 화 수 목 금 토요일 )
    fun getWeekDayIndex(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

}
