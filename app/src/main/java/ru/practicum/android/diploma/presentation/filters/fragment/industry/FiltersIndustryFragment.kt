package ru.practicum.android.diploma.presentation.filters.fragment.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterChooseIndustryBinding
import ru.practicum.android.diploma.presentation.filters.fragment.industry.recycleview.IndustriesAdapter
import ru.practicum.android.diploma.presentation.filters.fragment.industry.recycleview.IndustriesAdapterItem
import ru.practicum.android.diploma.presentation.filters.state.industry.FiltersIndustryState
import ru.practicum.android.diploma.presentation.filters.state.industry.RequestIndustriesState
import ru.practicum.android.diploma.presentation.filters.viewmodel.industry.FiltersIndustryViewModel
import ru.practicum.android.diploma.presentation.search.fragment.gone
import ru.practicum.android.diploma.presentation.search.fragment.visible

class FiltersIndustryFragment : Fragment() {
    private var _binding: FragmentFilterChooseIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FiltersIndustryViewModel>()
    private var adapter = IndustriesAdapter { industry ->
        binding.buttonPick.isVisible = industry.active.isActive
        viewModel.setCurrentIndustry(industry.industry)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterChooseIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getIndustries()

        binding.industryList.adapter = this.adapter
        binding.choosingIndustry.doOnTextChanged { text, _, _, _ ->
            viewModel.filterIndustries(text.toString())
            if (text.isNullOrBlank()) {
                binding.clearButton.gone()
                binding.searchDrawable.visible()
                if (adapter.checkedRadioButtonId != -1) {
                    binding.buttonPick.visible()
                }
            } else {
                binding.clearButton.visible()
                binding.searchDrawable.gone()
                binding.clearButton.setOnClickListener {
                    binding.choosingIndustry.text?.clear()
                }
            }
        }

        binding.arrowBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonPick.setOnClickListener {
            val industry = when (val state = viewModel.industryState.value) {
                is FiltersIndustryState.CurrentIndustry -> state.industry
                else -> null
            }
            if (industry != null) {
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(INDUSTRY_KEY to industry))
                findNavController().popBackStack()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                RequestIndustriesState.Error -> {
                    showError()
                }

                RequestIndustriesState.Empty -> {
                    showEmpty()
                }

                RequestIndustriesState.Loading -> {
                    showLoading()
                }

                is RequestIndustriesState.Success -> {
                    showContent(state.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showContent(list: List<IndustriesAdapterItem>) {
        val currentIndustry = when (val state = viewModel.industryState.value) {
            is FiltersIndustryState.CurrentIndustry -> state.industry
            else -> null
        }
        val active = list.find { it.industry.id == currentIndustry?.id }
        if (active == null) {
            binding.buttonPick.isVisible = false
        } else {
            active.active.isActive = true
            binding.buttonPick.isVisible = true
        }
        adapter.updateList(list)

        if (currentIndustry == null) {
            val idPrefs = arguments?.getString(INDUSTRY_KEY)
            adapter.setSelectedIndustry(idPrefs)
            viewModel.setIndustryId(idPrefs)
            viewModel.setIndustryIndex(adapter.checkedRadioButtonId)

            if (!idPrefs.isNullOrEmpty()) {
                val position = adapter.data.indexOfFirst { it.industry.id == idPrefs }
                if (position != -1) {
                    binding.buttonPick.visible()
                    viewModel.setCurrentIndustry(adapter.data[position].industry)
                }
            }
        }

        binding.industryList.visible()
        binding.progressBar.gone()
        binding.errorHolder.gone()
    }

    private fun showEmpty() {
        binding.buttonPick.gone()
        binding.industryList.gone()
        binding.progressBar.gone()

        binding.errorsImage.setImageResource(R.drawable.ph_nothing_found)
        binding.errorsText.setText(R.string.industry_not_found)
        binding.errorHolder.visible()
    }

    private fun showError() {
        binding.buttonPick.gone()
        binding.industryList.gone()
        binding.progressBar.gone()

        binding.errorHolder.visible()
    }

    private fun showLoading() {
        binding.progressBar.visible()
        binding.buttonPick.gone()
        binding.industryList.gone()
        binding.errorHolder.gone()
    }

    companion object {
        const val REQUEST_KEY = "KEY"
        const val INDUSTRY_KEY = "INDUSTRY"
    }
}
