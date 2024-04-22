package ru.practicum.android.diploma.presentation.filters.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.CountryItemBinding
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.presentation.filters.adapter.viewholder.FilterViewHolder

class FilterAdapter(private val clickListener: CountryClickListener) : RecyclerView.Adapter<FilterViewHolder>() {
    var countriesList = ArrayList<Country>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FilterViewHolder(
            CountryItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(countriesList[position])
        holder.itemView.setOnClickListener { clickListener.onCountryClick(countriesList[position]) }
    }

    override fun getItemCount(): Int {
        return countriesList.size
    }

    fun interface CountryClickListener {
        fun onCountryClick(country: Country)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newList: List<Country>) {
        countriesList = newList as ArrayList<Country>
        notifyDataSetChanged()
    }

    fun getItemByPosition(position: Int) =
        countriesList[position]

}
