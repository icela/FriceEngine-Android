package org.ice1000

import org.frice.Game
import org.frice.obj.sub.ShapeObject
import org.frice.resource.graphics.ColorResource
import org.frice.utils.shape.FCircle

class MainActivity : Game() {
	override fun onInit() {
		addObject(ShapeObject(ColorResource.BLACK, FCircle(10.0)))
	}
}
