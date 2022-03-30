package rakib.hasan.weatherapp.services.utils

class Constants {

    // http://api.weatherstack.com/current?access_key=77586bd9deb82ce6d2ae26569cf6130d&query=Dhaka
    // http://api.weatherstack.com/current?access_key=77586bd9deb82ce6d2ae26569cf6130d&query=Dhaka

    companion object{
        val WEATHER_API_BASE_URL = "http://api.weatherstack.com"
        val WEATHER_API_KEY = "77586bd9deb82ce6d2ae26569cf6130d"

        fun getWeatherUrl(city : String) : String{
            return "$WEATHER_API_BASE_URL/current?access_key=$WEATHER_API_KEY&query=$city";
        }

    }

}