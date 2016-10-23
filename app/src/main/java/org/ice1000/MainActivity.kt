package org.ice1000

import org.frice.android.Game
import org.frice.android.resource.graphics.ColorResource
import org.frice.android.utils.graphics.shape.FCircle
import org.frice.game.obj.sub.ShapeObject

class MainActivity : Game() {
	override fun onInit() {
		addObject(ShapeObject(ColorResource.BLACK, FCircle(10.0)))
	}
}
