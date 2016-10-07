package org.frice.android.utils.message.log

import android.util.Log

/**
 * Created by ice1000 on 2016/8/13.
 * @author ice1000
 * @since v0.1
 */
object FLog {
	const val LOG_PREFIX = "Frice log["

	@JvmStatic fun v(e: Any?) = verbose(e)
	@JvmStatic fun d(e: Any?) = debug(e)
	@JvmStatic fun i(e: Any?) = info(e)
	@JvmStatic fun w(e: Any?) = warning(e)
	@JvmStatic fun e(e: Any?) = error(e)

	@JvmStatic fun verbose(e: Any?) = Log.v("$LOG_PREFIX verbose]\n", e?.toString())
	@JvmStatic fun debug(e: Any?) = Log.d("$LOG_PREFIX debug]\n", e?.toString())
	@JvmStatic fun info(e: Any?) = Log.i("$LOG_PREFIX info]\n", e?.toString())
	@JvmStatic fun warning(e: Any?) = Log.w("$LOG_PREFIX warning]\n", e?.toString())
	@JvmStatic fun error(e: Any?) = Log.e("!!!$LOG_PREFIX error]\n", e?.toString())
}
