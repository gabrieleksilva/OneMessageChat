package br.edu.scl.ifsp.ads.onemessagechat.model

import android.os.Parcelable
import androidx.annotation.NonNull
import kotlinx.android.parcel.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.scl.ifsp.ads.onemessagechat.model.Constant.INVALID_CONTACT_ID


@Parcelize
@Entity
// data class nao precisa de get e set
data class Message (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = INVALID_CONTACT_ID,
    @NonNull
    var name: String = "",
    @NonNull
    var message: String = ""
): Parcelable