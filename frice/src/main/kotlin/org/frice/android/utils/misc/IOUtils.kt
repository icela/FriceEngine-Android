package org.frice.android.utils.misc

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import org.frice.android.utils.data.readString
import org.frice.android.utils.data.save
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * Created by ice1000 on 2016/10/8.
 *
 * @author ice1000
 */
fun Context.openWeb(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

/**
 * if this value is null,
 * it means I have 2 load data from Sp.
 */
val Context.haveNetwork: Boolean
	get() = connection != null && connection!!.isConnected

private val Context.connection: NetworkInfo?
	get() = (getSystemService(Context.CONNECTIVITY_SERVICE)
			as ConnectivityManager).activeNetworkInfo


const val DEFAULT_VALUE = "DEFAULT_VALUE"

/**
 * this will cache the data into SharedPreference
 * next time when the network is invalid, it will return the data
 * stored in the SharedPreference.
 *
 * this method extended String.
 */
fun Context.webResource(
		url: String,
		submit: (String) -> Unit,
		default: String = DEFAULT_VALUE) {
	var ret = ""
	async() {
		ret = readString(default)
		uiThread { submit(ret) }
	}
	async() {
		if (ret != DEFAULT_VALUE && !haveNetwork) {
			uiThread { submit(ret) }
		} else {
			ret = URL(url).readText(Charsets.UTF_8)
			uiThread { submit(ret) }
			save(url, ret)
		}
	}
}


