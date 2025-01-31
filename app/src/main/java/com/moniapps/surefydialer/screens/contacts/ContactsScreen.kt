package com.moniapps.surefydialer.screens.contacts

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.moniapps.surefydialer.screens.components.ContactCard
import com.moniapps.surefydialer.screens.recents.makeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val allContacts = remember { mutableStateListOf<Contact>() }
    var displayedContacts by remember { mutableStateOf(listOf<Contact>()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                allContacts.clear()
                allContacts.addAll(getAllContacts(context))
                displayedContacts = allContacts.toList() // Initialize displayedContacts
            } catch (e: Exception) {
                Log.e("ContactScreen", "Error fetching contacts", e)
            } finally {
                isLoading = false
            }
        }
    }

    // Function to filter contacts based on search text
    fun filterContacts(text: String) {
        displayedContacts = if (text.isBlank()) {
            allContacts.toList()
        } else {
            allContacts.filter {
                it.name?.contains(text, ignoreCase = true) ?: false
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Contacts") },
                )

            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            filterContacts(it)
                        },
                        label = { Text("Search Contacts") },
                        modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(.9f)
                    )
                    LazyColumn {
                        items(items = displayedContacts) { item ->
                            Log.d("ContactScreen", "Contact: ${item.name} ${item.phoneNumber}")
                            item.name?.let { name ->
                                ContactCard(
                                    name = name,
                                    onCallClick = {
                                        item.phoneNumber?.let { phoneNumber ->
                                            makeCall(context, phoneNumber)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }


        }

    }
}

data class Contact(
    val id: String?,
    val name: String?,
    val phoneNumber: String?
)

suspend fun getAllContacts(context: Context): List<Contact> = withContext(Dispatchers.IO) {
    val contacts = mutableListOf<Contact>()

    // Check for READ_CONTACTS permission
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return@withContext emptyList()
    }

    val contentResolver: ContentResolver = context.contentResolver

    // Query for contacts
    val cursor: Cursor? = contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        null,
        null,
        null,
        null
    )

    cursor?.use { contactCursor ->
        if (contactCursor.count > 0) {
            while (contactCursor.moveToNext()) {
                val id =
                    contactCursor.getString(contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name =
                    contactCursor.getString(contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber =
                    contactCursor.getInt(contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                var phoneNumber: String? = null // Initialize as null

                if (hasPhoneNumber > 0) {
                    // Get the first phone number
                    val phoneCursor: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                        arrayOf(id),
                        null
                    )

                    phoneCursor?.use { phoneIt ->
                        if (phoneIt.moveToFirst()) { // Only get the first phone number
                            phoneNumber =
                                phoneIt.getString(phoneIt.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                    }
                }

                // Add contact only if name is not null and phone number is available
                if (!name.isNullOrEmpty()) {
                    contacts.add(Contact(id, name, phoneNumber))
                }
            }
        }
    }

    return@withContext contacts
}
