package ru.practicum.android.diploma.presentation.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.BottomProgressBarBinding

class ProgressBarViewHolder(
    private val binding: BottomProgressBarBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.pagingProgressBar.isVisible = loadState is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup): ProgressBarViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.bottom_progress_bar, parent, false)
            val binding = BottomProgressBarBinding.bind(view)
            return ProgressBarViewHolder(binding)
        }
    }
}
