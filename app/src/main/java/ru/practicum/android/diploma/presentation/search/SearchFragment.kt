package ru.practicum.android.diploma.presentation.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.VacancyAdapter

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel by viewModel<SearchViewModel>()
    private var isClickAllowed = true
    private lateinit var vacancyAdapter: VacancyAdapter
    private lateinit var recyclerView: RecyclerView
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.getStateLiveData().observe(viewLifecycleOwner) { stateLiveData ->
            when (stateLiveData) {
                is SearchScreenState.DefaultSearch -> defaultSearch()
                is SearchScreenState.ConnectionError -> connectionError()
                is SearchScreenState.Loading -> loading()
                is SearchScreenState.NothingFound -> nothingFound()
                is SearchScreenState.SearchIsOk -> searchIsOk(stateLiveData.data)
                else -> {
                    connectionError()
                }
            }
        }
        vacancyAdapter = VacancyAdapter(
            clickListener = {
                if (isClickAllowed) {
                    clickAdapting(it)
                }
            },
            longClickListener = {})
        isClickAllowed = false
        clickDebounceManager()
        onEditorFocus()
        onSearchTextChange()
        onClearIconClick()
        clearIconVisibilityChanger()
        startSearchByEnterPress()

        recyclerView = binding.rvSearch
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = vacancyAdapter

    }

    // метод восстанавливает поисковой запрос после пересоздания
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_USER_INPUT, "")
        }
    }

    private lateinit var searchText: String

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }


    private fun clickDebounceManager() {
        GlobalScope.launch { clickDebounce() }
    }

    private suspend fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = false
        }
        return current
    }

    private fun clickAdapting(item: Vacancy) {
        Log.d("SearchFragment", "Click on the track")
        isClickAllowed = false
        val bundle = Bundle()
        bundle.putParcelable("vacancy", item)
        Log.d("vacancy", "$item")
        val navController = findNavController()
        TODO("Навигация на страницу вакансии")
        //navController.navigate("toVacancy", bundle)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search() {
        searchViewModel.searchRequesting(binding.inputEditText.text.toString())
    }
    private fun searchDebounce() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            search()
        }
    }

    private fun onEditorFocus() {
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty()) {
                searchViewModel.clearVacancyList()
            }
        }
    }

    private fun onSearchTextChange() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.inputEditText.hasFocus() && p0?.isEmpty() == true) {
                    searchViewModel.clearVacancyList()
                }
                if (binding.inputEditText.text.isNotEmpty()) {
                    searchText = binding.inputEditText.text.toString()
                    searchDebounce()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun startSearchByEnterPress() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()) {
                    searchText = binding.inputEditText.text.toString()
                    search()
                    vacancyAdapter.notifyDataSetChanged()
                }
            }
            false
        }
    }

    private fun onClearIconClick() {
        binding.clearText.setOnClickListener {
            binding.inputEditText.setText("")
            val keyboard =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.inputEditText.windowToken,
                0
            )
            binding.inputEditText.clearFocus()
            searchViewModel.clearVacancyList()
        }
    }

    private fun clearIconVisibilityChanger() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun defaultSearch() {
        recyclerView.visibility = View.VISIBLE
        binding.notInternetImage.visibility = View.GONE
        binding.errorVacancyImage.visibility = View.GONE
        Log.d("DefaultSearch", "DefaultSearch was started")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        binding.notInternetImage.visibility = View.GONE
        binding.errorVacancyImage.visibility = View.GONE
        vacancyAdapter.notifyDataSetChanged()
        Log.d("Loading", "Loading was started")
    }

    private fun searchIsOk(data: List<Vacancy>) {
        binding.progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        binding.notInternetImage.visibility = View.GONE
        binding.errorVacancyImage.visibility = View.GONE
        binding.clearText.visibility - View.GONE
        vacancyAdapter.setItems(data)
        Log.d("SearchIsOk", "Loading has been end")
    }

    private fun nothingFound() {
        binding.progressBar.visibility = View.GONE
        binding.clearText.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        binding.errorVacancyImage.visibility = View.VISIBLE
        binding.notInternetImage.visibility = View.GONE
        Log.d("NothingFound", "NothingFound")
    }

    private fun connectionError() {
        binding.progressBar.visibility = View.GONE
        binding.notInternetImage.visibility = View.VISIBLE
        binding.errorVacancyImage.visibility = View.GONE
        recyclerView.visibility = View.GONE
        Log.d("ConnectionError", "Connection Error")
    }



    companion object {
        private const val SEARCH_USER_INPUT = "SEARCH_USER_INPUT"
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
