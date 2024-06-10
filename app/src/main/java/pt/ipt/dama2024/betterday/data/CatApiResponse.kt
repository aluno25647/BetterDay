package pt.ipt.dama2024.betterday.data

import com.google.gson.annotations.SerializedName

data class CatApiResponse(
    @SerializedName("cat") val imageUrl: String
)