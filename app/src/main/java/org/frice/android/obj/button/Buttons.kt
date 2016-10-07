package org.frice.android.obj.button

import android.graphics.Bitmap
import org.frice.android.resource.graphics.ColorResource
import org.frice.android.resource.image.ImageResource
import org.frice.android.utils.graphics.utils.ColorUtils.darker
import org.frice.game.obj.AbstractObject
import org.frice.game.obj.FContainer
import org.frice.game.obj.FObject

/**
 * Created by ice1000 on 2016/8/18.
 * @author ice1000
 * @since v0.3.2
 */

interface FButton : FContainer, AbstractObject {
	var onClickListener: OnClickListener?

	fun onClick() = onClickListener?.onClick()

	/**
	 * Created by ice1000 on 2016/8/19.
	 * @author ice1000
	 * @since v0.4
	 */
	interface OnClickListener {
		fun onClick()
	}

	/**
	 * Created by ice1000 on 2016/8/19.
	 * @author ice1000
	 * @since v0.4
	 */
	interface OnMouseListener {
		fun onMouse()
	}
}

/**
 * Created by ice1000 on 2016/9/3 0003.
 *
 * @author ice1000
 * @since v0.5
 */
class ImageButton(val imageNormal: ImageResource, val imagePressed: ImageResource, x: Double, y: Double) :
		FButton, FObject.ImageOwner, SimpleButton("", x, y,
		imageNormal.bitmap.width.toDouble(), imageNormal.bitmap.height.toDouble()) {

	override var rotate = 0.0

	constructor(image: ImageResource, x: Double, y: Double) :
	this(image, image, x, y)

	override var onClickListener: FButton.OnClickListener? = null

	private var bool = false

	override val image: Bitmap
		get () = if (bool) imagePressed.bitmap
		else imageNormal.bitmap
}

/**
 * Created by ice1000 on 2016/8/18.
 *
 * @author ice1000
 * @since v0.3.3
 */
open class SimpleButton(
		var colorResource: ColorResource,
		override var text: String,
		override var x: Double, override var y: Double,
		override var width: Double, override var height: Double) : FButton, FText() {

	constructor(text: String, x: Double, y: Double, width: Double, height: Double) :
	this(ColorResource.IntelliJ_IDEA黑, text, x, y, width, height)

	private var bool = false

	override var onClickListener: FButton.OnClickListener? = null

	override fun getColor() =
			if (bool) ColorResource(colorResource.color.darker())
			else colorResource
}
