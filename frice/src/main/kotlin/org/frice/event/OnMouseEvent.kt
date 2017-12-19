@file:JvmName("Events")

package org.frice.event

const val MOUSE_CLICKED = 0x00
const val MOUSE_RELEASED = 0x01
const val MOUSE_ENTERED = 0x02
const val MOUSE_EXITED = 0x03
const val MOUSE_PRESSED = 0x04

/**
 * Created by ice1000 on 2016/8/13.
 * @author ice1000
 * @since v0.1
 */
data class OnMouseEvent(
		val x: Double,
		val y: Double,
		val type: Int)
