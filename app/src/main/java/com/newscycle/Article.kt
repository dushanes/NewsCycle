package com.newscycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.newscycle.data.models.ArticleModel
import com.newscycle.databinding.FragmentArticleBinding
import java.text.SimpleDateFormat
import java.util.*

class Article(private val article: ArticleModel) : DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentArticleBinding = DataBindingUtil.inflate<FragmentArticleBinding>(
            layoutInflater,
            R.layout.fragment_article, null, false
        )
        binding.article = article
        binding.articleTime.text = getFormattedTime(article.pubDate)
        binding.articleTitle.text = removeSource(article.title, '-')
        binding.articleContent.text = removeSource(article.content, '[')
        //binding.articleCardView.setOnTouchListener(SwipeUpEventListener(this))
        //binding.articleScrollview.setOnTouchListener(SwipeUpEventListener(this))

        Glide.with(this)
            .load(article.image)
            .into(binding.articleImage)
        return binding.root
    }

    /*
    private var downX = 0f
    private var downY = 0f
    private var upX = 0f
    private var upY = 0f
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        if (event != null && v != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    //action = SwipeUpEventListener.Action.None
                    return false // allow other events like Click to be processed
                }
                MotionEvent.ACTION_UP -> {
                    upX = event.x
                    upY = event.y
                    val deltaX = downX - upX
                    val deltaY = downY - upY

                    // horizontal swipe detection
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            Log.i(logTag, "Swipe Left to Right")
                            //action = SwipeUpEventListener.Action.LR
                            return true
                        }
                        if (deltaX > 0) {
                            Log.i(logTag, "Swipe Right to Left")
                            //a//ction = SwipeUpEventListener.Action.RL
                            return true
                        }
                    } else if (Math.abs(deltaY) > MIN_DISTANCE) { // vertical swipe
                        // detection
                        // top or down
                        if (deltaY < 0) {
                            Log.i(logTag, "Swipe Top to Bottom")
                            //action = SwipeUpEventListener.Action.TB
                            dismiss()
                            return true
                        }
                        if (deltaY > 0) {
                            Log.i(logTag, "Swipe Bottom to Top")
                            //action = SwipeUpEventListener.Action.BT
                            dismiss()
                            return true
                        }
                    }
                    return true
                }
            }
        }
        return false
    }

     */

    fun removeSource(title: String?, remove: Char): String {
        if(title.isNullOrBlank()) return ""
        val size = title.length
        for(i in size-1 downTo 0){
            val char = title[i]
            if(char == remove){
                val ret = title.removeRange(i, size)
                return ret
            }
        }
        return ""
    }

    fun getFormattedTime(pubDate: Date?): String {
        if (pubDate == null) return ""
        return SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH).format(pubDate)
    }

    companion object {
        private const val logTag = "SwipeDetector"
        private const val MIN_DISTANCE = 50
    }
}