package ru.practicum.android.diploma.presentation.search.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.Resource
import ru.practicum.android.diploma.data.Constant.STATIC_PAGE_SIZE
import ru.practicum.android.diploma.domain.filters.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Filters
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyData
import ru.practicum.android.diploma.domain.models.checkEmpty
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.presentation.search.SearchState
import ru.practicum.android.diploma.presentation.search.adapter.SearchPage
import ru.practicum.android.diploma.utils.debounce
import java.net.ConnectException

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filterInteractor: FiltersInteractor,
) : ViewModel() {
    val searchState = MutableStateFlow<SearchState>(SearchState.Start)
    var errorMessage = MutableLiveData<String?>()

    private var job: Job? = null
    private var jobFilter: Job? = null
    private val actionStateFlow = MutableSharedFlow<String>()
    private var actualSearchString = ""
    private var newState: LoadState? = null
    val stateBottomFilters = MutableStateFlow(false)
    var filters: Filters = Filters("", "", "", "", "", "", "", false)

    val stateVacancyData = actionStateFlow.flatMapLatest {
        actualSearchString = it
        getPagingData(it)
    }

    init {
        jobFilter = viewModelScope.launch(Dispatchers.IO) {
            filters = filterInteractor.getPrefs()
            stateBottomFilters.value = filters.checkEmpty()
        }
    }

    fun checkFiltersChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            if (jobFilter?.isActive != true) {
                val newFilters = filterInteractor.getPrefs()
                if (newFilters != filters) {
                    filters = newFilters
                    stateBottomFilters.value = filters.checkEmpty()
                    if (actualSearchString.isNotEmpty()) {
                        searchState.emit(SearchState.Loading)
                        actionStateFlow.emit(actualSearchString)
                    }
                }
            }
        }
    }

    suspend fun search(
        expression: String,
        page: Int,
    ): Resource<VacancyData> {
        val result = searchInteractor.search(expression, page)
        SearchState.Loaded.counter = result.data?.found ?: 0
        return result
    }

    private fun getPagingData(search: String): StateFlow<PagingData<Vacancy>> {
        return Pager(PagingConfig(pageSize = STATIC_PAGE_SIZE)) {
            SearchPage(search, ::search)
        }.flow.map {
            it
        }.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    fun search(vacancy: String) {
        if (vacancy.isNotEmpty()) {
            if (vacancy == actualSearchString) return
            searchState.value = SearchState.Search
            job = debounce(viewModelScope, job, function = {
                searchState.value = SearchState.Loading
                viewModelScope.launch(Dispatchers.IO) {
                    actionStateFlow.emit(vacancy.trim())
                }
            })
        } else {
            actualSearchString = ""
            searchState.value = SearchState.Start
            job?.cancel()
        }
    }

    fun listenerOfStates(loadState: CombinedLoadStates) {
        viewModelScope.launch(Dispatchers.Main) {
            when (val refresh = loadState.source.refresh) {
                is LoadState.Error -> when (refresh.error) {
                    is ConnectException -> searchState.value = SearchState.NoInternet
                    is NullPointerException -> searchState.value = SearchState.FailedToGetList
                    else -> searchState.value = SearchState.ServerError
                }

                LoadState.Loading -> {}
                is LoadState.NotLoading -> {
                    if (newState == LoadState.Loading) {
                        searchState.emit(SearchState.Start)
                        searchState.emit(SearchState.Loaded)
                    }
                }
            }
            newState = loadState.source.refresh

            val errorState = when {
                loadState.source.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.source.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                else -> null
            }
            when (errorState?.error) {
                is ConnectException -> errorMessage.value = "Нет интернета"
            }
        }
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}
