package org.frice.utils.message

import android.util.Log

/**
 * Created by ice1000 on 2016/8/13.
 * @author ice1000
 * @since v0.1
 */
object FLog {
	const val LOG_PREFIX = "Frice log["

	@JvmStatic
	var level = 50

	const val VERBOSE = 40
	const val DEBUG = 30
	const val INFO = 20
	const val WARN = 10
	const val ERROR = 0

	@JvmStatic
	fun v(e: Any?) = verbose(e)

	@JvmStatic
	fun d(e: Any?) = debug(e)

	@JvmStatic
	fun i(e: Any?) = info(e)

	@JvmStatic
	fun w(e: Any?) = warning(e)

	@JvmStatic
	fun e(e: Any?) = error(e)

	@JvmStatic
	fun verbose(e: Any?) {
		if (level >= VERBOSE) Log.v("$LOG_PREFIX verbose]\n", e?.toString())
	}

	@JvmStatic
	fun debug(e: Any?) {
		if (level >= DEBUG) Log.d("$LOG_PREFIX debug]\n", e?.toString())
	}

	@JvmStatic
	fun info(e: Any?) {
		if (level >= INFO) Log.i("$LOG_PREFIX info]\n", e?.toString())
	}

	@JvmStatic
	fun warning(e: Any?) {
		if (level >= WARN) Log.w("$LOG_PREFIX warning]\n", e?.toString())
	}

	@JvmStatic
	fun error(e: Any?) {
		if (level >= ERROR) Log.e("!!!$LOG_PREFIX error]\n", e?.toString())
	}
}