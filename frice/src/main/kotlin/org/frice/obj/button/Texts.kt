package org.frice.obj.button

import org.frice.obj.AbstractObject
import org.frice.resource.graphics.ColorResource
import org.frice.resource.graphics.ColorResource.Companion.DARK_GRAY

/**
 * Created by ice1000 on 2016/8/19.
 *
 * @author ice1000
 * @since v0.4
 */
abstract class FText : AbstractObject {
	open var text = ""
	open var textSize: Double = 16.0

	override var rotate = 0.0
	abstract val color: ColorResource
}

/**
 * Created by ice1000 on 2016/8/19.
 *
 * @author ice1000
 * @since v0.4
 */
class SimpleText
@JvmOverloads
constructor(
	override var color: ColorResource = DARK_GRAY,
	override var text: String,
	override var x: Double,
	override var y: Double) : FText()
