package br.edu.scl.ifsp.ads.onemessagechat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import androidx.room.Entity


@Parcelize
@Entity
// data class nao precisa de get e set
data class Message (
    var id: Int,
    var name: String,
    var message: String
): Parcelable