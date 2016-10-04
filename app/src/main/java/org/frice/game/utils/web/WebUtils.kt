package org.frice.game.utils.web

import android.graphics.BitmapFactory
import java.net.URL

/**
 * Created by ice1000 on 2016/9/3.
 *
 * @author ice1000
 * @since v0.5
 */
object WebUtils {
	@JvmStatic fun readText(url: URL) = url.readText()
	@JvmStatic fun readText(url: String) = readText(URL(url))

	@JvmStatic fun readBytes(url: URL) = url.readBytes()
	@JvmStatic fun readBytes(url: String) = readBytes(URL(url))

	@JvmStatic fun readImage(url: String) = BitmapFactory.decodeFile(url)!!

	@JvmStatic fun readImages(url: String) = HTMLUtils.findTag(readText(url), "img")
	@JvmStatic fun readImages(url: URL) = HTMLUtils.findTag(readText(url), "img")
}
