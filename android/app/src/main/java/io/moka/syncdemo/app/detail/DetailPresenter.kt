package io.moka.syncdemo.app.detail

import io.moka.syncdemo.component.UserManager
import io.moka.syncdemo.model.dao.answer.AnswerDao
import io.moka.syncdemo.model.domain.Question

class DetailPresenter(var view: DetailActivity) {

    fun insertAnswer(question: Question, answer: String) {
        AnswerDao.insert(
                realm = null,
                insert = {
                    it.questionId = question.id
                    it.questionServerId = question.serverId
                    it.name = UserManager.currentName
                    it.answer = answer
                }
        )

        view.refreshAnswer()
    }

}