package rakib.hasan.weatherapp.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import rakib.hasan.weatherapp.services.model.WeatherInfo
import rakib.hasan.weatherapp.services.repositories.HomeActivityRepository

class HomeActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val homeActivityRepository : HomeActivityRepository = HomeActivityRepository(application.applicationContext)
    fun getWeatherInfo(latitude: Double, longitude: Double) : MutableLiveData<WeatherInfo> {
        return homeActivityRepository.getWeatherInfo(latitude, longitude)
    }
}