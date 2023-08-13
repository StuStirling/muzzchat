package com.stustirling.muzzchat.feature.chat.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun MessageListContainer(
    modifier: Modifier = Modifier,
    messages: List<MessageItem>
) {
    Box(
        modifier = modifier
    ) {
        MessageList(
            modifier = Modifier
                .fillMaxSize(),
            messages = messages
        )

        Shadow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .rotate(180f)
        )

        Shadow(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .rotate(0f)
        )
    }
}

@Composable
fun Shadow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = .1f),
                        Color.Transparent,
                    )
                )
            )
    )
}