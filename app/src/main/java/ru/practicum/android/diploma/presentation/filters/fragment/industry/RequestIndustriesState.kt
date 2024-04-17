package ru.practicum.android.diploma.presentation.filters.fragment.industry

import ru.practicum.android.diploma.domain.models.Industry

sealed interface RequestIndustriesState {
    object Loading : RequestIndustriesState
    object Error : RequestIndustriesState
    object Empty : RequestIndustriesState

    data class Success(val data: List<Industry>) : RequestIndustriesState
}

