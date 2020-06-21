package pers.hyy.butian.db.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pers.hyy.butian.db.entitiy.Question
import pers.hyy.butian.db.entitiy.Questionnaire


@Repository
interface QuestionRepository: MongoRepository<Question, String> {
}