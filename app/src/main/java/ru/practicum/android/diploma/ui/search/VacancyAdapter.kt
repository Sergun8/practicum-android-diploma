package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Paginaciya

class VacancyAdapter(private val clickListener: VacancyClickListener) : RecyclerView.Adapter<VacancyViewHolder>() {
    var vacancyList = ArrayList<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy, parent, false)
        return VacancyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancyList[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(vacancyList[position]) }
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    fun interface VacancyClickListener {
        fun onTrackClick(vacancy: Vacancy)
    }

    // переделать
    fun setVacancies(newVacancies: List<Vacancy>?) {
        val diffCallback = Paginaciya(vacancyList, newVacancies ?: emptyList())
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        vacancyList.clear()
        if (!newVacancies.isNullOrEmpty()) {
            vacancyList.addAll(newVacancies)
        }
        diffResult.dispatchUpdatesTo(this)
    }

}
