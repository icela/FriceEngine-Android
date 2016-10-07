package org.frice.android.obj.button

import org.frice.android.obj.AbstractObject
import org.frice.android.resource.graphics.ColorResource

/**
 * Created by ice1000 on 2016/8/19.
 *
 * @author ice1000
 * @since v0.4
 */
abstract class FText : AbstractObject {
	open var text = ""
	override var rotate = 0.0

	abstract fun getColor(): ColorResource
}

/**
 * Created by ice1000 on 2016/8/19.
 *
 * @author ice1000
 * @since v0.4
 */
class SimpleText(var colorResource: ColorResource, override var text: String,
                 override var x: Double, override var y: Double) : FText() {

	constructor(text: String, x: Double, y: Double) :
	this(ColorResource.GRAY, text, x, y)

	override fun getColor() = colorResource
}
