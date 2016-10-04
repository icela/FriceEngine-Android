package org.frice.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.frice.game.obj.AbstractObject
import org.frice.game.obj.FObject
import org.frice.game.obj.PhysicalObject
import org.frice.game.obj.button.FText
import org.frice.game.obj.sub.ShapeObject
import org.frice.game.resource.FResource
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.utils.graphics.shape.FOval
import org.frice.game.utils.graphics.shape.FRectangle
import org.frice.game.utils.time.FTimeListener
import org.frice.game.utils.time.FTimer
import java.util.*
import kotlin.concurrent.thread

open class Game : AppCompatActivity() {

	protected @JvmField val objects = LinkedList<AbstractObject>()
	protected @JvmField val objectDeleteBuffer = ArrayList<AbstractObject>()
	protected @JvmField val objectAddBuffer = ArrayList<AbstractObject>()

	protected @JvmField val timeListeners = LinkedList<FTimeListener>()
	protected @JvmField val timeListenerDeleteBuffer = ArrayList<FTimeListener>()
	protected @JvmField val timeListenerAddBuffer = ArrayList<FTimeListener>()

	protected @JvmField val texts = LinkedList<FText>()
	protected @JvmField val textDeleteBuffer = ArrayList<FText>()
	protected @JvmField val textAddBuffer = ArrayList<FText>()

	/**
	 * if paused, main window will not call `onRefresh()`.
	 */
	var paused = false

	/**
	 * not implemented yet.
	 * currently it's same as paused.
	 */
	var stopped = false

	/**
	 * background resource (don't setBackground, please use `setBack()` instead.)
	 */
	var back: FResource = ColorResource.BLACK
	var debug = true

	/**
	 * a general purpose instance for generating random numbers
	 */
	val random = Random()

	/**
	 * if true, the engine will collect all objects which are invisible from game window.
	 */
	var autoGC = true

	/**
	 * if true, there will be a fps calculating on the bottom-left side of window.
	 */
	var showFPS = true

	var loseFocus = false
		protected set

	var loseFocusChangeColor = true

	private val refresh = FTimer(30)

	lateinit var canvas: FriceCanvas

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		canvas = FriceCanvas(this)
		onInit()
		thread {

		}
	}

	protected open fun onInit() = Unit
	protected open fun onRefresh() = Unit
	protected open fun customDraw(canvas: Canvas) = Unit


	/**
	 * do the delete and add work, to prevent Exceptions
	 */
	private fun processBuffer() {
		objects.addAll(objectAddBuffer)
		objects.removeAll(objectDeleteBuffer)
		objectDeleteBuffer.clear()
		objectAddBuffer.clear()

		timeListeners.addAll(timeListenerAddBuffer)
		timeListeners.removeAll(timeListenerDeleteBuffer)
		timeListenerDeleteBuffer.clear()
		timeListenerAddBuffer.clear()

		texts.addAll(textAddBuffer)
		texts.removeAll(textDeleteBuffer)
		textDeleteBuffer.clear()
		textAddBuffer.clear()
	}

	inner class FriceCanvas(context: Context) : View(context) {

		val p = Paint().apply {
			isAntiAlias = true
			style = Paint.Style.FILL
		}

		override fun onDraw(canvas: Canvas?) {
			super.onDraw(canvas)
			processBuffer()

			objects.forEach { o ->
				if (o is FObject) {
					o.runAnims()
					o.checkCollision()
				}
			}

			canvas?.let {
				canvas.save()
				objects.forEach { o ->
					canvas.restore()
					if (o is PhysicalObject) canvas.rotate(o.rotate.toFloat(),
							(o.x + o.width / 2).toFloat(), (o.y + o.height / 2).toFloat())
					else canvas.rotate(o.rotate.toFloat(), o.x.toFloat(), o.y.toFloat())
					when (o) {
						is FObject.ImageOwner ->
							canvas.drawBitmap(o.image, o.x.toFloat(), o.y.toFloat(), p)
						is ShapeObject -> {
							p.color = o.getResource().color
							when (o.collideBox) {
								is FRectangle -> canvas.drawRect(
										o.x.toFloat(),
										o.y.toFloat(),
										o.width.toFloat(),
										o.height.toFloat(),
										p
								)
//								is FOval -> canvas.drawOval(
//										o.x.toFloat(),
//										o.y.toFloat(),
//										o.width.toFloat(),
//										o.height.toFloat(),
//										p
//								)
							}
						}
					}
				}

				customDraw(canvas)
			}
		}
	}
}
