package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.util.ConvertSalary

class VacancyFragment : Fragment() {

    private var vacancyId: String? = null
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel<VacancyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
        vacancyId = requireArguments().getString(ARGS_VACANCY)
        viewModel.getVacancyDetail(vacancyId!!)

        viewModel.vacancyState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(stateLiveData: VacancyState) {
        when (stateLiveData) {
            is VacancyState.Loading -> loading()
            is VacancyState.Content -> content(stateLiveData.vacancy)
            is VacancyState.Error -> connectionError()
            is VacancyState.EmptyScreen -> defaultSearch()
        }
    }

    private fun initViews(vacancy: DetailVacancy) {
        with(binding) {
            jobName.text = vacancy.name
            jobSalary.text =
                ConvertSalary().formatSalaryWithCurrency(
                    vacancy.salaryFrom,
                    vacancy.salaryTo,
                    vacancy.salaryCurrency
                )

            Glide.with(requireContext())
                .load(vacancy.areaUrl)
                .placeholder(R.drawable.ic_toast)
                .fitCenter()
                .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.margin_8)))
                .into(ivCompany)

            companyName.text = vacancy.employerName
            companyCity.text = vacancy.areaName
            jobTime.text = vacancy.scheduleName
            if (vacancy.experienceName == null) {
                neededExperience.visibility = GONE
                yearsOfExperience.visibility = GONE
            } else {
                neededExperience.visibility = VISIBLE
                yearsOfExperience.visibility = VISIBLE
                yearsOfExperience.text = vacancy.experienceName

            }

            createContacts(vacancy)
            createDiscription(vacancy.description)
            createKeySkills(vacancy.keySkillsNames)

        }
    }

    fun createContacts(vacancy: DetailVacancy) {
        with(binding) {
            contactPersonData.text = vacancy.contactsName
            contactPersonEmail.text = vacancy.contactsEmail

            if (
                vacancy.contactsName?.isNotEmpty() == true ||
                vacancy.contactsEmail?.isNotEmpty() == true || vacancy.contactsPhones.toString().isNotEmpty()
            ) {
                contactInformation.visibility = VISIBLE
                contactPerson.visibility = VISIBLE
                contactPersonData.visibility = VISIBLE
                contactPersonEmail.visibility = VISIBLE
                contactPersonPhone.visibility = VISIBLE
                contactPersonPhone.visibility = VISIBLE
                contactPersonPhoneData.visibility = VISIBLE
                contactComment.visibility = VISIBLE
                contactCommentData.visibility = VISIBLE
            }

            if (vacancy.contactsName?.isNotEmpty() == true) {
                contactPersonData.text = vacancy.contactsName
            }

            if (vacancy.contactsEmail?.isNotEmpty() == true) {
                contactPersonEmail.text = vacancy.contactsEmail
            }

            if (vacancy.contactsPhones?.isNotEmpty() == true) {
                var phones = ""
                vacancy.contactsPhones.forEach { phone ->
                    phones += " ${phone}\n"
                }
                contactPersonPhoneData.text = phones
            }
        }

    }

    private fun createDiscription(description: String?) {
        binding.tvDescription.text = HtmlCompat.fromHtml(
            description?.replace(Regex("<li>\\s<p>|<li>"), "<li>\u00A0") ?: "",
            HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM
        )
    }

    private fun createKeySkills(keySkills: List<String?>) {
        with(binding) {
            if (keySkills.isEmpty()) {
                keySkillsRecyclerView.visibility = GONE
                binding.keySkills.visibility = GONE
            } else {
                var skills = ""
                keySkills.forEach { skill ->
                    skills += "- ${skill}\n"
                }
                keySkillsRecyclerView.text = skills
            }
        }

    }

    private fun loading() {
        binding.progressBar.visibility = VISIBLE

    }

    private fun content(data: DetailVacancy) {
        binding.progressBar.visibility = GONE
        initViews(data)

    }

    private fun defaultSearch() {
        binding.progressBar.visibility = GONE
    }

    private fun connectionError() {
        binding.progressBar.visibility = GONE
        with(binding) {
            tvServerError.visibility = VISIBLE
            ivServerError.visibility = VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARGS_VACANCY = "vacancyId"
        fun createArgs(vacancyId: String): Bundle =
            bundleOf(ARGS_VACANCY to vacancyId)

    }
}
