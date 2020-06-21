package pers.hyy.butian.db.entitiy

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Document
data class Answer(@Id var id: String?, @Indexed val questionId: String?, val value: Any?)

@Document
data class Reply(
        @Id var id: String?,
        @Indexed
        val questionnaireId: String?,
        val userName: String?,
        @CreatedDate
        var createdDate: Date?,
        val userIp: String?,
        @DBRef
        val answer: List<Answer>
)