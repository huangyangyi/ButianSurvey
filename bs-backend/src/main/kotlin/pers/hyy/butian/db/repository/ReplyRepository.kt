package pers.hyy.butian.db.repository

import org.bson.types.BasicBSONList
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pers.hyy.butian.db.entitiy.Reply

@Repository
interface ReplyRepository: MongoRepository<Reply, String> {
    fun findAllByQuestionnaireId(questionnaireId: String):List<Reply>
    fun countByQuestionnaireId(questionnaireId: String):Int
    fun countByQuestionnaireIdAndUserName(questionnaireId: String, userName: String):Int
    fun countByQuestionnaireIdAndUserIp(questionnaireId: String, userIp: String):Int
    fun deleteAllByQuestionnaireId(questionnaireId: String)
}