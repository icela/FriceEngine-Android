package org.frice.game

import org.frice.game.event.OnClickEvent
import org.frice.game.event.OnKeyEvent
import org.frice.game.event.OnMouseEvent
import org.frice.game.event.OnWindowEvent
import org.frice.game.obj.AbstractObject
import org.frice.game.obj.FObject
import org.frice.game.obj.PhysicalObject
import org.frice.game.obj.button.FButton
import org.frice.game.obj.button.FText
import org.frice.game.obj.button.SimpleButton
import org.frice.game.obj.effects.LineEffect
import org.frice.game.obj.sub.ImageObject
import org.frice.game.obj.sub.ShapeObject
import org.frice.game.resource.FResource
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.resource.image.ImageResource
import org.frice.game.utils.graphics.shape.FOval
import org.frice.game.utils.graphics.shape.FRectangle
import org.frice.game.utils.graphics.utils.ColorUtils
import org.frice.game.utils.message.FDialog
import org.frice.game.utils.message.error.FatalError
import org.frice.game.utils.message.log.FLog
import org.frice.game.utils.misc.forceRun
import org.frice.game.utils.misc.loop
import org.frice.game.utils.misc.loopIf
import org.frice.game.utils.time.FTimeListener
import org.frice.game.utils.time.FTimer
import java.awt.*
import java.awt.event.*
import java.awt.image.BufferedImage
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.WindowConstants

/**
 * First game class(not for you)
 *
 * Standard library, mainly for GUI.
 * some other library is in @see
 * The base game class.
 * this class do rendering, and something which are invisible to
 * game developer.
 *
 * DO NOT override the constructor.
 *
 * Created by ice1000 on 2016/8/15.
 * @author ice1000
 * @since v0.2.3
 */
abstract class Game() : JFrame(), Runnable {

	private val buffer: BufferedImage

	private val panel: GamePanel
	private val stableBuffer: BufferedImage

	private val bg: Graphics2D
		get() = buffer.graphics as Graphics2D


	init {
		iconImage = ImageIO.read(javaClass.getResourceAsStream("/icon.png"))

		Thread(this).start()
	}

	protected fun mouse(e: OnMouseEvent) = texts.forEach { b ->
		if (b is FButton && b.containsPoint(e.event.x, e.event.y)) b onMouse e
	}

	protected fun click(e: OnClickEvent) = texts.forEach { b ->
		if (b is FButton && b.containsPoint(e.event.x, e.event.y)) b onClick e
	}

	protected open fun onClick(e: OnClickEvent) = Unit
	protected open fun onMouse(e: OnMouseEvent) = Unit
	protected open fun onExit() {
		if (FDialog(this).confirm("Are you sure to exit?",
				"Ensuring", JOptionPane.YES_NO_OPTION) ==
				JOptionPane.YES_OPTION)
			System.exit(0)
		else return
	}



	protected fun drawEverything(getBGG: () -> Graphics2D) {
		processBuffer()

		objects.forEach { o ->
			if (o is FObject) {
				o.runAnims()
				o.checkCollision()
			}
		}

		objects.forEach { o ->
			when (o) {
				is FObject.ImageOwner -> bgg.drawImage(o.image, o.x.toInt(), o.y.toInt(), this)
				is ShapeObject -> {
					bgg.color = o.getResource().color
					when (o.collideBox) {
						is FRectangle -> bgg.fillRect(
								o.x.toInt(),
								o.y.toInt(),
								o.width.toInt(),
								o.height.toInt()
						)
						is FOval -> bgg.fillOval(
								o.x.toInt(),
								o.y.toInt(),
								o.width.toInt(),
								o.height.toInt()
						)
					}
				}
				is LineEffect -> bgg.drawLine(o.x.toInt(), o.y.toInt(), o.x2.toInt(), o.y2.toInt())
			}
			if (autoGC && (o.x.toInt() < -width ||
					o.x.toInt() > width + width ||
					o.y.toInt() < -height ||
					o.y.toInt() > height + height)) {
				if (o is PhysicalObject) o.died = true
				removeObject(o)
				//FLog.i("o.x.toInt() = ${o.x.toInt()}, width = $width," +
				//		" o.y.toInt() = ${o.y.toInt()}, height = $height")
			}
		}

		texts.forEach { b ->
			val bgg = getBGG()
			if (b is FButton) {
				when (b) {
					is FObject.ImageOwner -> bgg.drawImage(b.image, b.x.toInt(), b.y.toInt(), this)
					is SimpleButton -> {
						bgg.color = b.getColor().color
						bgg.fillRoundRect(b.x.toInt(), b.y.toInt(),
								b.width.toInt(), b.height.toInt(),
								Math.min((b.width * 0.5).toInt(), 10),
								Math.min((b.height * 0.5).toInt(), 10))
						bgg.color = ColorResource.GRAY.color
						bgg.drawString(b.text, b.x.toInt() + 10, (b.y + (b.height / 2)).toInt())
					}
				}
			} else bgg.drawString(b.text, b.x.toInt(), b.y.toInt())
		}
		customDraw(getBGG())
	}

