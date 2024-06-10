package pt.ipt.dama2024.betterday.retrofit.service

import pt.ipt.dama2024.betterday.data.CatApiResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * function that request data from API
 */
interface CatService {
    @GET("api/cat")
    fun getCatImage(): Call<CatApiResponse>

}
