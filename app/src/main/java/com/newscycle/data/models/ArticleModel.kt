package com.newscycle.data.models

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ArticleModel(
    val source: Source,
    val author: String? = null,
    val title: String? = null,
    @Json(name = "description") val desc: String? = null,
    val url: String? = null,
    @Json(name = "urlToImage") val image: String? = null,
    @Json(name = "publishedAt") val pubDate: Date,
    val content: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        source = parcel.readSerializable() as Source,
        author = parcel.readString(),
        title = parcel.readString(),
        url = parcel.readString(),
        image = parcel.readString(),
        pubDate = parcel.readSerializable() as Date,
        content = parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeSerializable(source)
        dest?.writeString(author)
        dest?.writeString(title)
        dest?.writeString(url)
        dest?.writeString(image)
        dest?.writeSerializable(pubDate)
        dest?.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleModel> {
        override fun createFromParcel(parcel: Parcel): ArticleModel {
            return ArticleModel(parcel)
        }

        override fun newArray(size: Int): Array<ArticleModel?> {
            return arrayOfNulls(size)
        }
    }
}