package ru.practicum.android.diploma.data.dto.room

data class VacancyShort(
    val id: String,
    val area: String,
    val alternateUrl: String?,
    val employer: String?,
    val name: String,
    val salary: String
)
