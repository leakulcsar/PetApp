package com.example.petapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petapp.R

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, error: String, onRetry: () -> Unit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row {

                Image(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = "error icon"
                )
                Spacer(modifier = Modifier.size(8.dp))

                Text(text = error)
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { onRetry() }
            ) {
                Text(text = stringResource(id = R.string.retry_text))

            }
        }

    }
}