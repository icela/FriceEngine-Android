package org.frice.platform

import org.frice.obj.AbstractObject
import org.frice.obj.button.FText
import java.util.*

class Layer {
	val objects = mutableListOf<AbstractObject>()
	val objectDeleteBuffer = mutableListOf<AbstractObject>()
	val objectAddBuffer = mutableListOf<AbstractObject>()

	val texts = mutableListOf<FText>()
	val textDeleteBuffer = mutableListOf<FText>()
	val textAddBuffer = mutableListOf<FText>()

	fun processBuffer() {
		objects.addAll(objectAddBuffer)
		objects.removeAll(objectDeleteBuffer)
		objectDeleteBuffer.clear()
		objectAddBuffer.clear()

		texts.addAll(textAddBuffer)
		texts.removeAll(textDeleteBuffer)
		textDeleteBuffer.clear()
		textAddBuffer.clear()
	}

	fun clearObjects() {
		objectDeleteBuffer.addAll(objects)
		textDeleteBuffer.addAll(texts)
	}

	fun addObject(obj: AbstractObject): Boolean = when (obj) {
		is FText -> textAddBuffer.add(obj)
		else -> objectAddBuffer.add(obj)
	}

	fun removeObject(obj: AbstractObject): Boolean = when (obj) {
		is FText -> textDeleteBuffer.add(obj)
		else -> objectDeleteBuffer.add(obj)
	}

	fun instantAddObject(obj: AbstractObject): Boolean = when (obj) {
		is FText -> texts.add(obj)
		else -> objects.add(obj)
	}

	fun instantRemoveObject(obj: AbstractObject): Boolean = when (obj) {
		is FText -> texts.remove(obj)
		else -> objects.remove(obj)
	}
}