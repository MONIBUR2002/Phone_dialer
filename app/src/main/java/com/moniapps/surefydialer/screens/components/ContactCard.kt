package com.moniapps.surefydialer.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactCard(modifier: Modifier = Modifier, name: String, onCallClick: () -> Unit) {
    Card(modifier = modifier.padding(8.dp)) {
        Row(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = modifier.weight(1f).size(30.dp),
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
            Text(
                modifier = modifier.weight(4f),
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            IconButton(onClick = {
                onCallClick()
            }) {
                Icon(
                    modifier = modifier.weight(1f),
                    imageVector = Icons.Default.Phone,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun contactCardPreview() {
    ContactCard(name = "Anna", onCallClick = {})
}