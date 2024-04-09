package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun search(dto: JobSearchRequest): Response
    suspend fun doRequest(dto: Any): Response

}
