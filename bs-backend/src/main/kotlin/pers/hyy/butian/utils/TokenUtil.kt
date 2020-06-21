package pers.hyy.butian.utils

import com.auth0.jwt.JWT
import org.springframework.web.context.request.RequestContextHolder

import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest


object TokenUtil {
    // 从 http 请求头中取出 token
    val tokenUserId: String
        get() {
            val token = request!!.getHeader("token") // 从 http 请求头中取出 token
            return JWT.decode(token).audience[0]
        }

    /**
     * 获取request
     *
     * @return
     */
    val request: HttpServletRequest?
        get() {
            val requestAttributes = RequestContextHolder
                    .getRequestAttributes() as ServletRequestAttributes?
            return requestAttributes?.request
        }
}