package com.moniapps.surefydialer.navigation

sealed class ScreenGraph(val route: String) {
    data object RecentScreen : ScreenGraph(route = "recent_screen")
    data object ContactScreen : ScreenGraph(route = "contact_screen")
    data object CallScreen : ScreenGraph("call_screen/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "call_screen/$phoneNumber"
    }
}