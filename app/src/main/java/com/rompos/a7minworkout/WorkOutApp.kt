package com.rompos.a7minworkout

import android.app.Application

class WorkOutApp : Application() {

    val db by lazy {//lazy means it will be used when called
        HistoryDatabase.getInstance(this)
    }

}