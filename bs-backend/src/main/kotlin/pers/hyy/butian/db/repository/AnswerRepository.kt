package pers.hyy.butian.db.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pers.hyy.butian.db.entitiy.Answer

@Repository
interface AnswerRepository:MongoRepository<Answer, String> {
    fun findAnswersByQuestionId(questionId: String):Answer?
    fun deleteAllByQuestionId(questionId: String)
}