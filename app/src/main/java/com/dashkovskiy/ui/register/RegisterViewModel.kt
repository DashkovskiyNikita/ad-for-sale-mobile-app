package com.dashkovskiy.ui.register

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashkovskiy.PreferenceManager
import com.dashkovskiy.api.NetworkResponse
import com.dashkovskiy.api.Register
import com.dashkovskiy.repository.AuthorizationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.w3c.dom.Text

data class RegisterState(
    val name: String = "",
    val surname: String = "",
    val password: String = "",
    val phone: String = "",
    val email: String = "",
    val isLoading: Boolean = false
) {
    val registerBtnEnabled : Boolean
        get() =
            !TextUtils.isEmpty(name) &&
            !TextUtils.isEmpty(surname) &&
            !TextUtils.isEmpty(password) &&
            Patterns.PHONE.matcher(phone).matches() &&
            Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun mapToRegisterModel() =
        Register(
            name = name,
            surname = surname,
            password = password,
            phone = phone,
            email = email
        )
}

class RegisterViewModel(
    private val authorizationRepository: AuthorizationRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    private var state: RegisterState
        get() = _uiState.value
        set(newState) = _uiState.update { newState }

    fun setName(name: String) {
        state = state.copy(name = name)
    }

    fun setSurname(surname: String) {
        state = state.copy(surname = surname)
    }

    fun setPassword(password: String) {
        state = state.copy(password = password)
    }

    fun setPhone(phone: String) {
        state = state.copy(phone = phone)
    }

    fun setEmail(email: String) {
        state = state.copy(email = email)
    }

    fun register() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val registerModel = state.mapToRegisterModel()
            when (val result = authorizationRepository.register(registerModel)) {
                is NetworkResponse.Success -> {
                    with(preferenceManager) {
                        setAccessToken(result.body.access)
                        setRefreshToken(result.body.refresh)
                        setIsUserAuthorized(true)
                    }
                }
                else -> {} // todo show error
            }
            state = state.copy(isLoading = false)
        }
    }
}