package ru.practicum.android.diploma.data

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Locale

class VacanciesAdapterForFavScreen(
    private val clickListener: VacancyClickListener
) : RecyclerView.Adapter<VacanciesAdapterForFavScreen.VacancyViewHolder>() {

    private val vacanciesList: MutableList<Vacancy> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VacancyViewHolder(VacancyBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = vacanciesList.size

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickListener.onVacancyClick(vacanciesList[position])
        }
        holder.bind(vacanciesList[position])
    }

    fun setContent(newList: List<Vacancy>) {
        vacanciesList.clear()
        vacanciesList.addAll(newList)
    }

    fun getItemByPosition(position: Int) =
        vacanciesList[position]


    inner class VacancyViewHolder(private val binding: VacancyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            with(binding) {
                setImage(vacancy.employerLogoUrls)
                val name = vacancy.name
                tvDescription.text = name
                tvNameCompany.text = vacancy.employer
                tvSalary.text = getSalaryDescription(
                    itemView.resources,
                    vacancy.salaryFrom,
                    vacancy.salaryTo,
                    vacancy.currency
                )
            }
        }

        private fun setImage(imageUrl: String?) {
            val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_card_company_12)
            Glide.with(itemView)
                .load(imageUrl)
                .transform(FitCenter(), RoundedCorners(cornerRadius))
                .placeholder(R.drawable.ic_toast)
                .into(binding.ivLogo)
        }
    }

    @SuppressLint("StringFormatMatches")
    fun getSalaryDescription(resources: Resources, salaryFrom: Int?, salaryTo: Int?, salaryCurrency: String?): String {
        return when {
            salaryFrom != null && salaryTo != null ->
                resources.getString(
                    R.string.salary_to,
                    formatSalary(salaryFrom),
                    formatSalary(salaryTo),
                    salaryCurrency?.let { getCurrencySymbol(it) }
                )

            salaryFrom != null && salaryTo == null ->
                resources.getString(
                    R.string.salary_from,
                    formatSalary(salaryFrom),
                    salaryCurrency?.let { getCurrencySymbol(it) }
                )

            salaryFrom == null && salaryTo != null ->
                resources.getString(
                    R.string.salary_to,
                    formatSalary(salaryTo),
                    salaryCurrency?.let { getCurrencySymbol(it) }
                )

            else -> resources.getString(R.string.without_salary)
        }
    }

    private fun formatSalary(amount: Int): String {
        val format: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
        return format.format(amount).replace(",", " ")
    }

    private val currencySymbols = mutableMapOf<String, String>(
        "RUR" to "₽",
        "EUR" to "€",
        "KZT" to "₸",
        "AZN" to "\u20BC",
        "USD" to "$",
        "BYR" to "\u0042\u0072",
        "GEL" to "\u20BE",
        "UAH" to "\u20b4",
    )

    fun getCurrencySymbol(currency: String): String {
        return currencySymbols[currency] ?: currency
    }

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }
}
