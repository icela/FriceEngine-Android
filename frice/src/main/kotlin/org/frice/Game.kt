@file:Suppress("unused")

package org.frice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.*
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import org.frice.event.*
import org.frice.obj.button.FText
import org.frice.platform.FriceGame
import org.frice.platform.Layer
import org.frice.platform.adapter.DroidDrawer
import org.frice.platform.adapter.DroidImage
import org.frice.resource.graphics.ColorResource
import org.frice.utils.data.readString
import org.frice.utils.data.save
import org.frice.utils.loop
import org.frice.utils.message.FLog
import org.frice.utils.shape.FRectangle
import org.frice.utils.time.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL
import java.util.*


/**
 * The base game class using Swing as renderer.
 * This class does rendering jobs, and something which are
 * invisible to game developers.
 *
 * Feel free to override the constructor, but please remember to
 * invoke super().
 *
 * Created by ice1000 on 2016/8/15.
 * @author ice1000
 * @since v0.2.3
 */
open class Game @JvmOverloads constructor(layerCount: Int = 1) :
		Activity(), FriceGame<DroidDrawer>, Runnable {
	private var screenWidth: Int = -1
	private var screenHeight: Int = -1

	override fun getHeight() = screenHeight
	override fun getWidth() = screenWidth

	override val layers = Array(layerCount) { Layer() }
	override var paused = false
		set(value) {
			if (value) FClock.pause() else FClock.resume()
			field = value
		}

	override var stopped = false
		set(value) {
			if (value) FClock.pause() else FClock.resume()
			field = value
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		window.setFormat(PixelFormat.TRANSLUCENT)
		surfaceView = SurfaceView(this)
		frameLayout = FrameLayout(this)
		frameLayout.addView(surfaceView, ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT))
		setContentView(frameLayout)
		surfaceView.setOnClickListener {
			mouse(OnMouseEvent(it.x.toDouble(), it.y.toDouble(), MOUSE_CLICKED))
			onMouse(OnMouseEvent(it.x.toDouble(), it.y.toDouble(), MOUSE_CLICKED))
		}
		surfaceView.setOnTouchListener { _, it ->
			mouse(OnMouseEvent(it.x.toDouble(), it.y.toDouble(), MOUSE_PRESSED))
			onMouse(OnMouseEvent(it.x.toDouble(), it.y.toDouble(), MOUSE_PRESSED))
			return@setOnTouchListener false
		}
		screenWidth = resources.displayMetrics.widthPixels
		screenHeight = resources.displayMetrics.heightPixels
		onInit()
		FLog.v("height: $screenHeight, width: $screenWidth")
		Thread(this).start()
	}

	override fun run() {
		onLastInit()
		loop {
			try {
				onRefresh()
				// only update per "refreshTime"
				if (!paused && !stopped && refresh.ended() && surfaceView.holder.surface.isValid) {
					val canvas = surfaceView.holder.lockCanvas()
					val localDrawer = drawer ?: DroidDrawer(canvas).also { drawer = it }
					localDrawer.canvas = canvas
					clearScreen(localDrawer)
					drawEverything(localDrawer)
					// if (loseFocus and loseFocusChangeColor) repeat(localDrawer.canvas.width) { x: Int -> repeat(localDrawer.canvas.height) { y: Int -> localDrawer.canvas[x, y] = drawer.friceImage[x, y].darker() } }

					localDrawer.restore()
					localDrawer.init()
					localDrawer.color = ColorResource.DARK_GRAY
					if (showFPS) localDrawer.drawString("fps: ${fpsCounter.display}", 30.0, height - 30.0)

					fpsCounter.refresh()
					surfaceView.holder.unlockCanvasAndPost(canvas)
				}
			} catch (ignored: ConcurrentModificationException) {
			}
		}
	}

	override fun onDestroy() {
		onExit()
		super.onDestroy()
	}

	override fun onPause() {
		super.onPause()
		loseFocus = true
		FClock.pause()
		onLoseFocus()
	}

	override fun onResume() {
		super.onResume()
		loseFocus = false
		FClock.resume()
		onFocus()
	}

	override fun dialogConfirmYesNo(msg: String, title: String) =
			false // JOptionPane.showConfirmDialog(this, msg, title, YES_NO_OPTION) == YES_OPTION

	override fun dialogShow(msg: String, title: String) {}
	// JOptionPane.showMessageDialog(this, msg, title, OK_OPTION)

	override fun dialogInput(msg: String, title: String): String =
			"TODO" // JOptionPane.showInputDialog(this, msg, title)

	override var debug = true
	override var autoGC = true
	override var showFPS = true

	final override var loseFocus = false

	override var loseFocusChangeColor = true

	@get:JvmName(" refresh")
	internal val refresh = FTimer(4)
	override var millisToRefresh: Int
		get () = refresh.time
		set (value) {
			refresh.time = value
		}

	var drawer: DroidDrawer? = null
	lateinit var frameLayout: FrameLayout
	lateinit var surfaceView: SurfaceView

	val fpsCounter = FpsCounter()

	init {
		onInit()
	}

	open fun onExit() {
		// TODO()
	}

	override fun measureText(text: FText): FRectangle {
		TODO()
	}

	override fun measureTextWidth(text: FText): Int {
		TODO()
	}

	fun Game.openWeb(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

	private val Game.connection: NetworkInfo?
		get() = (getSystemService(Context.CONNECTIVITY_SERVICE)
				as ConnectivityManager).activeNetworkInfo

	/**
	 * if this value is null,
	 * it means I have 2 load data from Sp.
	 */
	val isNetConnected: Boolean
		get() = connection?.isConnected ?: false

	/**
	 * this will cache the data into SharedPreference
	 * next time when the network is invalid, it will return the data
	 * stored in the SharedPreference.
	 *
	 * this method extended String.
	 */
	@JvmOverloads
	fun webResource(
			url: String,
			default: String = "DEFAULT_VALUE",
			submit: (String) -> Unit) {
		var ret = ""
		doAsync {
			ret = readString(default)
			uiThread { submit(ret) }
		}
		doAsync {
			if (ret != "DEFAULT_VALUE" && !isNetConnected) {
				uiThread { submit(ret) }
			} else {
				ret = URL(url).readText(Charsets.UTF_8)
				uiThread { submit(ret) }
				save(url, ret)
			}
		}
	}

	override val screenCut get() = DroidImage(surfaceView.drawingCache)
}
