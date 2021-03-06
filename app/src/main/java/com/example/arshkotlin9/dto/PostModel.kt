package com.example.arshkotlin9.dto

import com.example.arshkotlin9.SERVER_URL

enum class AttachmentType {
    IMAGE
}

data class AttachmentModel(val id: String, val mediaType: AttachmentType) {
    val url
        get() = "$SERVER_URL/api/v1/static/$id"
}


data class PostModel(

    val id: Long,
    val source: PostModel? = null,
    val authorName: String,
    val authorId: Long,
    val created: Int,
    val content: String? = null,
    var likes: Int = 0,
    var likedByMe: Boolean = false,
    val reposts: Int = 0,
    val repostedByMe: Boolean = false,
    val link: String? = null,
    val type: PostTypes = PostTypes.POSTBASIC,
    val attachment: AttachmentModel?
) {
    var likeActionPerforming = false
    var repostActionPerforming = false
    fun updateLikes(updatedModel: PostModel) {

        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
        likedByMe = updatedModel.likedByMe

    }
}