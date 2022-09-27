package com.example.oving3

import java.io.Serializable
import java.time.LocalDate

data class Friend(
    var name:String,
    var date: LocalDate
) : Serializable