package ru.practicum.android.diploma.ui.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarVacanciesBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.adapter.VacancyAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.debounce

class SimilarVacanciesFragment : Fragment() {
    private var _binding: FragmentSimilarVacanciesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SimilarViewModel>()
    private var vacancyClickDebounce: ((Vacancy) -> Unit)? = null
    private var vacancyAdapter: VacancyAdapter? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarVacanciesBinding.inflate(inflater, container, false)
        binding.similarToolbars.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.vacancyState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        initialAdapter()
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

    private fun render(stateLiveData: SimilarState) {
        when (stateLiveData) {
            is SimilarState.Loading -> loading()
            is SimilarState.Content -> searchIsOk(stateLiveData.vacancies)
            is SimilarState.Error -> error()
        }
    }
    private fun loading() {
        with(binding) {
            progressBar.visibility = VISIBLE
            rvSearch.visibility = GONE

        }
    }

    private fun error() {
        with(binding) {
            notInternetImage.visibility = VISIBLE
            tvNotInternet.visibility = VISIBLE
            progressBar.visibility = GONE
            progressBar.visibility = GONE
            tvError.visibility = GONE
            errorVacancyImage.visibility = GONE
        }
    }

    private fun searchIsOk(data: List<Vacancy>) {
        binding.progressBar.visibility = GONE
        recyclerView?.visibility = VISIBLE
        binding.notInternetImage.visibility = GONE
        binding.errorVacancyImage.visibility = GONE
        vacancyAdapter?.setItems(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARGS_VACANCY = "vacancyId"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(vacancyId: String): Bundle =
            bundleOf(ARGS_VACANCY to vacancyId)

    }
}
