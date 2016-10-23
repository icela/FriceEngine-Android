package org.frice.android.utils.data

import java.io.File

/**
 * Created by ice1000 on 2016/9/3.
 *
 * @author ice1000
 * @since v0.5
 */
// TODO image 2 file

fun string2File(string: String, file: File) = file.writeText(string)

fun string2File(string: String, file: String) = string2File(string, File(file))

fun bytes2File(byteArray: ByteArray, file: File) = file.writeBytes(byteArray)
fun bytes2File(byteArray: ByteArray, file: String) = bytes2File(byteArray, File(file))

