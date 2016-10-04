package org.frice.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.frice.game.obj.AbstractObject
import org.frice.game.obj.button.FText
import org.frice.game.resource.FResource
import org.frice.game.resource.graphics.ColorResource
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
    var back: FResource = ColorResource.LIGHT_GRAY
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

        thread {

        }
    }

    inner class FriceCanvas(context: Context) : View(context) {

        val p = Paint()

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
        }
    }
}
