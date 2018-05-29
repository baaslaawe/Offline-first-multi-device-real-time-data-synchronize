package io.moka.syncdemo.app.detail

import android.os.Bundle
import io.moka.base.component.BaseActivity
import io.moka.base.module.radius
import io.moka.mokabaselib.util.gone
import io.moka.mokabaselib.util.visible
import io.moka.syncdemo.R
import io.moka.syncdemo.app.detail.dialog.PostAnswerDialog
import io.moka.syncdemo.model.dao.answer.AnswerDao
import io.moka.syncdemo.model.dao.question.QuestionDao
import io.moka.syncdemo.model.domain.Answer
import io.moka.syncdemo.model.domain.Question
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.sdk15.coroutines.onClick
import kotlin.properties.Delegates

class DetailActivity : BaseActivity() {

    private val presenter by lazy { DetailPresenter(this) }
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

    /*
     */

    private fun initView() {
        imageView_back.onClick { onBackPressed() }
        floatingActionButton_reply.onClick { onClickToReply() }
    }

    private fun loadData() {
        val id = intent.getLongExtra(KEY_QUESTION_ID, 0)
        viewModel.question = QuestionDao.get(null, id)!!
        viewModel.answers = AnswerDao.getByQuestion(id) ?: ArrayList()
    }

    /*
     */

    private fun onClickToReply() {
        PostAnswerDialog()
                .showDialog(supportFragmentManager, { text: String ->
                    presenter.insertAnswer(viewModel.question, text)
                })
    }

    /*
     */

    fun refreshAnswer() {
        viewModel.answers = AnswerDao.getByQuestion(viewModel.question.id) ?: ArrayList()
    }

    /**
     * ViewModel
     */

    inner class ViewModel {

        var question: Question by Delegates.observable(Question(),
                { _, _, newQuestion ->
                    if (newQuestion.name.isNullOrEmpty() and newQuestion.say.isNullOrEmpty()) {
                        textView_name.text = "-"
                        textView_say.text = "익명 입니다"
                        imageView_profile.radius(this@DetailActivity, R.drawable.vc_profile_gray, 28)
                    }
                    else {
                        textView_name.text = newQuestion.name
                        textView_say.text = newQuestion.say
                        if (newQuestion.imageUrl.isNullOrEmpty())
                            imageView_profile.radius(this@DetailActivity, R.drawable.vc_profile_gray, 28)
                        else
                            imageView_profile.radius(this@DetailActivity, newQuestion.imageUrl, 28)
                    }

                    textView_question.text = "Q. ${newQuestion.question}"
                })

        var answers: List<Answer> by Delegates.observable(ArrayList(),
                { _, _, answers ->
                    var answerText = ""
                    answers.forEach {
                        answerText += it.answer
                        answerText += "\n\n\n"
                    }

                    if (answerText.isEmpty()) {
                        textView_answer_title.gone()
                        textView_answer.alpha = 0.4f
                        textView_answer.text = "답변을 기다리는 중입니다"
                    }
                    else {
                        textView_answer_title.visible()
                        textView_answer_title.text = "총 ${answers.size}개의 답변이 있습니다"
                        textView_answer.alpha = 1f
                        textView_answer.text = answerText
                    }
                })
    }

}
