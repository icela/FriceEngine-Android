package org.frice.platform.adapter

import android.graphics.Bitmap
import org.frice.platform.FriceImage
import org.frice.resource.graphics.ColorResource
import org.frice.utils.*

open class DroidImage(val image: Bitmap) : FriceImage {
	constructor(width: Int, height: Int) : this(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888))

	override val width = image.width
	override val height = image.height
	override operator fun get(x: Int, y: Int) = ColorResource(image.getPixel(x, y))
	override operator fun set(x: Int, y: Int, color: Int) = image.setPixel(x, y, color)

	override fun scale(x: Double, y: Double) = TODO() // DroidImage(scaleImpl(x, y))
//	protected fun scaleImpl(x: Double, y: Double): Bitmap = AffineTransformOp(
//			AffineTransform().apply { scale(x, y) },
//			AffineTransformOp.TYPE_BILINEAR).filter(image, null)

	override fun part(x: Int, y: Int, width: Int, height: Int) = DroidImage(Bitmap.createBitmap(image, x, y, width, height))

	override fun clone() = DroidImage(image.copy(image.config, true))

	override fun flip(orientation: Boolean) = TODO() // DroidImage(flipImpl(orientation))
//	protected fun flipImpl(orientation: Boolean): BufferedImage = AffineTransformOp(
//			if (orientation) AffineTransform.getScaleInstance(-1.0, 1.0).apply { translate((-width).toDouble(), 0.0) } // horizontal
//			else AffineTransform.getScaleInstance(1.0, -1.0).apply { translate(0.0, (-height).toDouble()) }, // vertical
//			AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(image, null)

	fun greenify() = image.greenify()
	fun redify() = image.redify()
	fun bluify() = image.bluify()
}


