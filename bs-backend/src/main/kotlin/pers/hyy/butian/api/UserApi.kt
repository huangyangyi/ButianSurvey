package pers.hyy.butian.api

import RandomStringGenerator
import com.mongodb.MongoException
import encodeMD5
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import pers.hyy.butian.db.entitiy.User
import pers.hyy.butian.db.entitiy.UserAuth
import pers.hyy.butian.db.repository.UserRepository
import pers.hyy.butian.service.TokenService
import pers.hyy.butian.utils.UserLoginToken
import java.lang.RuntimeException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

data class LoginInfo(val userName: String, val passwd: String)
data class RegisterInfo(val userName: String,
                        var userEmail: String,
                        var passwd: String,
                        var auth: UserAuth=UserAuth(1, "user"))

@RestController
@RequestMapping("/api/users")
open class UserApi {
    @Autowired
    lateinit var repo: UserRepository
    @Autowired
    lateinit var tokenService: TokenService
    @UserLoginToken
    @RequestMapping("/get/{userName}", method = [RequestMethod.GET])
    fun get(@PathVariable userName: String): ApiResult {
        return try {
            ApiResult("success", repo.findByUserName(userName) ?: "[]")
        } catch (err: MongoException) {
            ApiResult("failure", err.message)
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody registerInfo: RegisterInfo): ApiResult {
        return try {
            val salt = RandomStringGenerator.getRandomString(32)
            var user = User(
                    registerInfo.userName,
                    registerInfo.userEmail,
                    encodeMD5(registerInfo.passwd + salt),
                    salt
            )
            ApiResult("success", repo.insert(user))
        } catch (err: RuntimeException) {
            if (err.message!!.contains("dup key"))
                ApiResult("failure", "Duplicated User!")
            else
                ApiResult("failure", "Database error!")
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginInfo: LoginInfo, response: HttpServletResponse): ApiResult {
        return try {
            val user: User? = repo.findByUserName(loginInfo.userName)
            if (user != null) {
                val hashPwd = encodeMD5(loginInfo.passwd + user.salt)
                if (user.hashPwd == hashPwd) {
                    var token = tokenService.getToken(user)
                    var cookie = Cookie("token", token)
                    cookie.path="/"
                    response.addCookie(cookie)
                    ApiResult("success",  mapOf(Pair("token", token)))
                } else
                    ApiResult("failure", "Authentication failed")
            } else ApiResult("failure", "No such user")
        } catch (err: RuntimeException) {
            ApiResult("failure", "Database error!")
        }
    }
}