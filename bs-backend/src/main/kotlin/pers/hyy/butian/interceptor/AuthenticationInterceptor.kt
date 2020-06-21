package pers.hyy.butian.interceptor

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import pers.hyy.butian.db.entitiy.User
import pers.hyy.butian.db.repository.UserRepository
import pers.hyy.butian.utils.PassToken
import pers.hyy.butian.utils.UserLoginToken
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationInterceptor : HandlerInterceptor {
    @Autowired
    lateinit var userRepository: UserRepository

    @Override
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = request.getHeader("token")
        if (handler !is HandlerMethod) return true
        var handlerMethod = handler as HandlerMethod
        var method = handlerMethod.method
        // if PassToken annotation exist, pass the authentication
        if (method.isAnnotationPresent(PassToken::class.java)) {
            var passToken: PassToken = method.getAnnotation(PassToken::class.java)
            if (passToken.required) return true
        }
        if (method.isAnnotationPresent(UserLoginToken::class.java)) {
            var userLoginToken = method.getAnnotation(UserLoginToken::class.java)
            if (userLoginToken.required) {
                if (token == null) {
                    throw RuntimeException("Expected token but got null, please login again")
                }
                var userName: String
                try {
                    userName = JWT.decode(token).audience[0]
                } catch (jwtErr: JWTDecodeException) {
                    throw RuntimeException("401")
                }
                val user: User = userRepository.findByUserName(userName)
                        ?: throw RuntimeException("User not exists, please login again")
                val jwtVerifier = JWT.require(Algorithm.HMAC256(user.hashPwd)).build()
                try {
                    jwtVerifier.verify(token)
                } catch (err: JWTVerificationException) {
                    throw RuntimeException("401")
                }
                return true
            }
        }
        return true
    }

    @Override
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) =
            Unit

    @Override
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) =
            Unit
}