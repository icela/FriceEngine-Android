@file:JvmName("ColorUtils")
@file:JvmMultifileClass

/**
 * @author ice1000
 * @since 3.1.4
 */
package org.frice.utils

import android.graphics.Bitmap
import org.frice.platform.adapter.DroidImage

private inline fun Bitmap.fy(fy: Bitmap.(Int, Int) -> Int): DroidImage {
	val jvmImage = DroidImage(width, height)
	repeat(width - 1) { x: Int ->
		repeat(height - 1) { y: Int -> jvmImage[x, y] = fy(x, y) }
	}
	return jvmImage
}

fun Bitmap.greenify() = fy { x, y -> getPixel(x, y).greenify() }
fun Bitmap.redify() = fy { x, y -> getPixel(x, y).redify() }
fun Bitmap.bluify() = fy { x, y -> getPixel(x, y).bluify() }
