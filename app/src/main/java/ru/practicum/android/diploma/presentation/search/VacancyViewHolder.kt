package ru.practicum.android.diploma.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy1

class VacancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
    private val tvCompanyName: TextView = itemView.findViewById(R.id.tv_name_company)
    private val tvSalary: TextView = itemView.findViewById(R.id.tv_salary)
    private val ivUrl100: ImageView = itemView.findViewById(R.id.iv_logo)

    fun bind(item: Vacancy1) {
        tvDescription.text = item.name
        tvCompanyName.text = item.employmentName
        item.salaryCurrency?.let { tvSalary.text = item.salaryCurrency }

        Glide.with(itemView)
            .load(item.areaName)
            .placeholder(R.drawable.ic_toast)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.margin_8)))
            .into(ivUrl100)
    }
}
