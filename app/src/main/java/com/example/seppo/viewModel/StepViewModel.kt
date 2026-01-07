package com.example.seppo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.seppo.reposiitory.StepRepository
import com.example.seppo.data.DailyStep
import javax.inject.Inject

@HiltViewModel
class StepViewModel @Inject constructor(
    private val repository: StepRepository
) : ViewModel() {

    // Today steps as StateFlow
    val todaySteps: StateFlow<Int> =
        repository.getTodayStepsFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    //Last 7 days
    private val _last7Days = MutableStateFlow<List<DailyStep>>(emptyList())
    val last7Days: StateFlow<List<DailyStep>> = _last7Days

    init {
        loadLast7Days()
    }

    private fun loadLast7Days() {
        viewModelScope.launch {
            _last7Days.value = repository.getLast7Days()
        }
    }

    fun resetTodaySteps() {
        viewModelScope.launch {
            repository.resetToday()
        }
    }
}
