package com.steve.jungsoos.util

class ItemUtil {

    fun getThumbNail(itemId: String) : String {
        when (itemId) {
            "1", "0001" -> return "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/apple/237/banana_1f34c.png"
            "2", "0002" -> return "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/apple/237/red-apple_1f34e.png"
            else -> return "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/120/apple/237/sparkles_2728.png"
        }
    }

    fun getQrUrl (idStr: String): String {
        return "https://zxing.org/w/chart?cht=qr&chs=350x350&chld=L&choe=UTF-8&chl=$idStr"
    }

    fun getName(itemId: String): String {
        when (itemId) {
            "1" -> return "Banana"
            "2" -> return "Apple"
            else -> return "Other Stuff"
        }
    }

    fun getIdString(itemId: String) : String {
        if (itemId.length > 4) return ""
        return String.format("%4s", itemId).replace(' ', '0')
    }
}