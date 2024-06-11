package pt.ipt.dama2024.betterday.retrofit.service

import pt.ipt.dama2024.betterday.data.CatApiResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * Service interface with the objective of making API calls to fetch cat images.
 */
interface CatService {
    @GET("cat")
    fun getCatImage(): Call<CatApiResponse>

}
