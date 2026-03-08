package com.example.app_firebase.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app_firebase.R

@Composable
fun SocialLogin() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        IconButton(onClick = {}) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google",
                tint = Color.Unspecified
            )
        }

        IconButton(onClick = {}) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = "Google",
                tint = Color.Unspecified
            )
        }

        IconButton(onClick = {}) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.ic_instagram),
                contentDescription = "Google",
                tint = Color.Unspecified
            )
        }

        IconButton(onClick = {}) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.ic_twitter),
                contentDescription = "Google",
                tint = Color.Unspecified
            )
        }
    }
}