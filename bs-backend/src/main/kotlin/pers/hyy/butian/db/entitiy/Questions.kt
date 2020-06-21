package pers.hyy.butian.db.entitiy

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Question(
        @Id var id: String?,
        var mustFill: Boolean,
        var index: Int,
        var type: String,
        var title: String,
        var description: String?,
        var dependencies: List<Int>,
        var content: Map<String, Any>
)
