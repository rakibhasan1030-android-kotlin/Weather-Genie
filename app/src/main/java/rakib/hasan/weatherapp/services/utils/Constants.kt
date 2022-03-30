package rakib.hasan.weatherapp.services.utils

class Constants {

    // http://api.weatherstack.com/current?access_key=77586bd9deb82ce6d2ae26569cf6130d&query=Dhaka
    // http://api.weatherstack.com/current?access_key=77586bd9deb82ce6d2ae26569cf6130d&query=Dhaka


    //TOMORROW ----------------------------
    //https://api.openweathermap.org/data/2.5/weather?lat=23.7743889&lon=90.3669191&appid=3d6c443f18312d21ac8146de13fc5c98

    companion object{
        val WEATHER_API_BASE_URL = "http://api.weatherstack.com"
        val WEATHER_API_KEY = "77586bd9deb82ce6d2ae26569cf6130d"

        fun getWeatherUrl(city : String) : String{
            return "$WEATHER_API_BASE_URL/current?access_key=$WEATHER_API_KEY&query=$city";
        }

    }

}