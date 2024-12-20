package com.mobdeve.s20.dimagiba.rafael.mco2

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomDate {
    private var day_in_month: Int
    private var month: Int
    private var year: Int

    // Creates a CustomDate for today
    constructor() {
        val c = Calendar.getInstance()
        year = c[Calendar.YEAR]
        day_in_month = c[Calendar.DAY_OF_MONTH]
        month = c[Calendar.MONTH]
    }

    // When supplied the day, month, and year, create the date accordingly.
    // Month here is expected to be 0-11
    constructor(year: Int, month: Int, day_in_month: Int) {
        this.year = year
        this.day_in_month = day_in_month
        this.month = month
    }

    // Example output
    // if CustomDate has 2023, 0, 1: Jan 1, 2023
    // if CustomDate has 2023, 11, 1: Dec 1, 2023
    fun toStringFull(): String {
        return monthString[month] + " " + day_in_month + ", " + year
    }

    // Example output
    // if CustomDate has 2023, 0, 1: Jan 1
    // if CustomDate has 2023, 11, 1: Dec 1
    fun toStringNoYear(): String {
        return monthString[month] + " " + day_in_month
    }

    fun toISO8601String(): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day_in_month)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    companion object {
        private val monthString = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
    }
}