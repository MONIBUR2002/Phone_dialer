package com.moniapps.surefydialer.screens.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moniapps.surefydialer.screens.model.NavItem

@Composable
fun BottomBarRow(
    modifier: Modifier = Modifier,
    items: List<NavItem> = emptyList(),
    selectedIndex: Int = 0,
    onClick: (Int) -> Unit,
    ) {
    NavigationBar{
        items.forEachIndexed { index, navItem ->
            NavigationBarItem(
                modifier = modifier,
                selected = index == selectedIndex,
                onClick = {
                    onClick(index)
                },
                icon = {
                    Icon(
                        modifier = modifier.size(30.dp),
                        painter = if (index == selectedIndex) painterResource(navItem.selectedIcon) else painterResource(navItem.unSelectedIcon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = navItem.title,
                        fontWeight = FontWeight.Thin
                    )
                },
            )
        }
    }
}