package ru.educationalwork.kotlinhw4

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val post = Post(
            1,
            1586829722,
            "Jack Sparrow",
            "Описание лень придумывать. Пусть будет пока так",
            likeStatus = true,
            likeCounter = 100,
            commentStatus = false,
            commentCounter = 0,
            shareStatus = false,
            shareCounter = 9,
            itIsEvent = true,
            postLat = 55.258,
            postLng = 37.17,
            videoViewsCounter = 2044,
            itIsVideo = true,
            videoPath = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        )

        val copyPost = post.copy()

        authorTextView.text = copyPost.author
        contentTextView.text = copyPost.content
        timeTextView.text = calculateTime(copyPost)

        actionLike(copyPost)
        actionComment(copyPost)
        actionShare(copyPost)
        itIsEvent(copyPost)
        videoPlayerInit(copyPost)
        calculateTime(copyPost)
    }

    private fun actionLike(post: Post) {
        startStatus(post.likeStatus, post.likeCounter, likeCounterTextView, likeImageView)
        // обработка нажатия
        likeImageView.setOnClickListener {
            if (!post.likeStatus) {
                // если статус отрицательный, то при клике красим картинку и текст в красный
                likeImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOn))
                likeCounterTextView.setTextColor(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOn))
                // увеличиваем счётчик на 1
                post.likeCounter += 1
                likeCounterTextView.text = post.likeCounter.toString()
                // если счётчик стал > 0, то делаем поле видимым
                if (post.likeCounter > 0) likeCounterTextView.visibility = View.VISIBLE
            } else {
                likeImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOff))
                likeCounterTextView.setTextColor(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOff))
                post.likeCounter -= 1
                likeCounterTextView.text = post.likeCounter.toString()
                if (post.likeCounter == 0) likeCounterTextView.visibility = View.INVISIBLE
            }
            // переключаем статус
            post.likeStatus = !post.likeStatus
        }
    }

    private fun actionComment(post: Post) {
        startStatus(
            post.commentStatus,
            post.commentCounter,
            commentsCounterTextView,
            commentImageView
        )
        // обработка нажатия
        commentImageView.setOnClickListener {
            if (!post.commentStatus) {
                // если статус отрицательный, то при клике красим картинку и текст в красный
                commentImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOn))
                commentsCounterTextView.setTextColor(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOn))
                // увеличиваем счётчик на 1
                post.commentCounter += 1
                commentsCounterTextView.text = post.commentCounter.toString()
                // если счётчик стал > 0, то делаем поле видимым
                if (post.commentCounter > 0) commentsCounterTextView.visibility = View.VISIBLE
            } else {
                commentImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOff))
                commentsCounterTextView.setTextColor(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOff))
                post.commentCounter -= 1
                commentsCounterTextView.text = post.commentCounter.toString()
                if (post.commentCounter == 0) commentsCounterTextView.visibility = View.INVISIBLE
            }
            // переключаем статус
            post.commentStatus = !post.commentStatus
        }
    }

    private fun actionShare(post: Post) {
        startStatus(post.shareStatus, post.shareCounter, shareTextViewCounter, shareImageView)
        // обработка нажатия
        shareImageView.setOnClickListener {
            if (!post.shareStatus) {
                // если статус отрицательный, то при клике красим картинку и текст в красный
                shareImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOn))
                shareTextViewCounter.setTextColor(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOn))
                // увеличиваем счётчик на 1
                post.shareCounter += 1
                shareTextViewCounter.text = post.shareCounter.toString()
                // если счётчик стал > 0, то делаем поле видимым
                if (post.shareCounter > 0) shareTextViewCounter.visibility = View.VISIBLE

                // передаём сообщение
                val intentShare = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT, """
                        Автор: ${post.author}. Опубликовано: ${calculateTime(post)}.
                        ${post.content}
                        """.trimIndent()
                    )
                    type = "text/plain"
                }
                    startActivity(intentShare)

            } else {
                shareImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOff))
                shareTextViewCounter.setTextColor(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOff))
                post.shareCounter -= 1
                shareTextViewCounter.text = post.shareCounter.toString()
                if (post.shareCounter == 0) shareTextViewCounter.visibility = View.INVISIBLE
            }
            // переключаем статус
            post.shareStatus = !post.shareStatus
        }
    }

    private fun actionLocation(post: Post){
        locationImageView.setOnClickListener {
            val intentLocation = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("geo:${post.postLat},${post.postLng}")
            }
            startActivity(intentLocation)
        }

    }

    private fun startStatus(
        click: Boolean,
        counter: Int,
        actionTextView: TextView,
        actionImageView: ImageView
    ) {
        // Задание начальных параметров
        if (click) actionImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLikeRepostShareActionOn))
        actionTextView.text = counter.toString()

        // Если счётчик = 0, то скрываем поле
        if (counter == 0) actionTextView.visibility = View.INVISIBLE
    }


    private fun calculateTime(post: Post) : String {
        val time: Long = System.currentTimeMillis() / 1000 - post.date
        val systemPlural: String

        systemPlural = when (time) {
            0L -> getString(R.string.just_now)
            in 1..59 -> this.resources.getQuantityString(R.plurals.plurals_sec, time.toInt(), time)
            in 60..3599 -> this.resources.getQuantityString(
                R.plurals.plurals_min,
                time.toInt() / 60,
                time / 60
            )
            in 3600..86399 -> this.resources.getQuantityString(
                R.plurals.plurals_hour,
                time.toInt() / 3600,
                time / 3600
            )
            in 86400..172799 -> getString(R.string.one_day)
            in 172_800..2_678_399 -> getString(R.string.few_days)
            in 2_678_400..5_270_399 -> getString(R.string.month)
            in 5_270_400..31_535_999 -> getString(R.string.few_months)
            in 31_536_000..63_071_999 -> getString(R.string.year)
            else -> getString(R.string.few_years)
        }

        return if (time == 0L) systemPlural
        else resources.getString(R.string.time_string, systemPlural)
    }

    // Получим адрес из координат
    private fun getAddress(lat: Double, lng: Double): Pair<String, String> {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(lat, lng, 1)

        if (addressList.size > 0) {
            val city = addressList[0].adminArea
            val country = addressList[0].countryName
            return Pair(city, country)
        }
        return Pair("", "")
    }

    // Если пост является событием
    private fun itIsEvent(post: Post){
        if (post.itIsEvent){
            // делаем видимым адрес и иконку
            addressAreaTextView.visibility = View.VISIBLE
            locationImageView.visibility = View.VISIBLE
            // заполняем адрес
            addressAreaTextView.text = getAddress(post.postLat, post.postLng).first
            addressCountryTextView.text = getAddress(post.postLat, post.postLng).second
            // обрабатываем нажатие на иконку
            actionLocation(post)
        } else {
            addressAreaTextView.visibility = View.INVISIBLE
            locationImageView.visibility = View.INVISIBLE
        }
    }

    private fun videoPlayerInit(post: Post){
        if (post.itIsVideo){
            videoViewsCounterTextView.text = resources.getString(R.string.video_views, post.videoViewsCounter)
            contentTextView.textSize = 14F
            contentTextView.maxLines = 1
            contentTextView.ellipsize = TextUtils.TruncateAt.END

            videoPlayerImageView.setOnClickListener {
                val intentVideoPlay = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoPath))
                startActivity(intentVideoPlay)

                post.videoViewsCounter += 1
                videoViewsCounterTextView.text = resources.getString(R.string.video_views, post.videoViewsCounter)
            }
        } else {
            videoPlayerImageView.visibility = View.GONE
            videoViewsCounterTextView.visibility = View.GONE
        }
    }

}