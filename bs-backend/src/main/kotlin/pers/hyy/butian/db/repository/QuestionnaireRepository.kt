package pers.hyy.butian.db.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pers.hyy.butian.db.entitiy.Questionnaire
import pers.hyy.butian.db.entitiy.QuestionnaireBasicInfo

@Repository
interface QuestionnaireRepository: MongoRepository<Questionnaire, String> {
    fun findAllByCreatedBy(createBy: String): Collection<QuestionnaireBasicInfo>
    fun findAllByTitleLikeAndCreatedBy(title: String, createdBy: String): Collection<QuestionnaireBasicInfo>
}
