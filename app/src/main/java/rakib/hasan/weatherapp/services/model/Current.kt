package rakib.hasan.weatherapp.services.model

import com.google.gson.annotations.SerializedName


data class Current (

    @SerializedName("observation_time"     ) var observationTime     : String?           = null,
    @SerializedName("temperature"          ) var temperature         : Int?              = null,
    @SerializedName("weather_code"         ) var weatherCode         : Int?              = null,
    @SerializedName("weather_icons"        ) var weatherIcons        : ArrayList<String> = arrayListOf(),
    @SerializedName("weather_descriptions" ) var weatherDescriptions : ArrayList<String> = arrayListOf(),
    @SerializedName("wind_speed"           ) var windSpeed           : Int?              = null,
    @SerializedName("wind_degree"          ) var windDegree          : Int?              = null,
    @SerializedName("wind_dir"             ) var windDir             : String?           = null,
    @SerializedName("pressure"             ) var pressure            : Int?              = null,
    @SerializedName("precip"               ) var precip              : Double?           = null,
    @SerializedName("humidity"             ) var humidity            : Int?              = null,
    @SerializedName("cloudcover"           ) var cloudcover          : Int?              = null,
    @SerializedName("feelslike"            ) var feelslike           : Int?              = null,
    @SerializedName("uv_index"             ) var uvIndex             : Int?              = null,
    @SerializedName("visibility"           ) var visibility          : Int?              = null,
    @SerializedName("is_day"               ) var isDay               : String?           = null

)