package com.vivekkaushik.datepicker.helper

import android.util.Log
import org.joda.time.DateTime
import java.util.ArrayList
import java.util.Date
import java.util.HashMap

interface Utils{
    companion object{
        fun getMapperDate(dateTag : Int) : String {
            val date = HashMap<Int, String>().apply {
                this[1] = "Minggu"
                this[2] = "Senin"
                this[3] = "Selasa"
                this[4] = "Rabu"
                this[5] = "Kamis"
                this[6] = "Jum'at"
                this[7] = "Sabtu"
            }
            return date[dateTag]!!
        }

        fun generateDateRange(
            start: DateTime,
            end: DateTime
        ): List<DateTime> {
            val ret: MutableList<DateTime> =
                ArrayList()
            var tmp = start
            while (tmp.isBefore(end) || tmp == end) {
                ret.add(tmp)
                tmp = tmp.plusDays(1)
            }
            return ret
        }

        fun getDateRange(
            dateStart: String?,
            dateEnd: String?
        ): ArrayList<Date>? {
            val start = DateTime.parse(dateStart)
            val end = DateTime.parse(dateEnd)
            val between = generateDateRange(start, end)
            val dates = ArrayList<Date>()
            for (d in between) {
                dates.add(d.toDate())
            }
            return dates
        }
    }
}