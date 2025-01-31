package com.moniapps.surefydialer.screens.recents.model

import coil3.compose.ImagePainter
import java.time.LocalTime

data class CallLogItem(
    val name: String?,
    val phoneNumber: String,
    val type: String?,
    val dateAndTime: Long?
)
