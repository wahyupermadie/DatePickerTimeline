package com.vivekkaushik.datepicker.helper

import java.util.HashMap

interface Utils{
    companion object{
        fun getMapperDate(dateTag : String) : String {
            val date = HashMap<String, String>().apply {
                this["SUN"] = "Minggu"
                this["MON"] = "Senin"
                this["TUE"] = "Selasa"
                this["WED"] = "Rabu"
                this["THU"] = "Kamis"
                this["FRI"] = "Jum'at"
                this["SAT"] = "Sabtu"
            }
            return date[dateTag]!!
        }
    }
}