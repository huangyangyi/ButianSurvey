import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun encodeMD5(text: String): String {
    try {
        //获取md5加密对象
        val instance: MessageDigest = MessageDigest.getInstance("MD5")
        //对字符串加密，返回字节数组
        val digest:ByteArray = instance.digest(text.toByteArray())
        var sb : StringBuffer = StringBuffer()
        for (b in digest) {
            //获取低八位有效值
            var i :Int = b.toInt() and 0xff
            //将整数转化为16进制
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                //如果是一位的话，补0
                hexString = "0" + hexString
            }
            sb.append(hexString)
        }
        return sb.toString()

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return ""
}

object RandomStringGenerator {

    private fun getRandom(count: Int): Int {
        return Math.round(Math.random() * (count)).toInt()
    }

    private val string = "1234567890abcdefghijklmnopqrstuvwxyz"

    fun getRandomString(length: Int): String {
        val sb = StringBuffer()
        val len = string.length
        for (i in 0..length - 1) {
            sb.append(string[getRandom(len - 1)])
        }
        return sb.toString()
    }
}