package io.moka.syncdemo.app.main

import io.moka.syncdemo.component.UserManager
import io.moka.syncdemo.model.dao.answer.AnswerDao
import io.moka.syncdemo.model.dao.question.QuestionDao
import io.moka.syncdemo.util.postMain
import io.moka.syncdemo.util.workInBack

class MainPresenter(var view: MainActivity) {

    fun loadData() {
        workInBack {
            val qnaList = ArrayList<QnaAdapter.Data>()

            QuestionDao
                    .getAll()
                    ?.forEach {
                        val answer = AnswerDao.getByQuestion(it.id)
                        qnaList.add(QnaAdapter.Data(it, answer))
                    }

            postMain { view.setAdapter(qnaList) }
        }
    }

    fun insertQuestion(question: String) {
        QuestionDao.insert(
                realm = null,
                insert = {
                    it.name = UserManager.currentName
                    it.say = UserManager.currentSay
                    it.imageUrl = UserManager.currentImageUrl
                    it.question = question
                }
        )

        view.refresh()
    }

}