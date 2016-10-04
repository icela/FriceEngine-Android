package org.frice.game.resource.image

import android.graphics.Bitmap
import org.frice.game.Game
import org.frice.game.resource.FResource
import org.frice.game.utils.message.log.FLog
import org.frice.game.utils.time.FTimeListener
import org.frice.game.utils.web.WebUtils

/**
 * Created by ice1000 on 2016/8/13.
 * @author ice1000
 * @since v0.1
 */
abstract class ImageResource : FResource {

	companion object {
		@JvmStatic fun create(image: Bitmap) = object : ImageResource() {
			override var bitmap = image
		}

		@JvmStatic fun fromBitmap(bitmap: Bitmap): ImageResource = create(bitmap)
		@JvmStatic fun fromPath(path: String) = FileImageResource(path)
		@JvmStatic fun fromWeb(url: String) = WebImageResource(url)
		@JvmStatic fun empty() = create(Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888))
	}

	abstract var bitmap: Bitmap

	override fun getResource() = bitmap

	fun scaled(x: Double, y: Double) = fromBitmap(Bitmap.createScaledBitmap(
			bitmap,
			(x * bitmap.width).toInt(),
			(y * bitmap.height).toInt(),
			true
	))

	fun part(x: Int, y: Int, width: Int, height: Int) = fromBitmap(Bitmap.createBitmap(
			bitmap,
			x,
			y,
			width,
			height
	))

}


/**
 * create an bitmap from a part of another bitmap
 *
 * Created by ice1000 on 2016/8/15.
 * @author ice1000
 * @since v0.2.3
 */
class PartImageResource(origin: ImageResource, x: Int, y: Int, width: Int, height: Int) : ImageResource() {

	override var bitmap = Bitmap.createBitmap(origin.bitmap, x, y, width, height)
}


/**
 * Image Resource from internet
 *
 * Created by ice1000 on 2016/8/15.
 * @author ice1000
 * @since v0.2.2
 */
class WebImageResource(url: String) : ImageResource() {
	override var bitmap = WebUtils.readImage(url)

}


/**
 * Created by ice1000 on 2016/8/16.
 * @author ice1000
 * @since v0.3.1
 */
class FrameImageResource(val game: Game, val list: MutableList<ImageResource>, div: Int) : ImageResource() {

	constructor(game: Game, list: Array<ImageResource>, div: Int) : this(game, list.toMutableList(), div)

	private var start: Long
	private val timer: FTimeListener
	private var counter = 0
	private var ended = false
	var loop = true

	override var bitmap: Bitmap
		get() = if (loop || ended) list.getImage(counter).bitmap else list.last().bitmap
		set(value) {
			list.add(ImageResource.create(value))
		}

	fun MutableList<ImageResource>.getImage(index: Int): ImageResource {
		if (index == this.size - 1) ended = true
		return this[index]
	}

	init {
		start = System.currentTimeMillis()
		timer = FTimeListener(div, {
			FLog.e("counter = $counter")
			counter++
			counter %= list.size
		})
		game.addTimeListener(timer)
	}
}

/**
 * Created by ice1000 on 2016/8/13.
 * @author ice1000
 * @since v0.1
 */
class FileImageResource(path: String) : ImageResource() {
	override var bitmap = WebUtils.readImage(path)

}
