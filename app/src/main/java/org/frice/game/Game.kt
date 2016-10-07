package org.frice.game

import org.frice.android.resource.image.ImageResource

/**
 * First game class(not for you)
 *
 * Standard library, mainly for GUI.
 * some other library is in @see
 * The base game class.
 * this class do rendering, and something which are invisible to
 * game developer.
 *
 * DO NOT override the constructor.
 *
 * Created by ice1000 on 2016/8/15.
 * @author ice1000
 * @since v0.2.3
 */
abstract class Game() {

	/**
	 * get a screenShot.
	 *
	 * @return screen cut as an bitmap
	 */
	fun getScreenCut() = ImageResource.create(stableBuffer)
}
