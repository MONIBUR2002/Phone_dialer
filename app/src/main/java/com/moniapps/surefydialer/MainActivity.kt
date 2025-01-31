package com.moniapps.surefydialer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.moniapps.surefydialer.navigation.AppNavigation
import com.moniapps.surefydialer.navigation.ScreenGraph
import com.moniapps.surefydialer.permission.RequestPermissions
import com.moniapps.surefydialer.screens.CallScreen.CallBroadcastReceiver
import com.moniapps.surefydialer.screens.CallScreen.CallDialog
import com.moniapps.surefydialer.screens.components.BottomBarRow
import com.moniapps.surefydialer.screens.model.NavItem
import com.moniapps.surefydialer.ui.theme.SurefyDialerTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val navItemList = listOf(
            NavItem(
                selectedIcon = R.drawable.recent_filled,
                unSelectedIcon = R.drawable.recent_outlined,
                title = "Recent"
            ),
            NavItem(
                selectedIcon = R.drawable.contacts_filled,
                unSelectedIcon = R.drawable.contacts_outlined,
                title = "Contacts"
            ),
        )

        setContent {
            val navController = rememberNavController()
            CallDialog.ShowCompose(context = this, navController = navController)

            var selectedIndex by remember {
                mutableIntStateOf(0)
            }

            SurefyDialerTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.height(100.dp),
                            windowInsets = WindowInsets(top = (-10).dp)
                        ) {
                            BottomBarRow(
                                items = navItemList,
                                selectedIndex = selectedIndex,
                                onClick = {
                                    selectedIndex = it
                                    when (selectedIndex) {
                                        0 -> {
                                            navController.navigate(ScreenGraph.RecentScreen.route)
                                        }

                                        1 -> {
                                            navController.navigate(ScreenGraph.ContactScreen.route)
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    RequestPermissions()
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navHostController = navController
                    )
                }
            }
        }
    }
}
