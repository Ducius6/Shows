package com.example.ducius

import android.os.Parcel
import android.os.Parcelable

data class Show(
    val ID: Int,
    val name: String,
    val airDate: String,
    val imageId: Int,
    val description: String,
    var listOfEpisodes: MutableList<Episode>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.createTypedArrayList(Episode)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ID)
        parcel.writeString(name)
        parcel.writeString(airDate)
        parcel.writeInt(imageId)
        parcel.writeString(description)
        parcel.writeTypedList(listOfEpisodes)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Show> {
        override fun createFromParcel(parcel: Parcel): Show {
            return Show(parcel)
        }

        override fun newArray(size: Int): Array<Show?> {
            return arrayOfNulls(size)
        }
    }
}