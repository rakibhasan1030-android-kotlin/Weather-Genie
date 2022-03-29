package rakib.hasan.weatherapp.services.model

import com.google.gson.annotations.SerializedName


data class WeatherInfo (

    @SerializedName("request"  ) var request  : Request?  = Request(),
    @SerializedName("location" ) var location : Location? = Location(),
    @SerializedName("current"  ) var current  : Current?  = Current()

)