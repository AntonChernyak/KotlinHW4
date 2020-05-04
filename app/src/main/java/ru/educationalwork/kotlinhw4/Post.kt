package ru.educationalwork.kotlinhw4

data class Post(
    val id: Int,
    val date: Long,
    val author: String,
    val content: String,
    var avatar: Int = 0,
    var likeStatus: Boolean = false,
    var commentStatus: Boolean = false,
    var shareStatus: Boolean = false,
    var itIsEvent: Boolean = false,
    var itIsVideo: Boolean = false,
    var commentCounter: Int = 0,
    var likeCounter: Int = 0,
    var shareCounter: Int = 0,
    var videoViewsCounter: Int = 0,
    var postLng: Double = 0.0,
    var postLat: Double = 0.0,
    val address: String = "",
    val videoPath: String = ""
)