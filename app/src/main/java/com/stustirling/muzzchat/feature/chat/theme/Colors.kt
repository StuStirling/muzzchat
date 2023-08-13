package com.stustirling.muzzchat.feature.chat.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import com.stustirling.muzzchat.ui.theme.MuzzPink
import com.stustirling.muzzchat.ui.theme.VeryLightGrey

internal fun Colors.currentUserChat() = MuzzPink
internal fun Colors.otherUserChat() = VeryLightGrey

internal fun Colors.onCurrentUserChat() = Color.White
internal fun Colors.onOtherUserChat() = Color.Black