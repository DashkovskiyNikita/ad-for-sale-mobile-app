package com.dashkovskiy.ui.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dashkovskiy.R
import org.koin.androidx.compose.getViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = getViewModel(),
    navigateBack: () -> Unit = {}
) {
    val state by registerViewModel.uiState.collectAsStateWithLifecycle()
    val numberVisualTransformation = remember {
        PhoneVisualTransformation(mask = "+7-000-000-00-00", maskNumber = '0')
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Регистрация",
                color = Color.Black
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = state.name,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Имя",
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                },
                onValueChange = registerViewModel::setName,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = state.surname,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Фамилия",
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                },
                onValueChange = registerViewModel::setSurname,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = state.phone,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Номер телефона",
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                },
                visualTransformation = numberVisualTransformation,
                onValueChange = registerViewModel::setPhone,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = state.email,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Почта",
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                },
                onValueChange = registerViewModel::setEmail,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                value = state.password,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Пароль",
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                },
                onValueChange = registerViewModel::setPassword,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp),
                    enabled = state.registerBtnEnabled,
                    onClick = registerViewModel::register
                ) {
                    Text("Зарегистрироватся")
                }
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}