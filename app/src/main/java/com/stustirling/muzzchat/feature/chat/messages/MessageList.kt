package com.stustirling.muzzchat.feature.chat.messages

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stustirling.muzzchat.feature.chat.theme.currentUserChat
import com.stustirling.muzzchat.feature.chat.theme.onCurrentUserChat
import com.stustirling.muzzchat.feature.chat.theme.onOtherUserChat
import com.stustirling.muzzchat.feature.chat.theme.otherUserChat
import com.stustirling.muzzchat.ui.theme.MuzzChatTheme
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dayFormat = DateTimeFormatter.ofPattern("eeee")

@Composable
internal fun MessageList(
    modifier: Modifier = Modifier,
    messages: List<MessageItem>
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        reverseLayout = true,
    ) {
        items(
            items = messages.reversed(),
            key = { message -> message.id }
        ) { message ->
            Column(modifier = Modifier.fillMaxWidth()) {
                if (message.showTimeHeading) {
                    TimeHeader(
                        modifier = Modifier.padding(bottom = 12.dp),
                        timestamp = message.timestamp
                    )
                }

                MessageBubble(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .align(if (message.isCurrentUser) Alignment.End else Alignment.Start),
                    messageItem = message
                )
            }

        }
    }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(messages.lastOrNull()) {
        coroutineScope.launch {
            lazyListState.animateScrollToItem(0)
        }
    }
}

@Composable
private fun TimeHeader(
    modifier: Modifier = Modifier,
    timestamp: Long
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = timeHeaderText(timestamp = timestamp),
        textAlign = TextAlign.Center,
        color = Color.LightGray
    )
}

@Composable
private fun timeHeaderText(timestamp: Long): AnnotatedString {
    val offsetDateTime = remember(timestamp) {
        Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
    }
    val context = LocalContext.current

    return buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold
            )
        ) {
            append(dayFormat.format(offsetDateTime))
        }
        append(" ")
        val timeFormatter = remember {
            if (DateFormat.is24HourFormat(context)) {
                DateTimeFormatter.ofPattern("HH:mm")
            } else {
                DateTimeFormatter.ofPattern("h:mma")
            }
        }
        append(timeFormatter.format(offsetDateTime))
    }
}

@Composable
private fun MessageBubble(
    modifier: Modifier = Modifier,
    messageItem: MessageItem
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .widthIn(max = maxWidth * .85f)
                .align(if (messageItem.isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart)
                .background(
                    color = if (messageItem.isCurrentUser) MuzzChatTheme.colors.currentUserChat()
                    else MuzzChatTheme.colors.otherUserChat(),
                    shape = bubbleShape(messageItem = messageItem)
                )
                .padding(8.dp),
            text = messageItem.content,
            color = if (messageItem.isCurrentUser) MuzzChatTheme.colors.onCurrentUserChat()
            else MuzzChatTheme.colors.onOtherUserChat()
        )
    }
}

@Composable
private fun bubbleShape(messageItem: MessageItem) = RoundedCornerShape(
    topStart = 18.dp,
    topEnd = 18.dp,
    bottomStart = if (!messageItem.isCurrentUser && messageItem.showTail) 0.dp else 18.dp,
    bottomEnd = if (messageItem.isCurrentUser && messageItem.showTail) 0.dp else 18.dp,
)

@Preview
@Composable
private fun Preview() {
    MuzzChatTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MessageList(
                modifier = Modifier.fillMaxSize(),
                messages = listOf(
                    MessageItem(
                        id = 0,
                        showTimeHeading = true,
                        showTail = true,
                        isCurrentUser = true,
                        timestamp = 0L,
                        content = "First message"
                    ),
                    MessageItem(
                        id = 1,
                        showTimeHeading = false,
                        showTail = true,
                        isCurrentUser = false,
                        timestamp = 2000L,
                        content = "Second message"
                    ),
                    MessageItem(
                        id = 2,
                        showTimeHeading = false,
                        showTail = false,
                        isCurrentUser = false,
                        timestamp = 4000L,
                        content = "Third message that is longer than the others to test the wrapping as well as the max width constraints"
                    )
                )
            )
        }

    }
}
