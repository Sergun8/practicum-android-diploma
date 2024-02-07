package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.DetailVacancy
import ru.practicum.android.diploma.domain.models.ErrorNetwork


class VacancyFragment : Fragment() {

    private var vacancyId: String? = null
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getVacancyDetail(vacancyId!!)

        viewModel.vacancyState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    private fun render(stateLiveData: VacancyState) {
        when (stateLiveData) {
            is VacancyState.Loading -> loading()
            is VacancyState.Content -> content(stateLiveData.vacancy)
            is VacancyState.Error -> connectionError(stateLiveData.error)
            is VacancyState.EmptyScreen -> defaultSearch()
        }
    }


    private fun initViews(vacancy: DetailVacancy) {
        vacancyId = requireArguments().getString("vacancyId")

        with(binding) {

            jobName.text = vacancy?.name

            jobSalary.text = " " // написать функцию преобразования зарплаты


            Glide.with(requireContext())
                .load(vacancy?.areaUrl)
                .placeholder(R.drawable.ic_toast)
                .fitCenter()
                .transform(RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.margin_8)))
                .into(ivCompany)



            companyName.text = vacancy.employmentName

            companyCity.text = vacancy.areaName

            neededExperience.text = vacancy.experienceName

            jobTime.text = vacancy.scheduleName

            if (vacancy.description.isNullOrEmpty()) {
                conditions.visibility = VISIBLE
                tvConditions.visibility = VISIBLE
            } else {
                var conditions = ""
                vacancy.description.forEach { condition ->
                    conditions += " ${condition}\n"
                }
                tvConditions.text = conditions
            }

            if (vacancy.keySkillsNames.isNullOrEmpty()) {
                requirements.visibility = VISIBLE
                tvRequirements.visibility = VISIBLE
            } else {
                var skills = ""
                vacancy.keySkillsNames.forEach { skill ->
                    skills += " ${skill}\n"
                }
                 tvKeySkills.text = skills
            }
            if (
                vacancy?.contactsName?.isNotEmpty() == true ||
                vacancy?.contactsEmail?.isNotEmpty() == true ||
                vacancy?.contactsPhones.toString()?.isNotEmpty() == true
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

            if (vacancy?.contactsName?.isNotEmpty() == true) {
                contactPersonData.text = vacancy.contactsName
            }

            if (vacancy?.contactsEmail?.isNotEmpty() == true) {
                contactPersonEmail.text = vacancy.contactsEmail
            }

            if (vacancy?.contactsPhones?.isNotEmpty() == true) {
                var phones = ""
                vacancy.contactsPhones.forEach { phone ->
                    phones += " ${phone}\n"
                }
                contactPersonPhoneData.text = phones
            }
        }
        }


    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE
        Log.d("Loading", "Loading was started")
    }


    private fun content(data: DetailVacancy) {
        initViews(data)
        Log.d("SearchIsOk", "Loading has been end")
    }

    private fun defaultSearch() {

    }

    private fun connectionError(error: ErrorNetwork) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}





