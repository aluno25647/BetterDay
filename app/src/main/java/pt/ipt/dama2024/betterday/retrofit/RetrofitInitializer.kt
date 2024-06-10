package pt.ipt.dama2024.betterday.retrofit
import pt.ipt.dama2024.betterday.retrofit.service.CatService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val host = "https://cataas.com/"

    //Opens the connection to the API
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(host)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

/**
 * Object to simulate the
 */
object ApiClient {
    val catService: CatService by lazy {
        RetrofitClient.retrofit.create(CatService::class.java)
    }
}