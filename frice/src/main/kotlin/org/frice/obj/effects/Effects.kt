package org.frice.obj.effects

import org.frice.obj.AbstractObject
import org.frice.resource.graphics.ColorResource
import org.frice.resource.graphics.ColorResource.Companion.BLACK

/**
 * Created by ice1000 on 2016/8/19.
 *
 * @author ice1000
 * @since v0.5
 */
class LineEffect
@JvmOverloads
constructor(
	var color: ColorResource = BLACK,
	override var x: Double,
	override var y: Double,
	var x2: Double,
	var y2: Double) : AbstractObject {

	override var rotate = 0.0
}
