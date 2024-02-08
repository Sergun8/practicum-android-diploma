package ru.practicum.android.diploma.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.ErrorNetwork
import ru.practicum.android.diploma.domain.models.Vacancy
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

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        vacancyAdapter = VacancyAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }

        initInputSearchForm()

        vacancyClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
            val vacancyId = it.id
            findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment, VacancyFragment.createArgs(vacancyId))
        }
        vacancyAdapter = VacancyAdapter {
            vacancyClickDebounce?.let { vacancyClickDebounce -> vacancyClickDebounce(it) }
        }
        recyclerView = binding.rvSearch
        recyclerView!!.adapter = vacancyAdapter

    }

    private fun initInputSearchForm() {
        binding.inputSearchForm.doOnTextChanged { query: CharSequence?, _, _, _ ->
            if (query.isNullOrEmpty()) {
                binding.closeImage.visibility = GONE
                binding.searchImage.visibility = VISIBLE
            } else {
                binding.closeImage.visibility = VISIBLE
                binding.searchImage.visibility = GONE
            }
            viewModel.search(query.toString())
        }

        binding.inputSearchForm.requestFocus()
        onClearIconClick()

    }

    private fun render(stateLiveData: SearchState) {
        when (stateLiveData) {
            is SearchState.Loading -> loading()
            is SearchState.SearchContent -> searchIsOk(stateLiveData.vacancys)
            is SearchState.Error -> connectionError(stateLiveData.error)
            is SearchState.EmptyScreen -> defaultSearch()
        }
    }

    private fun onClearIconClick() {
        binding.closeImage.setOnClickListener {
            binding.inputSearchForm.setText("")
            val keyboard =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.inputSearchForm.windowToken,
                0
            )
            binding.inputSearchForm.clearFocus()
        }
    }

    private fun defaultSearch() {
        recyclerView?.visibility = GONE
        binding.placeholderImage.visibility = VISIBLE
        binding.notInternetImage.visibility = GONE
        binding.errorVacancyImage.visibility = GONE
        Log.d("DefaultSearch", "DefaultSearch was started")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loading() {
        binding.progressBar.visibility = VISIBLE
        recyclerView?.visibility = GONE
        binding.placeholderImage.visibility = GONE
        binding.notInternetImage.visibility = GONE
        binding.errorVacancyImage.visibility = GONE
        vacancyAdapter?.notifyDataSetChanged()
        Log.d("Loading", "Loading was started")
    }

    private fun searchIsOk(data: List<Vacancy>) {
        binding.progressBar.visibility = GONE
        recyclerView?.visibility = VISIBLE
        binding.notInternetImage.visibility = GONE
        binding.errorVacancyImage.visibility = GONE
        binding.placeholderImage.visibility = GONE
        binding.closeImage.visibility = GONE
        vacancyAdapter?.vacancyList?.addAll(data)
        Log.d("SearchIsOk", "Loading has been end")
    }

    private fun connectionError(data: ErrorNetwork) {
        binding.progressBar.visibility = GONE
        recyclerView?.visibility = GONE
        binding.placeholderImage.visibility = GONE
        when (data) {
            ErrorNetwork.NO_CONNECTIVITY_MESSAGE -> {
                binding.notInternetImage.visibility = GONE
                binding.errorVacancyImage.visibility = GONE
                binding.tvNotInternet.visibility = VISIBLE
            }

            ErrorNetwork.NOT_FOUND -> {
                binding.notInternetImage.visibility = GONE
                binding.errorVacancyImage.visibility = VISIBLE
                binding.tvError.visibility = VISIBLE
            }

            ErrorNetwork.CAPTCHA_INPUT -> {
                binding.notInternetImage.visibility = GONE
                binding.errorVacancyImage.visibility = VISIBLE
                binding.errorVacancyImage.setImageResource(R.drawable.ic_error_server_search)
                binding.tvError.visibility = VISIBLE
                binding.tvError.text = "Капча"
            }

            ErrorNetwork.BAD_REQUEST_RESULT_CODE -> {
                binding.notInternetImage.visibility = VISIBLE
                binding.errorVacancyImage.visibility = GONE
                binding.tvError.text = "Хуевый запрос"
            }

            ErrorNetwork.SERVER_ERROR_MESSAGE -> {
                binding.notInternetImage.visibility = VISIBLE
                binding.errorVacancyImage.visibility = GONE
                binding.tvError.text = "Всякая шляпа"
            }
        }
        Log.d("ConnectionError", "Connection Error")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L


    }
}
