package com.stustirling.muzzchat.feature.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.stustirling.muzzchat.ui.theme.MuzzPink

@Composable
internal fun MessageInputField(
    modifier: Modifier = Modifier,
    enteredMessage: String,
    onMessageEntered: (String) -> Unit,
    onSubmitPressed: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 8.dp)
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = if (enteredMessage.isBlank()) Color.LightGray else MuzzPink,
                    focusedIndicatorColor = MuzzPink,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(50),
                value = enteredMessage,
                onValueChange = onMessageEntered)

            IconButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MuzzPink,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MuzzPink.copy(alpha = .4f)
                ),
                enabled = enteredMessage.isNotBlank(),
                onClick = onSubmitPressed
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null
                )
            }
        }
    }
}