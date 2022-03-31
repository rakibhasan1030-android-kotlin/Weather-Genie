package rakib.hasan.weatherapp.services.utils

class Constants {

    // http://api.weatherstack.com/current?access_key=77586bd9deb82ce6d2ae26569cf6130d&query=Dhaka
    // http://api.weatherstack.com/current?access_key=77586bd9deb82ce6d2ae26569cf6130d&query=Dhaka


    //TOMORROW ----------------------------
    //https://api.openweathermap.org/data/2.5/onecall?lat=23.7743889&lon=90.3669191&appid=3d6c443f18312d21ac8146de13fc5c98&units=imperial

    companion object{
        val WEATHER_API_BASE_URL = "https://api.openweathermap.org"
        val WEATHER_API_KEY = "3d6c443f18312d21ac8146de13fc5c98"

        fun getWeatherUrl(latitude:Double, longitude : Double) : String{
            return "$WEATHER_API_BASE_URL/data/2.5/onecall?lat=$latitude&lon=$longitude&appid=$WEATHER_API_KEY&units=imperial"
        }
    }

}