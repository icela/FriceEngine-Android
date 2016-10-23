package org.frice.android.utils.graphics.utils

import android.graphics.Color

/**
 * Created by ice1000 on 16-8-6.
 * Reference: http://blog.csdn.net/lzs109/article/details/7316507
 * @author ice1000
 */
val asciiList = listOf('#', '0', 'X', 'x', '+', '=', '-', ';', ',', '.', ' ')

fun Int.toAscii() = asciiList[gray() / (256 / asciiList.size + 1)]

/**
 * Android API
 */
fun Int.gray(): Int {
	val c = (Color.red(this) + Color.blue(this) + Color.green(this)) / 3
	return Color.argb(0xff, c, c, c)
}

/**
 * Android API
 * making a color darker
 */
fun Int.darker() = Color.argb(
		Color.alpha(this),
		Color.red(this) * 2 / 3,
		Color.green(this) * 2 / 3,
		Color.blue(this) * 2 / 3
)

fun darkerRGB(int: Int) = int.darker()


