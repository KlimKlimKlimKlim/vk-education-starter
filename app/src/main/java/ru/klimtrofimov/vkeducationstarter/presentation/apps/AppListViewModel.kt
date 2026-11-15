package ru.klimtrofimov.vkeducationstarter.presentation.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.klimtrofimov.vkeducationstarter.domain.model.AppInfo
import ru.klimtrofimov.vkeducationstarter.domain.repository.AppsRepository
import ru.klimtrofimov.vkeducationstarter.domain.usecase.LoadRatingsUseCase

data class AppListUiState(
    val apps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val ratings: Map<String, Float> = emptyMap(),
    val ratingsLoading: RatingsMode? = null,
    val ratingsError: Boolean = false,
    val sequentialMillis: Long? = null,
    val parallelMillis: Long? = null,
)

enum class RatingsMode { Sequential, Parallel }

sealed interface AppListEffect {
    data object ShowToolbarSnackbar : AppListEffect
}

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: AppsRepository,
    private val loadRatings: LoadRatingsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppListUiState())
    val uiState = _uiState.asStateFlow()

    // Новый экран не получает старое событие после поворота или возврата.
    private val _effects = MutableSharedFlow<AppListEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effects = _effects.asSharedFlow()

    private var ratingsJob: Job? = null

    init {
        loadApps()
    }

    fun onRetryClick() {
        if (!_uiState.value.isLoading) loadApps()
    }

    private fun loadApps() {
        _uiState.value = AppListUiState()
        viewModelScope.launch {
            try {
                _uiState.value = AppListUiState(apps = repository.getApps(), isLoading = false)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.value = AppListUiState(isLoading = false, hasError = true)
            }
        }
    }

    fun onLoadRatingsClick(mode: RatingsMode) {
        val current = _uiState.value
        if (current.isLoading || current.hasError || current.apps.isEmpty() ||
            current.ratingsLoading != null || ratingsJob?.isActive == true) return
        val ids = current.apps.map { it.id }
        _uiState.update { it.copy(ratingsLoading = mode, ratingsError = false) }
        ratingsJob = viewModelScope.launch {
            try {
                var ratings: Map<String, Float> = emptyMap()
                val elapsed = measureTimeMillis {
                    ratings = when (mode) {
                        RatingsMode.Sequential -> loadRatings.loadRatingsSequentially(ids)
                        RatingsMode.Parallel -> loadRatings.loadRatingsInParallel(ids)
                    }
                }
                _uiState.update {
                    it.copy(
                        ratings = ratings,
                        ratingsLoading = null,
                        sequentialMillis = if (mode == RatingsMode.Sequential) elapsed else it.sequentialMillis,
                        parallelMillis = if (mode == RatingsMode.Parallel) elapsed else it.parallelMillis,
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.update { it.copy(ratingsLoading = null, ratingsError = true) }
            }
        }
    }

    fun onToolbarClick() {
        _effects.tryEmit(AppListEffect.ShowToolbarSnackbar)
    }
}
