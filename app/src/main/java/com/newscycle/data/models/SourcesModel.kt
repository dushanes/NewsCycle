package com.newscycle.data.models

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourcesModel(
    val id: String? = "",
    val name: String? = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readString(),
        name = parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SourcesModel> {
        override fun createFromParcel(parcel: Parcel): SourcesModel {
            return SourcesModel(parcel)
        }

        override fun newArray(size: Int): Array<SourcesModel?> {
            return arrayOfNulls(size)
        }
    }
}