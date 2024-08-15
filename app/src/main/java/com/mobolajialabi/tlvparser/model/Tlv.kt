package com.mobolajialabi.tlvparser.model

data class Tlv(
    val tag : String,
    val length : Int,
    val value : String,
)
