package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.R

class VacancyFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vacancy, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(vacancyId: String) =
            VacancyFragment().apply {
                arguments = Bundle().apply {
                    bundleOf("ARGS_VACANCY" to vacancyId)
                }

            }
    }
}
