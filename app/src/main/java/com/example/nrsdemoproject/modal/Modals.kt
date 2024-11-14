package com.example.nrsdemoproject.modal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NRSDataModal(var state : String? = null ,
                var population : Long? = 0L ,
                var counties : Int? = 0 ,
                var detail : String? = null) : Parcelable
