package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
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

        vacancyAdapter = VacancyAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }

        vacancyClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            val bundle = bundleOf("vacancy" to it.id)
            findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment, bundle)
        }
        binding.inputSearchForm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ничего не делаем
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchText = s.toString()
                viewModel.searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // Ничего не делаем
            }
        })
        vacancyAdapter = VacancyAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }
        recyclerView = binding.rvSearch
        recyclerView!!.adapter = vacancyAdapter
        binding.searchImage.setOnClickListener {
            viewModel.search()
        }
        scrolling(vacancyAdapter)
        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Content -> {
                    vacancyAdapter?.setItems(state.vacancies)
                }

                else -> {}
            }

        }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
