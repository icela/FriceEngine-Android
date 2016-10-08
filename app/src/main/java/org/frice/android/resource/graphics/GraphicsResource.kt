package org.frice.android.resource.graphics

import android.graphics.Bitmap
import android.graphics.Color
import org.frice.android.Game
import org.frice.android.resource.image.ImageResource
import org.frice.game.resource.FResource
import org.frice.game.utils.misc.forceRun
import org.frice.game.utils.misc.loop
import java.util.*

/**
 * Created by ice1000 on 2016/8/14.
 * @author ice1000
 * @since v0.1.1
 */
class ColorResource(color: Int) : FResource {
	val color: Int

	init {
		this.color = Color.rgb(
				(color shr 16) % 0xFF,
				(color shr 8) % 0xFF,
				(color) % 0xFF
		)
	}

	/**
	 * 颜表立。。。
	 * Read these color names in UTF-8 w/o BOM encoding, and pick up your 信仰 and 节操.
	 */
	companion object {
		@JvmField val GREEN = ColorResource(Color.GREEN)
		@JvmField val BLUE = ColorResource(Color.BLUE)
		@JvmField val GRAY = ColorResource(Color.GRAY)
		@JvmField val WHITE = ColorResource(Color.WHITE)
		@JvmField val RED = ColorResource(Color.RED)
		@JvmField val BLACK = ColorResource(Color.BLACK)
		@JvmField val CYAN = ColorResource(Color.CYAN)
		@JvmField val MAGENTA = ColorResource(Color.MAGENTA)
		@JvmField val YELLOW = ColorResource(Color.YELLOW)
		@JvmField val SHIT_YELLOW = ColorResource(0x633516)
		@JvmField val ORANGE = ColorResource(0xcc7832)
		@JvmField val PINK = ColorResource(0xC770CC)
		@JvmField val COLORLESS = ColorResource(Color.argb(0, 0, 0, 0))
		@JvmField val 小埋色 = ColorResource(0xFFAC2B)
		@JvmField val 基佬紫 = ColorResource(0x781895)
		@JvmField val 吾王蓝 = BLUE
		@JvmField val 教主黄 = YELLOW
		@JvmField val 宝强绿 = GREEN
		@JvmField val 冰封绿 = 宝强绿
		@JvmField val 如果奇迹有颜色那么一定是橙色 = ORANGE
		@JvmField val 高坂穗乃果 = ORANGE
		@JvmField val 南小鸟 = GRAY
		@JvmField val 园田海未 = BLUE
		@JvmField val 洵濑绘理 = ColorResource(0x0FFFFF)
		@JvmField val 星空凛 = 教主黄
		@JvmField val 西木野真姬 = RED
		@JvmField val 东条希 = 基佬紫
		@JvmField val 小泉花阳 = ColorResource(0x1BA61C)
		@JvmField val 矢泽妮可 = PINK
		@JvmField val 屎黄色 = SHIT_YELLOW
		@JvmField val 天依蓝 = ColorResource(0x66CCFF)
		@JvmField val 清真绿 = ColorResource(0x038B43)
		@JvmField val IntelliJ_IDEA黑 = ColorResource(0x2B2B2B)
		@JvmField val 如果真爱有颜色那么一定是黄色 = 教主黄
		@JvmField val 杀老师 = 如果真爱有颜色那么一定是黄色
		@JvmField val 潮田渚 = 园田海未
		@JvmField val 茅野枫 = 冰封绿
		@JvmField val 赤羽业 = 西木野真姬
	}

	/**
	 * not for users and developers.
	 * this should only be called by the engine itself.
	 */
	override fun getResource() = color

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || other !is ColorResource) return false
		return color == other.color
	}

	override fun hashCode() = color.hashCode()
}


/**
 * Created by ice1000 on 2016/8/27.
 *
 * @author ice1000
 * @since v0.4
 */
class FunctionResource(color: ColorResource, val f: (Double) -> Double, width: Int, height: Int) : FResource {

	private val image: Bitmap

	init {
		image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
		var lastTime = f(0.0)
		var thisTime: Double
		(0..width step 1).forEach { x ->
			thisTime = f(x.toDouble())
			forceRun { image.setPixel(x.toInt(), thisTime.toInt(), color.color) }
			if (Math.abs(thisTime - lastTime) >= 1.0) forceRun {
				(Math.min(thisTime, lastTime).toInt()..Math.max(thisTime, lastTime).toInt()).forEach { i ->
					image.setPixel(x, i, color.color)
				}
			}
			lastTime = thisTime
		}
	}

	override fun getResource() = image
}

/**
 * used to represent a Curve.
 * something like circle.
 */
class CurveResource(color: ColorResource, val f: (Double) -> List<Double>, width: Int, height: Int) : FResource {
	private val image: Bitmap

	init {
		image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
		(0..width step 1).forEach { x ->
			f(x.toDouble()).forEach { y ->
				forceRun { image.setPixel(x, y.toInt(), color.color) }
			}
		}
	}

	override fun getResource() = image
}

/**
 * Particle effects
 * Created by ice1000 on 2016/8/17.
 *
 * @author ice1000
 * @since v0.3.2
 */
// FIXME!!!
class ParticleResource(
		val game: Game,
		var width: Int,
		var height: Int,
		val back: FResource,
		var fore: ColorResource,
		var percentage: Double) : FResource {
	constructor(game: Game, x: Int, y: Int, back: ColorResource, fore: ColorResource) :
	this(game, x, y, back, fore, 0.5)

	constructor(game: Game, x: Int, y: Int, percentage: Double) :
	this(game, x, y, ColorResource.WHITE, ColorResource.BLACK, percentage)

	constructor(game: Game, x: Int, y: Int) : this(game, x, y, 0.5)

	/**
	 * particle effects as an bitmap
	 */
	private val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
	private val random = Random(Random().nextLong())

	private fun drawBackground() {
		// unknown how 2 deal with
		// FIXME
	}

	init {
		drawBackground()
		loop((bitmap.width * bitmap.height * percentage).toInt()) {
			bitmap.setPixel(random.nextInt(width), random.nextInt(height), fore.color)
		}
	}

	override fun getResource() = bitmap.apply {
		var cache1: Int
		var cache2: Int
		loop((bitmap.width * bitmap.height * percentage).toInt()) {
			cache1 = random.nextInt(width)
			cache2 = random.nextInt(height)
			bitmap.setPixel(random.nextInt(width), random.nextInt(height), fore.color)
			bitmap.setPixel(cache1, cache2, when (back) {
				is ColorResource -> back.color
				is ImageResource -> back.bitmap.getPixel(cache1, cache2)
				else -> ColorResource.COLORLESS.color
			})
		}
	}
}
