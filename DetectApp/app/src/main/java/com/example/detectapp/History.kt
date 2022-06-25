package com.example.detectapp

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class History() : Parcelable {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("image")
    var image: String = ""
    @SerializedName("prediction")
    var prediction: String = ""
    @SerializedName("message")
    var message: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        image = parcel.readString().toString()
        prediction = parcel.readString().toString()
        message = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(prediction)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<History> {
        override fun createFromParcel(parcel: Parcel): History {
            return History(parcel)
        }

        override fun newArray(size: Int): Array<History?> {
            return arrayOfNulls(size)
        }
    }
}
