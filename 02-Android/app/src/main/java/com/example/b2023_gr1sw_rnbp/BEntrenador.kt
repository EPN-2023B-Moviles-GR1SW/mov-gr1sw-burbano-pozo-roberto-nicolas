package com.example.b2023_gr1sw_rnbp


import android.os.Parcel
import android.os.Parcelable
class BEntrenador(
    var id: Int,
    var nombre: String?,
    var descripcion: String?
): Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BEntrenador> {
        override fun createFromParcel(parcel: Parcel): BEntrenador {
            return BEntrenador(parcel)
        }

        override fun newArray(size: Int): Array<BEntrenador?> {
            return arrayOfNulls(size)
        }
    }

    constructor (parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }
}
