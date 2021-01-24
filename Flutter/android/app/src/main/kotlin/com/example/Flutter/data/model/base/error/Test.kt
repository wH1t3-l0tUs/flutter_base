package com.vmodev.better.data.model.base.error

import android.os.Handler

class Test {
    private var t: Int = 0

    init {
        Handler().postDelayed({ this@Test.t = 0 }, 300)
    }
}
