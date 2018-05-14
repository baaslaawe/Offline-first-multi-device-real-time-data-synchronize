package io.moka.syncdemo.app.detail

import android.os.Bundle
import io.moka.base.component.BaseActivity
import io.moka.base.module.radius
import io.moka.syncdemo.R
import io.moka.syncdemo.model.dao.answer.AnswerDao
import io.moka.syncdemo.model.dao.question.QuestionDao
import io.moka.syncdemo.model.domain.Answer
import io.moka.syncdemo.model.domain.Question
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.sdk15.coroutines.onClick
import kotlin.properties.Delegates

class DetailActivity : BaseActivity() {

    private val viewModel by lazy { ViewModel() }

    companion object {
        const val KEY_QUESTION_ID = "DetailActivity/KEY_QUESTION_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initView()
        loadData()
    }

    private fun initView() {
        imageView_back.onClick { onBackPressed() }
    }

    private fun loadData() {
        val id = intent.getLongExtra(KEY_QUESTION_ID, 0)
        viewModel.question = QuestionDao.get(null, id)!!
        viewModel.answers = AnswerDao.getByQuestion(id) ?: ArrayList()
    }

    /**
     * ViewModel
     */

    inner class ViewModel {

        var question: Question by Delegates.observable(Question(),
                { _, _, new ->
                    if (new.name.isNullOrEmpty() and new.say.isNullOrEmpty()) {
                        textView_name.text = "-"
                        textView_say.text = "익명 입니다"
                        imageView_profile.radius(this@DetailActivity, R.drawable.vc_profile_gray, 28)
                    }
                    else {
                        textView_name.text = new.name
                        textView_say.text = new.say
                        imageView_profile.radius(this@DetailActivity, new.imageUrl, 28)
                    }

                    textView_question.text = "Q. ${new.question}"
                })

        var answers: List<Answer> by Delegates.observable(ArrayList(),
                { _, _, answers ->
                    var answerText = ""
                    answers.forEach {
                        answerText += it.answer
                        answerText += "\n\n"
                    }

                    if (answerText.isEmpty()) {
                        textView_answer.alpha = 0.4f
                        textView_answer.text = "답변을 기다리는 중입니다"
                    }
                    else {
                        textView_answer.alpha = 1f
                        textView_answer.text = answerText
                    }
                })
    }

}
