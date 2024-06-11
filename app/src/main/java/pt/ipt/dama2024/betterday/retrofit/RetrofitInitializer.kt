package pt.ipt.dama2024.betterday.retrofit
import pt.ipt.dama2024.betterday.retrofit.service.CatService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Object that provides a Retrofit instance configured to connect to the Cataas API.
 */
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
 *
 * Object that provides access to the CatService API endpoints
 *
 * Due to errors while following the method used in class, this guide suggested an object approach instead of function:
 * https://medium.com/@imkuldeepsinghrai/api-calls-with-retrofit-in-android-kotlin-a-comprehensive-guide-e049e19deba9
 */
object ApiClient {
    val catService: CatService by lazy {
        RetrofitClient.retrofit.create(CatService::class.java)
    }
}