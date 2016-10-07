package org.frice.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import org.frice.android.obj.AbstractObject
import org.frice.android.obj.FObject
import org.frice.android.obj.PhysicalObject
import org.frice.android.obj.button.FButton
import org.frice.android.obj.button.FText
import org.frice.android.obj.button.SimpleButton
import org.frice.android.resource.graphics.ColorResource
import org.frice.android.utils.graphics.shape.FOval
import org.frice.android.utils.graphics.shape.FRectangle
import org.frice.android.utils.message.error.FatalError
import org.frice.android.utils.message.log.FLog
import org.frice.android.obj.effects.LineEffect
import org.frice.game.obj.sub.ShapeObject
import org.frice.game.resource.FResource
import org.frice.game.utils.misc.forceRun
import org.frice.game.utils.misc.loopIf
import org.frice.game.utils.time.FTimeListener
import org.frice.game.utils.time.FTimer
import java.util.*
import kotlin.concurrent.thread


/**
 * First game class(not for you)
 *
 * Standard library, mainly for GUI.
 * some other library is in @see
 * The base game class.
 * this class do rendering, and something which are invisible to
 * game developer.
 *
 * Created by ice1000 on 2016/10/6.
 * @author ice1000
 */
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

	private var fpsCounter = 0
	private var fpsDisplay = 0
	private lateinit var fpsTimer: FTimer

	private lateinit var canvas: FriceCanvas

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		canvas = FriceCanvas(this)
		fpsTimer = FTimer(1000)
		setContentView(canvas)
		onInit()
		FLog.v("Engine start!")
		thread {
			loopIf(!paused && !stopped && refresh.ended()) {
				forceRun {
					onRefresh()
					timeListeners.forEach { it.check() }
					canvas.invalidate()
					++fpsCounter
					if (fpsTimer.ended()) {
						fpsDisplay = fpsCounter
						fpsCounter = 0
					}
				}
			}
		}
	}

	protected open fun onInit() = Unit
	protected open fun onRefresh() = Unit
	protected open fun onClick() = Unit
	protected open fun onTouch(event: MotionEvent) = Unit
	protected open fun customDraw(canvas: Canvas) = Unit

	/**
	 * adds objects
	 *
	 * @param objs as a collection
	 */
	infix fun addObjects(objs: Collection<AbstractObject>) = addObjects(objs.toTypedArray())

	/**
	 * adds objects
	 *
	 * @param objs as an array
	 */
	infix fun addObjects(objs: Array<AbstractObject>) = objs.forEach { o -> addObject(o) }

	/**
	 * adds an object to game, to be shown on game window.
	 */
	infix fun addObject(obj: AbstractObject) {
		if (obj is FText) textAddBuffer.add(obj)
		else objectAddBuffer.add(obj)
	}

	/**
	 * clears all objects.
	 * this method is safe.
	 */
	fun clearObjects() {
		objectDeleteBuffer.addAll(objects)
		textDeleteBuffer.addAll(texts)
	}


	/**
	 * removes objects.
	 * this method is safe.
	 *
	 * @param objs will remove objects which is equal to them, as an array.
	 */
	infix fun removeObjects(objs: Array<AbstractObject>) = objs.forEach { o -> objectDeleteBuffer.add(o) }

	/**
	 * removes objects.
	 * this method is safe.
	 *
	 * @param objs will remove objects which is equal to them, as a collection.
	 */
	infix fun removeObjects(objs: Collection<AbstractObject>) = removeObjects(objs.toTypedArray())

	/**
	 * removes single object.
	 * this method is safe.
	 *
	 * @param obj will remove objects which is equal to it.
	 */
	infix fun removeObject(obj: AbstractObject) {
		if (obj is FText) textDeleteBuffer.add(obj)
		else objectDeleteBuffer.add(obj)
	}

	/**
	 * adds a auto-executed time listener
	 * you must add or it won't work.
	 */
	infix fun addTimeListener(listener: FTimeListener) = timeListenerAddBuffer.add(listener)

	/**
	 * adds an array of auto-executed time listeners
	 */
	infix fun addTimeListeners(listeners: Array<FTimeListener>) = listeners.forEach { l -> addTimeListener(l) }

	/**
	 * adds a collection of auto-executed time listeners
	 */
	infix fun addTimeListeners(listeners: Collection<FTimeListener>) = addTimeListeners(listeners.toTypedArray())

	/**
	 * removes all auto-executed time listeners
	 */
	fun clearTimeListeners() = timeListenerDeleteBuffer.addAll(timeListeners)

	/**
	 * removes auto-executed time listeners specified in the given array.
	 *
	 * @param listeners the array
	 */
	infix fun removeTimeListeners(listeners: Array<FTimeListener>) =
			listeners.forEach { l -> removeTimeListener(l) }

	/**
	 * auto-execute time listeners which are equal to the given collection.
	 *
	 * @param listeners the collection
	 */
	infix fun removeTimeListeners(listeners: Collection<FTimeListener>) = removeTimeListeners(listeners.toTypedArray())

	/**
	 * removes specified listener
	 *
	 * @param listener the listener
	 */
	infix fun removeTimeListener(listener: FTimeListener) = timeListenerDeleteBuffer.add(listener)

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

	/**
	 * must be used in Game
	 */
	inner class FriceCanvas(context: Context) : View(context) {

		init {
			if (context !is Game) throw FatalError()
			setOnClickListener {
				context.onClick()
			}
			setOnTouchListener { view, motionEvent ->
				context.onTouch(motionEvent)
				return@setOnTouchListener false
			}
		}

		val p = Paint().apply {
			isAntiAlias = true
			style = Paint.Style.FILL
		}

		private fun Paint.reset() {
			color = ColorResource.GRAY.color
		}

		override fun onDraw(canvas: Canvas?) {
			super.onDraw(canvas)
			processBuffer()
			canvas?.save()

			objects.forEach { o ->
				if (o is FObject) {
					o.runAnims()
					o.checkCollision()
				}
			}

			canvas?.let {
				objects.forEach { o ->

					canvas.restore()
					p.reset()

					if (autoGC && (o.x.toInt() < -width ||
							o.x.toInt() > width + width ||
							o.y.toInt() < -height ||
							o.y.toInt() > height + height)) {
						if (o is PhysicalObject) o.died = true
						removeObject(o)
					}

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
								is FOval -> canvas.drawOval(
										RectF(
												o.x.toFloat(),
												o.y.toFloat(),
												o.width.toFloat(),
												o.height.toFloat()
										),
										p
								)
							}
						}
						is LineEffect -> {
							p.color = o.colorResource.color
							canvas.drawLine(
									o.x.toFloat(),
									o.y.toFloat(),
									o.x2.toFloat(),
									o.y2.toFloat(),
									p
							)
						}
					}

					texts.forEach { b ->

						canvas.restore()
						p.reset()

						if (b is FButton) {
							p.color = b.getColor().color
							when (b) {
								is FObject.ImageOwner ->
									canvas.drawBitmap(b.image, b.x.toFloat(), b.y.toFloat(), p)
								is SimpleButton -> {
									p.color = b.getColor().color
									canvas.drawRoundRect(
											RectF(
													b.x.toFloat(),
													b.y.toFloat(),
													b.width.toFloat(),
													b.height.toFloat()
											),
											Math.min((b.width * 0.5).toFloat(), 10F),
											Math.min((b.height * 0.5).toFloat(), 10F), p)
									p.color = ColorResource.GRAY.color
									canvas.drawText(b.text, b.x.toFloat(), b.y.toFloat(), p)
								}
							}
						} else canvas.drawText(b.text, b.x.toFloat(), b.y.toFloat(), p)
					}
				}

				if (showFPS) canvas.drawText("fps: $fpsDisplay", 30F, height - 30F, p)

				customDraw(canvas)
			}
		}
	}
}
