package rakib.hasan.weatherapp.services.model

import com.google.gson.annotations.SerializedName


data class Location (

    @SerializedName("name"            ) var name           : String? = null,
    @SerializedName("country"         ) var country        : String? = null,
    @SerializedName("region"          ) var region         : String? = null,
    @SerializedName("lat"             ) var lat            : String? = null,
    @SerializedName("lon"             ) var lon            : String? = null,
    @SerializedName("timezone_id"     ) var timezoneId     : String? = null,
    @SerializedName("localtime"       ) var localtime      : String? = null,
    @SerializedName("localtime_epoch" ) var localtimeEpoch : Int?    = null,
    @SerializedName("utc_offset"      ) var utcOffset      : String? = null

)