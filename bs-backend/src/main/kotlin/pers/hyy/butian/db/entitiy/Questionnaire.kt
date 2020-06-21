package pers.hyy.butian.db.entitiy

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigInteger
import java.util.*

@Document
data class Questionnaire(
        @Id var id: String?,
        var title: String?,
        var requireLogin: Boolean=false,
        var submitLimit: Int?,
        var description: String?,
        @Indexed var createdBy: String?,
        var startTime: Date?,
        var endTime: Date?,
        var displayMode: String?,
        var surveyMode: String?,
        var section: List<Int>?,
        @DBRef var questions: List<Question>?
)

interface QuestionnaireBasicInfo {
    val id:String?
    var title: String?
    var createdBy: String?
    var startTime: Date?
    var endTime: Date?
}