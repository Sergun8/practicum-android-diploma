package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy1

class VacancyAdapter(
    private val clickListener: VacancyClickListener,
    private val longClickListener: LongDurationPress
) : RecyclerView.Adapter<VacancyViewHolder>() {
    private var vacancy1List: List<Vacancy1> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy, parent, false)
        return VacancyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancy1List[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(vacancy1List[position]) }
    }

    override fun getItemCount(): Int {
        return vacancy1List.size
    }

    fun interface VacancyClickListener {
        fun onTrackClick(vacancy1: Vacancy1)
    }

    fun setItems(items: List<Vacancy1>) {
        this.vacancy1List = items
        notifyDataSetChanged()
    }
    fun interface LongDurationPress {
        fun onLongClick(vacancy1: Vacancy1)
    }
}
