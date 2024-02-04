package ru.practicum.android.diploma.data.search.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.response.CountryResponse
import ru.practicum.android.diploma.data.dto.response.IndustryResponse
import ru.practicum.android.diploma.data.dto.response.JobResponse
import ru.practicum.android.diploma.data.dto.response.RegionResponse

interface HhApi {

    @GET("/vacancies")
    suspend fun jobSearch(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20,
    ): JobResponse
  /*
    @GET("vacancies?text=query")
    suspend fun jobSearch(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20 //Константа 20
    ): JobResponse
    */
    @GET("vacancies/{vacancy_id}/similar_vacancies")
    suspend fun detailVacancy(@Path("vacancy_id") vacancyId: String): JobResponse
    @GET("areas/countries")
    suspend fun filterCountry(): CountryResponse
    @GET("areas/{area_id}")
    suspend fun filterRegion(@Path("area_id") areaId: String): RegionResponse
    @GET("areas/industries")
    suspend fun filterIndustry(): IndustryResponse
}
