package org.wit.golfpoi.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GolfPOIModel(var id: Long = 0,
                        var courseTitle: String = "",
                        var courseDescription: String = "",
                        var courseProvince: String = "") : Parcelable
