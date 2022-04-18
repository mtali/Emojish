package org.mtali.podmoji.ui.emoji

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mtali.podmoji.api.State
import org.mtali.podmoji.data.UsersRepo


class EmojiViewModel(private val repos: UsersRepo) : ViewModel() {

    val users = liveData {
        emit(State.loading())
        try {
            repos.getUsers().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(State.failed(e))
        }
    }

    fun updateEmojis(emojis: String) {
        viewModelScope.launch {
            repos.saveEmojis(emojis = emojis)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repo: UsersRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EmojiViewModel(repo) as T
        }
    }
}