package com.example.slackr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView

class QuizActivity : AppCompatActivity() {

    private var btnNext: Button? = null
    private var questionTv: TextView? =null
    private var questionCountTv: TextView? =null
    private var mTactile : RadioButton? =null
    private var mVisual : RadioButton? =null
    private var mAudible : RadioButton? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val questionList= Constants.getQuestions()
        btnNext = findViewById(R.id.next_submit)
        questionTv = findViewById(R.id.question)
        mTactile = findViewById(R.id.tactile)
        mVisual = findViewById(R.id.visual)
        mAudible = findViewById(R.id.audible)
        questionCountTv = findViewById(R.id.question_number)
        var count =0
        var audibleRes=0
        var visualRes=0
        var tactileRes=0

        val que= questionList[count]
        var id= que.id
        questionCountTv!!.text= id.toString() +  " of 10"
        questionTv!!.text= que.question
        mTactile!!.text = que.tactile
        mVisual!!.text= que.visual
        mAudible!!.text= que.audible

        btnNext!!.setOnClickListener {
            //Log.i(TAG, count.toString())
            count++
            //Log.i(TAG, count.toString())

            if( mTactile!!.isChecked){
                tactileRes++
            }
            if ( mAudible!!.isChecked){
                audibleRes++
            }
            if( mVisual!!.isChecked){
                visualRes++
            }
            if(id < 10){
                val que = questionList[id]
                questionCountTv!!.text= que.id.toString() +  " of 10"
                id= que.id
                questionTv!!.text= que.question
                mTactile!!.text = que.tactile
                mVisual!!.text= que.visual
                mAudible!!.text= que.audible
            }
            if (id==10) {
                btnNext!!.text = "Submit"
                id+=1
            } else if (id==11){
                var type=""
                type = if(tactileRes >= audibleRes && tactileRes >= visualRes){
                    "Tactile"
                } else if(visualRes >= audibleRes && visualRes >= tactileRes){
                    "Visual"

                } else{
                    "Audible"
                }
                val intent= Intent(this@QuizActivity, ResultActivity::class.java)
                intent.putExtra(TYPE, type)
                Log.i(TAG, type)
                startActivity(intent)
            }
            Log.i(TAG, id.toString())
        }

    }
    companion object {
        const val TAG = "Final_Project"
        const val TYPE = "type"

    }
}