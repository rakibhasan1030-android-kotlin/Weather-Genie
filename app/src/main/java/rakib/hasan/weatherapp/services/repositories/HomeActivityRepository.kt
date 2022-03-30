package rakib.hasan.weatherapp.services.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import rakib.hasan.weatherapp.services.api.VolleySingleton
import rakib.hasan.weatherapp.services.model.WeatherInfo
import rakib.hasan.weatherapp.services.utils.Constants

class HomeActivityRepository constructor(context: Context){
    private val TAG: String = HomeActivityRepository::class.java.simpleName

    var weatherInfoLiveData : MutableLiveData<WeatherInfo> = MutableLiveData()
    private val requestQueue = VolleySingleton.getInstance(context).requestQueue

    fun getWeatherInfo(city : String) :  MutableLiveData<WeatherInfo>{
        Log.v("TAG", "HomeActivityRepository <---> getWeatherInfo")

        val url  = Constants.getWeatherUrl(city)
        Log.v(TAG, "URL = $url");
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                var gson = Gson()
                val weatherInfo = gson.fromJson(response, WeatherInfo::class.java)
                weatherInfoLiveData.postValue(weatherInfo)
                //Log.v(TAG, "Response : $weatherInfo");
            },
            { error ->
                //var errorResponse : String = String(error.networkResponse.data, StandardCharsets.UTF_8)
                Log.v(TAG, "Error Response : $error");
            }
        )
        requestQueue.add(stringRequest)
        return weatherInfoLiveData
    }

}