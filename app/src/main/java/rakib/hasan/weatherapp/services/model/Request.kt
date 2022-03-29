package rakib.hasan.weatherapp.services.model

import com.google.gson.annotations.SerializedName


data class Request (

    @SerializedName("type"     ) var type     : String? = null,
    @SerializedName("query"    ) var query    : String? = null,
    @SerializedName("language" ) var language : String? = null,
    @SerializedName("unit"     ) var unit     : String? = null

)