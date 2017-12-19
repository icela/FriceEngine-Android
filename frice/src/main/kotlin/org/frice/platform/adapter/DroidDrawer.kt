package org.frice.platform.adapter

import android.graphics.*
import org.frice.platform.FriceDrawer
import org.frice.platform.FriceImage
import org.frice.resource.graphics.ColorResource
import org.frice.utils.cast

/**
 * Created by ice1000 on 2016/10/31.
 *
 * @author ice1000
 */
class DroidDrawer(var canvas: Canvas) : FriceDrawer {
	constructor(bitmap: Bitmap) : this(Canvas(bitmap))

	val paint = Paint()

	override fun init() {
		canvas.save()
	}

	private val rectF = RectF()

	override var color: ColorResource
		get() = ColorResource(paint.color)
		set(value) {
			paint.color = value.color
		}

	val g = canvas

	override fun stringSize(size: Double) {
		paint.textSize = size.toFloat()
	}

	override fun drawOval(x: Double, y: Double, width: Double, height: Double) {
		rectF.set(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
		paint.style = Paint.Style.FILL
		g.drawOval(rectF, paint)
	}

	override fun strokeOval(x: Double, y: Double, width: Double, height: Double) {
		rectF.set(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
		paint.style = Paint.Style.STROKE
		g.drawOval(rectF, paint)
	}

	override fun drawString(string: String, x: Double, y: Double) {
		g.drawText(string, x.toFloat(), y.toFloat(), paint)
	}

	override fun drawImage(image: FriceImage, x: Double, y: Double) {
		g.drawBitmap(cast<DroidImage>(image).image, x.toFloat(), y.toFloat(), paint)
	}

	override fun drawRect(x: Double, y: Double, width: Double, height: Double) {
		rectF.set(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
		paint.style = Paint.Style.FILL
		g.drawRect(rectF, paint)
	}

	override fun strokeRect(x: Double, y: Double, width: Double, height: Double) {
		rectF.set(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
		paint.style = Paint.Style.STROKE
		g.drawRect(rectF, paint)
	}

	override fun drawLine(x: Double, y: Double, width: Double, height: Double) =
			g.drawLine(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), paint)

	override fun rotate(theta: Double, x: Double, y: Double) = g.rotate(theta.toFloat(), x.toFloat(), y.toFloat())

	override fun drawRoundRect(
			x: Double,
			y: Double,
			width: Double,
			height: Double,
			arcWidth: Double,
			arcHeight: Double) {
		rectF.set(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
		paint.style = Paint.Style.FILL
		g.drawRoundRect(rectF, arcWidth.toFloat(), arcHeight.toFloat(), paint)
	}

	override fun strokeRoundRect(
			x: Double,
			y: Double,
			width: Double,
			height: Double,
			arcWidth: Double,
			arcHeight: Double) {
		rectF.set(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat())
		paint.style = Paint.Style.STROKE
		g.drawRoundRect(rectF, arcWidth.toFloat(), arcHeight.toFloat(), paint)
	}

	override fun restore() {
		canvas.restore()
	}
}