package pt.ipt.dama2024.betterday.data

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response from the cat image API.
 */
data class CatApiResponse(
    @SerializedName("cat") val imageUrl: String
)