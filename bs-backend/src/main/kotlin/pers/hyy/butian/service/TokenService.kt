package pers.hyy.butian.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Service
import pers.hyy.butian.db.entitiy.User
import java.util.*


@Service("TokenService")
class TokenService {
    fun getToken(user: User): String {
        val start = Date()
        val currentTime = System.currentTimeMillis() + 60 * 60 * 1000 //一小时有效时间

        val end = Date(currentTime)
        var token: String? = ""

        token = JWT.create().withAudience(user.userName).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.hashPwd))
        return token
    }
}