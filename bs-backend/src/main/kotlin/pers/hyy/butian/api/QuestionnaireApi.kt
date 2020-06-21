package pers.hyy.butian.api

import com.mongodb.MongoException
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import pers.hyy.butian.db.entitiy.Questionnaire
import pers.hyy.butian.db.entitiy.Reply
import pers.hyy.butian.db.repository.AnswerRepository
import pers.hyy.butian.db.repository.QuestionRepository
import pers.hyy.butian.db.repository.QuestionnaireRepository
import pers.hyy.butian.db.repository.ReplyRepository
import java.util.*

@RestController
@RequestMapping("/api/qn")
open class QuestionnaireApi {
    @Autowired
    lateinit var qnRepo: QuestionnaireRepository

    @Autowired
    lateinit var qRepo: QuestionRepository

    @Autowired
    lateinit var rRepo: ReplyRepository

    @Autowired
    lateinit var aRepo: AnswerRepository

    @RequestMapping("/save", method = [RequestMethod.POST])
    fun save(@RequestBody data: Questionnaire): ApiResult {

        if (data.id == null) {
            data.id = ObjectId.get().toHexString()
        }
        for (q in data.questions!!) {
            if (q.id == null) {
                q.id = ObjectId.get().toHexString()
            }
            qRepo.save(q);
        }
        return try {
            ApiResult("success", qnRepo.save(data) ?: "[]")
        } catch (err: MongoException) {
            ApiResult("failure", err.message)
        }
    }

    @RequestMapping("/get/{questionnaireId}", method = [RequestMethod.GET])
    fun get(@PathVariable questionnaireId: String): ApiResult {
        return try {
            var questionnaire = qnRepo.findById(questionnaireId)
            if (questionnaire != null)
                ApiResult("success", questionnaire)
            else
                ApiResult("failure", "Survey not found!")
        } catch (err: Exception) {
            ApiResult("failure", err.message)
        }
    }

    @RequestMapping("/get_user_qn/{userName}", method = [RequestMethod.GET])
    fun getUserQn(@PathVariable userName: String): ApiResult {
        return try {
            var qnInfo = qnRepo.findAllByCreatedBy(userName)
            ApiResult("success", qnInfo)
        } catch (err: MongoException) {
            ApiResult("failure", err.message)
        }
    }


    @RequestMapping("/reply", method = [RequestMethod.POST])
    fun reply(@RequestBody reply: Reply): ApiResult {
        return try {
            reply.id = ObjectId.get().toHexString()
            reply.createdDate = Date()
            val qn = qnRepo.findById(reply.questionnaireId!!).get();
            if (reply.createdDate!! > qn.endTime)
                throw RuntimeException("Questionnaire out of date!")
            if (qn.submitLimit != -1) {
                if (reply.userName != null) {
                    if (rRepo.countByQuestionnaireIdAndUserName(reply.questionnaireId, reply.userName) > qn.submitLimit!!)
                        throw RuntimeException("Submit Limit Exceeded!")
                }
                else if (reply.userIp != null) {
                    if (rRepo.countByQuestionnaireIdAndUserIp(reply.questionnaireId, reply.userIp) > qn.submitLimit!!)
                        throw RuntimeException("Submit Limit Exceeded!")
                }

            }
            for (ans in reply.answer) {
                ans.id = ObjectId.get().toHexString()
                aRepo.insert(ans)
            }
            rRepo.insert(reply)
            ApiResult("success", reply)
        } catch (err: Exception) {
            ApiResult("failure", err.message)
        }
    }

    @RequestMapping("/get_qn_replies/{questionnaireId}", method = [RequestMethod.GET])
    fun getQnReplies(@PathVariable questionnaireId: String): ApiResult {
        return try {
            ApiResult("success", rRepo.findAllByQuestionnaireId(questionnaireId))
        } catch (err: Exception) {
            ApiResult("failure", err.message)
        }
    }

    @RequestMapping("/get_reply/{replyId}", method = [RequestMethod.GET])
    fun getReply(@PathVariable replyId: String): ApiResult {
        return try {
            ApiResult("success", rRepo.findById(replyId))
        } catch (err: Exception) {
            ApiResult("failure", err.message)
        }
    }

    @RequestMapping("/delete/{id}", method = [RequestMethod.DELETE])
    fun delete(@PathVariable id: String): ApiResult {
        return try {
            val qn = qnRepo.findById(id);
            qnRepo.deleteById(id);
            rRepo.deleteAllByQuestionnaireId(id);
            if (!qn.isEmpty) {
                for (q in qn.get().questions!!) {
                    aRepo.deleteAllByQuestionId(id);
                }
            }
            ApiResult("success", "deleted")
        } catch (err: Exception) {
            ApiResult("failure", err.message)
        }
    }
}