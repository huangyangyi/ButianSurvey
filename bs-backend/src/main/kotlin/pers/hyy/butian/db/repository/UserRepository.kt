package pers.hyy.butian.db.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pers.hyy.butian.db.entitiy.User

@Repository
interface UserRepository: MongoRepository<User, String> {
    fun findByUserName(userName: String): User?
}