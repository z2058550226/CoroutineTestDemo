package com.example.coroutinetestdemo

import java.util.concurrent.TimeUnit

fun sleep(second: Long) {
    TimeUnit.SECONDS.sleep(second)
}