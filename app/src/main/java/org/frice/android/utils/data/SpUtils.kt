package org.frice.android.utils.data

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity

/**
 * Created by ice1000 on 2016/10/8.
 *
 * @author ice1000
 */


/**
 * insert a value in2 SharedPreference
 * any types of value is accepted.
 *
 * Will be smart casted.
 */
fun Context.save(key: String, value: Any) {
	val editor = openPreference().edit()
	if (value is Int) editor.putInt(key, value)
	else if (value is Float) editor.putFloat(key, value)
	else if (value is Long) editor.putLong(key, value)
	else if (value is Boolean) editor.putBoolean(key, value)
	else if (value is String) editor.putString(key, value)
	else throw Exception("not supported type")
	editor.apply()
}

fun Context.readString(key: String, default: String = "") = openPreference().getString(key, default) ?: default
fun Context.readInt(key: String, default: Int = 0) = openPreference().getInt(key, default) ?: default
fun Context.readBoolean(key: String, default: Boolean = false) = openPreference().getBoolean(key, default) ?: default

/**
 * @return a SharedPreference
 */
private fun Context.openPreference(): SharedPreferences =
		getSharedPreferences("MainPreference", AppCompatActivity.MODE_ENABLE_WRITE_AHEAD_LOGGING)


