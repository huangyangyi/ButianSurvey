package pers.hyy.butian.db.entitiy

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class UserAuth(val accessLevel: Int, val group: String)

@Document
data class User(@Indexed(unique=true) var userName: String,
                @Indexed(unique=true) var userEmail: String,
                var hashPwd: String,
                var salt: String,
                var auth: UserAuth= UserAuth(1, "user"))
