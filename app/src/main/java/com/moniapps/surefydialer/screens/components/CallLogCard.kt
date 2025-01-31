package com.moniapps.surefydialer.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.moniapps.surefydialer.R
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun CallLogCard(
    modifier: Modifier = Modifier,
    name: String?,
    phoneNumber: String,
    type: String?,
    dateAndTime: Long?,
    onCallClick: (phoneNumber: String) -> Unit = {}
) {
    val icon: ImageVector = when (type) {
        "Outgoing" -> Icons.AutoMirrored.Filled.ArrowForward
        "Incoming" -> Icons.AutoMirrored.Filled.ArrowBack
        else -> Icons.Sharp.Warning
    }

    Card(modifier =modifier.padding(vertical = 8.dp)) {
        Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(modifier = modifier.size(40.dp),imageVector = Icons.Filled.AccountCircle, contentDescription = "User")
            Column(
                modifier = Modifier
                    .weight(5f)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                (if (name == "") phoneNumber else name)?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = modifier.padding(end = 4.dp)
                            .size(20.dp)
                            .rotate(if (type == "Missed") 0f else -45f),
                        imageVector = icon,
                        contentDescription = "outgoing",
                        tint = if (type == "Missed") Color.Red else MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Mobile • ",
                        color = if (type == "Missed") Color.Red else MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = dateAndTime?.let { formatDate(it) } ?: "null",
                        color = if (type == "Missed") Color.Red else MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Icon(
                modifier = modifier.weight(1f).clickable {

                    onCallClick(phoneNumber)
                },
                imageVector = Icons.Default.Phone,
                contentDescription = "call"
            )
        }
    }
}
fun formatDate(timestamp: Long): String? {
    if (timestamp < 0) {
        return null
    }

    // Convert the timestamp to LocalDateTime
    val dateTime = LocalDateTime.ofInstant(
        Date(timestamp).toInstant(),
        ZoneId.systemDefault()
    )

    val now = LocalDateTime.now()
    val today = LocalDate.now()
    val date = dateTime.toLocalDate()
    val time = dateTime.toLocalTime()

    return when {
        date.isEqual(today) -> {
            // Today: show only the time in 12-hour format (e.g., 11:00 AM)
            formatTime(time)
        }

        date.isAfter(today.minusWeeks(1)) -> {
            // Within the last week: show day of the week and time (e.g., Sat 11:00 AM)
            formatDayAndTime(dateTime)
        }

        else -> {
            // Older than a week: show date and time (e.g., Jan 18, 10:46 PM)
            formatDateAndTime(dateTime)
        }
    }
}

/**
 * Formats a LocalTime into a 12-hour format with AM/PM.
 */
fun formatTime(time: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.current.platformLocale)
    return time.format(formatter)
}

/**
 * Formats a LocalDateTime into a day-of-week and time format (e.g., Sat 11:00 AM).
 */
fun formatDayAndTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("E h:mm a", Locale.current.platformLocale)
    return dateTime.format(formatter)
}

/**
 * Formats a LocalDateTime into a month, day, and time format (e.g., Jan 18, 10:46 PM).
 */
fun formatDateAndTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, h:mm a", Locale.current.platformLocale)
    return dateTime.format(formatter)
}

@Preview
@Composable
private fun LogCardPreview() {
    CallLogCard(
        name = "Akash",
        phoneNumber = "9876543210",
        type = "Outgoing",
        dateAndTime = 6565656L,
    )
}