	/**
	 * set the frame bounds (size and position)
	 */
	override infix fun setBounds(r: Rectangle) {
		super.setBounds(r)
		panel.bounds = r
	}

	/**
	 * set the frame bounds (size and position)
	 */
	override fun setBounds(x: Int, y: Int, width: Int, height: Int) {
		super.setBounds(x, y, width, height)
		panel.setBounds(x, y, width, height)
	}

	/**
	 * set the frame size
	 */
	override fun setSize(width: Int, height: Int) {
		super.setSize(width, height)
		panel.setSize(width, height)
	}

	/**
	 * set the frame size
	 */
	override infix fun setSize(d: Dimension) {
		super.setSize(d)
		panel.size = d
	}

	/**
	 * get a screenShot.
	 *
	 * @return screen cut as an bitmap
	 */
	fun getScreenCut() = ImageResource.create(stableBuffer)

	/**
	 * this method escaped the error
	 *
	 * @return exact position of the mouse
	 */
	override fun getMousePosition() = panel.mousePosition!!

	override fun run() {
		loopIf() {
			forceRun {

			}
		}
		FLog.v("Engine thread exited.")
	}

	private fun drawBackground(back: FResource, g: Graphics2D) {
		when (back) {
			is ImageResource -> g.paint = TexturePaint(back.bitmap, Rectangle(0, 0, width, height))
			is ColorResource -> g.color = back.color
			else -> throw FatalError("Unable to draw background")
		}
		g.fillRect(0, 0, width, height)
	}

	/**
	 * Demo24 game view.
	 * all rendering work and game object calculating are here.
	 *
	 * Created by ice1000 on 2016/8/13.
	 * @author ice1000
	 * @since v0.1
	 */
	inner class GamePanel() : JPanel() {
		init {
			addMouseListener(object : MouseListener {
				override fun mouseClicked(e: MouseEvent) {
					click(OnMouseEvent.create(e, OnMouseEvent.MOUSE_CLICK))
					onClick(OnClickEvent.create(e))
				}

				override fun mouseEntered(e: MouseEvent) = onMouse(OnMouseEvent.create(e, OnMouseEvent.MOUSE_ENTERED))
				override fun mouseExited(e: MouseEvent) = onMouse(OnMouseEvent.create(e, OnMouseEvent.MOUSE_EXITED))
				override fun mouseReleased(e: MouseEvent) {
					mouse(OnMouseEvent.create(e, OnMouseEvent.MOUSE_RELEASED))
					onMouse(OnMouseEvent.create(e, OnMouseEvent.MOUSE_RELEASED))
				}

				override fun mousePressed(e: MouseEvent) {
					mouse(OnMouseEvent.create(e, OnMouseEvent.MOUSE_PRESSED))
					onMouse(OnMouseEvent.create(e, OnMouseEvent.MOUSE_PRESSED))
				}
			})

			addWindowListener(object : WindowListener {
				override fun windowDeiconified(e: WindowEvent) = Unit
				override fun windowActivated(e: WindowEvent) {
					loseFocus = false
					onFocus(OnWindowEvent.create(e))
				}

				override fun windowDeactivated(e: WindowEvent) {
					loseFocus = true
					onLoseFocus(OnWindowEvent.create(e))
				}

				override fun windowIconified(e: WindowEvent) = Unit
				override fun windowClosing(e: WindowEvent) = onExit()
				override fun windowClosed(e: WindowEvent) = Unit
				override fun windowOpened(e: WindowEvent) = Unit
			})
		}

		override fun update(g: Graphics?) = paint(g)
		override fun paintComponent(g: Graphics) {
			drawBackground(back, bg)
			drawEverything({ bg })

			if (loseFocus && loseFocusChangeColor) {
				loop(buffer.width) { x ->
					loop(buffer.height) { y ->
						buffer.setRGB(x, y, ColorUtils.darkerRGB(buffer.getRGB(x, y)))
					}
				}
			}

			if (showFPS) bg.drawString("fps: $fpsDisplay", 30, height - 30)

			/*
			 * 厚颜无耻
			 * bg.drawString("Powered by FriceEngine. ice1000", 5, 20)
			 */

			stableBuffer.graphics.drawImage(buffer, 0, 0, this)
			g.drawImage(buffer, 0, 0, this)
		}
	}

}
