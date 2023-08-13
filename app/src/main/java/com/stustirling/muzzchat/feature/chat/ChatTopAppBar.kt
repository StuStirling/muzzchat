package com.stustirling.muzzchat.feature.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatTopAppBar(
    onNavigateUp: () -> Unit,
    state: ChatScreenState,
) {
    val title = (state as? ChatScreenState.Content)?.recipient?.name
    val image = (state as? ChatScreenState.Content)?.recipient?.imageUrl
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                image?.let {
                    AsyncImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(36.dp)
                            .padding(end = 8.dp),
                        model = it,
                        contentDescription = null
                    )
                }
                Text(text = title.orEmpty())
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Image(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}