package org.mtali.podmoji.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import org.mtali.podmoji.models.User


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListItem(user: User) {
    Card(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            text = { Text(text = user.displayName) },
            trailing = {
                Text(
                    text = user.emojis,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            },
            secondaryText = {
                Text(text = "Updated: ${user.getUpdatedAt()}")
            }
        )
    }
}