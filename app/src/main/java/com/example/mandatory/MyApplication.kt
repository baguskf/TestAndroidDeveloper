package com.example.mandatory

import android.app.Application


class mandatory : Application() {
    var callback: MyService.ServiceCallback? = null
}
