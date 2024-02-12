package ru.practicum.android.diploma.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.VacancyAdapter
import ru.practicum.android.diploma.ui.search.state.SearchState
import ru.practicum.android.diploma.ui.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()
    private var vacancyClickDebounce: ((Vacancy) -> Unit)? = null
    private var vacancyAdapter: VacancyAdapter? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputSearchForm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // ничего не делаем
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchText = s.toString()
                viewModel.searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank()) {
                    viewModel.searchDebounce()
                    stateClearEditText()
                } else {
                    binding.searchImage.visibility = View.GONE
                    binding.clearButton.visibility = View.VISIBLE
                }
            }
        })

        binding.searchImage.setOnClickListener {
            viewModel.search()
        }
        clearEditText()
        initialAdapter()
        scrolling(vacancyAdapter)
        render()
    }

    private fun scrolling(adapter: VacancyAdapter?) {
        binding.rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos =
                        (binding.rvSearch.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter?.itemCount
                    if (itemsCount != null && pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })
    }

    private fun render() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Content -> {
                    vacancyAdapter?.setItems(state.vacancies)
                }

                is SearchState.FailedToGetList -> stateFailedLoaded()
                is SearchState.Loading -> stateLoading()
                is SearchState.Loaded -> stateLoaded()
                is SearchState.NextPageLoading -> {
                    binding.progressBar.visibility = View.GONE
                    binding.bottomprogressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                }

                is SearchState.NoInternet -> {
                    stateNoInternet()
                }

                else -> {}
            }

        }
    }

    private fun initialAdapter() {
        vacancyAdapter = VacancyAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }

        vacancyClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            val vacancyId = it.id
            findNavController().navigate(
                R.id.action_searchFragment_to_vacancyFragment,
                VacancyFragment.createArgs(vacancyId)
            )
        }

        vacancyAdapter = VacancyAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }
        recyclerView = binding.rvSearch
        recyclerView!!.adapter = vacancyAdapter
    }

    private fun stateLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            rvSearch.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            bottomprogressBar.visibility = View.GONE
            keyboardGone()
        }
    }

    private fun stateLoaded() {
        with(binding) {
            progressBar.visibility = View.GONE
            tvError.visibility = View.GONE
            errorVacancyImage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            rvSearch.visibility = View.VISIBLE
        }
    }

    private fun stateNoInternet() {
        with(binding) {
            notInternetImage.visibility = View.VISIBLE
            tvNotInternet.visibility = View.VISIBLE
            bottomprogressBar.visibility = View.GONE
            progressBar.visibility = View.GONE
            tvError.visibility = View.GONE
            errorVacancyImage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
        }
    }

    private fun stateFailedLoaded() {
        with(binding) {
            tvError.visibility = View.VISIBLE
            errorVacancyImage.visibility = View.VISIBLE
            rvSearch.visibility = View.GONE
            placeholderImage.visibility = View.GONE
        }
    }

    private fun stateClearEditText() {
        with(binding) {
            progressBar.visibility = View.GONE
            notInternetImage.visibility = View.GONE
            tvNotInternet.visibility = View.GONE
            tvError.visibility = View.GONE
            errorVacancyImage.visibility = View.GONE
            rvSearch.visibility = View.GONE
            clearButton.visibility = View.GONE
            searchImage.visibility = View.VISIBLE
            viewModel.currentVacanciesList = emptyList()
            placeholderImage.visibility = View.VISIBLE
            keyboardVisible()
        }
    }

    private fun clearEditText() {
        binding.clearButton.setOnClickListener {
            binding.inputSearchForm.setText("")
            stateClearEditText()
        }
    }

    fun keyboardVisible() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.inputSearchForm, 0)
    }

    fun keyboardGone() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.fragmentSearch.windowToken, 0)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
