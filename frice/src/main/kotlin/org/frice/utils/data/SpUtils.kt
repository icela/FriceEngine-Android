/**
 * Created by ice1000 on 2016/10/8.
 *
 * @author ice1000
 */
package org.frice.utils.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 * insert a value in2 SharedPreference
 * any types of value is accepted.
 *
 * Will be smart casted.
 */
fun Context.save(key: String, value: Any) {
	val editor = openPreference().edit()
	when (value) {
		is Int -> editor.putInt(key, value)
		is Float -> editor.putFloat(key, value)
		is Long -> editor.putLong(key, value)
		is Boolean -> editor.putBoolean(key, value)
		is String -> editor.putString(key, value)
		else -> throw Exception("not supported type")
	}
	editor.apply()
}

fun Context.readString(key: String, default: String = "") = openPreference().getString(key, default) ?: default
fun Context.readInt(key: String, default: Int = 0) = openPreference().getInt(key, default)
fun Context.readBoolean(key: String, default: Boolean = false) = openPreference().getBoolean(key, default)

/**
 * @return a SharedPreference
 */
private fun Context.openPreference(): SharedPreferences =
		getSharedPreferences("MainPreference", Activity.MODE_APPEND)
