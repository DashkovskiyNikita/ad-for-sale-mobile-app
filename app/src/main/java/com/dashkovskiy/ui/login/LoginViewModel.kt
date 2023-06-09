package com.dashkovskiy.ui.login

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkovskiy.PreferenceManager
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.repository.AuthorizationRepository
import com.dashkovskiy.ui.events.Event
import com.dashkovskiy.ui.events.EventsContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val login: String = "",
    val password: String = "",
    val isLoginSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val loginError: Boolean = false,
    val passwordError: Boolean = false
)

class LoginViewModel(
    private val authorizationRepository: AuthorizationRepository,
    private val preferenceManager: PreferenceManager,
    private val eventsContainer: EventsContainer
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    private var state: LoginState
        get() = _uiState.value
        set(newState) = _uiState.update { newState }

    fun setLogin(login: String) {
        state = state.copy(login = login)
    }

    fun setPassword(password: String) {
        state = state.copy(password = password)
    }

    fun login() {
        viewModelScope.launch {

            state = state.copy(
                loginError = TextUtils.isEmpty(state.login),
                passwordError = TextUtils.isEmpty(state.password)
            )

            if (state.loginError || state.passwordError){
                val event = Event.SnackBar(msg = "Поля не могут быть пустыми")
                eventsContainer.postEvent(event)
                return@launch
            }

            state = state.copy(isLoading = true)

            val loginResult = authorizationRepository.login(
                login = state.login,
                password = state.password
            )

            when (loginResult) {
                is NetworkResponse.Success -> {
                    with(preferenceManager) {
                        setAccessToken(loginResult.body.access)
                        setRefreshToken(loginResult.body.refresh)
                        setIsUserAuthorized(true)
                    }
                    state = state.copy(isLoginSuccess = true)
                }
                else -> {
                    val errorEvent = Event.SnackBar(msg = "Неверный логин или пароль")
                    eventsContainer.postEvent(errorEvent)
                }
            }

            state = state.copy(isLoading = false)
        }
    }
}