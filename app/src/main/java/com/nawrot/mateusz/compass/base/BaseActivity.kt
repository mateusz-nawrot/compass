package com.nawrot.mateusz.compass.base

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import kotlin.reflect.KClass


open class BaseActivity : AppCompatActivity() {

    inline fun <reified T : Any> start(activityClass: KClass<T>, flags: Int? = null, finishCurrentActivity: Boolean = false) {
        val intent = Intent(this, activityClass.java)

        flags?.let { intent.flags = flags }

        if (finishCurrentActivity) finish()

        startActivity(intent)
    }
}