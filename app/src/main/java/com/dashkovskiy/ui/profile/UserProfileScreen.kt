package com.dashkovskiy.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dashkovskiy.R
import org.koin.androidx.compose.getViewModel

@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = getViewModel(),
    navigateToLoginScreen: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isUserAuthorized) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                painter = painterResource(R.drawable.baseline_account_circle_24),
                tint = Color.Black,
                contentDescription = null
            )
            Text(
                text = state.fullName,
                color = Color.Black
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = state.userInfo?.email.orEmpty(),
                singleLine = true,
                onValueChange = { },
                placeholder = {
                    Text(
                        text = "Email",
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = state.userInfo?.phone.orEmpty(),
                singleLine = true,
                onValueChange = { },
                placeholder = {
                    Text(
                        text = "Телефон",
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Вы не авторизованы")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .width(200.dp)
                    .padding(horizontal = 48.dp),
                onClick = navigateToLoginScreen
            ) {
                Text("Войти")
            }
        }
    }
}