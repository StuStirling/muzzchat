package com.stustirling.muzzchat.feature.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.stustirling.muzzchat.R
import com.stustirling.muzzchat.feature.chat.theme.currentUserChat
import com.stustirling.muzzchat.feature.chat.theme.otherUserChat
import com.stustirling.muzzchat.ui.theme.MuzzChatTheme
import com.stustirling.muzzchat.ui.theme.MuzzPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatTopAppBar(
    onNavigateUp: () -> Unit,
    state: ChatScreenState,
    onSwitchAuthor: () -> Unit
) {

    TopAppBar(
        title = {
            TitleContent(state = (state as? ChatScreenState.Content))
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Image(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            if (state is ChatScreenState.Content) {
                SwitchAuthorToggle(
                    onClick = onSwitchAuthor,
                    currentUserIsAuthor = state.currentAuthor.isCurrentUser
                )
            }
        }
    )
}

@Composable
private fun TitleContent(
    state: ChatScreenState.Content?
) {
    val title = state?.otherUser?.name
    val image = state?.otherUser?.imageUrl

    Row(verticalAlignment = Alignment.CenterVertically) {
        image?.let {
            AsyncImage(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape)
                    .background(MuzzPink)
                    .size(36.dp),
                model = it,
                contentDescription = stringResource(id = R.string.feature_chat_cd_other_user_avatar)
            )
        }
        Text(text = title.orEmpty())
    }
}

@Composable
private fun SwitchAuthorToggle(
    onClick: () -> Unit,
    currentUserIsAuthor: Boolean
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (currentUserIsAuthor) Icons.Filled.Person else Icons.Outlined.Person,
            tint = if (currentUserIsAuthor) MuzzChatTheme.colors.currentUserChat()
            else MuzzChatTheme.colors.otherUserChat(),
            contentDescription = stringResource(id = R.string.feature_chat_cd_switch_author),
        )
    }
}