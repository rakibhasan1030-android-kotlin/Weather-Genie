package rakib.hasan.weatherapp.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import rakib.hasan.weatherapp.R
import rakib.hasan.weatherapp.databinding.ActivityHomeBinding
import rakib.hasan.weatherapp.services.adapters.HourlyRvAdapter
import rakib.hasan.weatherapp.services.adapters.ViewPagerAdapter
import rakib.hasan.weatherapp.services.model.Current
import rakib.hasan.weatherapp.services.model.Daily
import rakib.hasan.weatherapp.services.model.Hourly
import rakib.hasan.weatherapp.services.utils.Constants
import rakib.hasan.weatherapp.viewModel.HomeActivityViewModel
import kotlin.math.roundToInt


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var homeActivityViewModel: HomeActivityViewModel

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController!!.hide(android.view.WindowInsets.Type.statusBars())
            window.decorView.windowInsetsController!!.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
        }
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getUserCurrentLocation()
        //binding.homeActivityWeatherCallButton.setOnClickListener(View.OnClickListener { getUserCurrentLocation() })
        homeActivityViewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]

        binding.activityHomeSwipeRefreshLayout.setOnRefreshListener { refreshPage() }

    }

    private fun getUserCurrentLocation() {
        if (checkPermissions()) {
            if (isUserLocationEnabled()) {
                // rest of code
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        getWeatherInfo(location.latitude, location.longitude)
                        setLocation(
                            Constants.getUserLocation(
                                applicationContext,
                                location.latitude,
                                location.longitude
                            )
                        )
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Sorry, can't find your location!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } else {
                //open setting
                Toast.makeText(
                    applicationContext,
                    "Please, turn on the location",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            //request permissions
            requestPermission()
        }
    }

    private fun setLocation(location: List<String?>) {

        if (location[0] != null) {
            binding.activityHomeUserCityTv.visibility = View.VISIBLE
            binding.activityHomeUserCityTv.text = location[0] + ","
        } else binding.activityHomeUserCityTv.visibility = View.GONE

        if (location[1] != null) {
            binding.activityHomeUserDivisionTv.visibility = View.VISIBLE
            binding.activityHomeUserDivisionTv.text = location[1]
        } else binding.activityHomeUserDivisionTv.visibility = View.GONE

        if (location[0] != null || location[1] != null) {
            binding.activityHomeLocationIv.visibility = View.VISIBLE
        } else binding.activityHomeLocationIv.visibility = View.GONE

    }

    private fun getWeatherInfo(latitude: Double, longitude: Double) {
        homeActivityViewModel.getWeatherInfo(latitude, longitude).observe(this, Observer { t ->
            val current: Current? = t.current
            val hourly: ArrayList<Hourly> = t.hourly
            val daily: ArrayList<Daily> = t.daily
            setCurrentWeatherData(current)
            setHourlyForeCastData(hourly)
            setDailyForeCastData(daily)
        })
    }

    private fun setDailyForeCastData(daily: ArrayList<Daily>) {
        //Constants.unixDateConvert("")
        val viewPagerAdapter = ViewPagerAdapter(applicationContext, daily)
        binding.activityHomeDailyForecastViewpager2.adapter = viewPagerAdapter
        binding.activityHomeDailyForecastViewpager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(
            binding.activityHomeDailyForecastTabLayout,
            binding.activityHomeDailyForecastViewpager2
        ) { tab, position ->
            val date: Int? = daily[position].dt
            tab.text = date?.let { Constants.unixToDateConvert(it) }
            Log.v("DATE", "Date  ${date?.let { Constants.unixToDateConvert(it) }}")
        }.attach()

    }

    private fun setHourlyForeCastData(hourly: ArrayList<Hourly>) {
        binding.activityHomeHourlyForecastRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.activityHomeHourlyForecastRv.setHasFixedSize(true)
        val hourlyRvAdapter: HourlyRvAdapter = HourlyRvAdapter(this, hourly)
        binding.activityHomeHourlyForecastRv.adapter = hourlyRvAdapter
    }

    private fun setCurrentWeatherData(current: Current?) {
        if (current != null) {
            val imageUrl: String? = current.weather[0].icon?.let { Constants.getImageApiUrl(it) }

            binding.activityHomeCurrentDateTv.text =
                Constants.unixToTimeConvert(current.dt.toString())
            binding.activityHomeCurrentSunriseTimeTv.text =
                applicationContext.getString(R.string.sunrise_at) + " " + Constants.unixToTimeConvert(
                    current.sunrise.toString()
                )
            //Picasso.get().load(Constants.getImageApiUrl("01d")).into(binding.activityHomeCurrentSunriseIconIv);
            binding.activityHomeCurrentSunsetTimeTv.text =
                applicationContext.getString(R.string.sunset_at) + " " + Constants.unixToTimeConvert(
                    current.sunset.toString()
                )
            //Picasso.get().load(Constants.getImageApiUrl("01n")).into(binding.activityHomeCurrentSunsetIconIv);

            binding.activityHomeCurrentDateTv.text =
                current.dt?.let { Constants.unixToDateConvertFullDate(it) }

            if (imageUrl != null) {
                Log.v("imageUrl", imageUrl)
                binding.activityHomeCurrentWeatherDescIv.visibility = View.VISIBLE
                Picasso.get()
                    .load(imageUrl)
                    .into(binding.activityHomeCurrentWeatherDescIv)
            } else binding.activityHomeCurrentWeatherDescIv.visibility = View.GONE

            val mainWeather: String? = current.weather[0].main
            if (mainWeather != null) {
                binding.activityHomeCurrentWeatherMainTv.visibility = View.VISIBLE
                binding.activityHomeCurrentWeatherMainTv.text = mainWeather
            } else binding.activityHomeCurrentWeatherMainTv.visibility = View.GONE
//
            val currentTemp: String? = current.temp?.roundToInt()?.toString()
            if (currentTemp != null) {
                binding.activityHomeCurrentTempTv.visibility = View.VISIBLE
                binding.activityHomeCurrentTempTv.text = currentTemp + "\u2103"
            } else binding.activityHomeCurrentTempTv.visibility = View.GONE

            val feelsLike: String? = current.feelsLike?.roundToInt()?.toString()
            if (feelsLike != null) {
                binding.activityHomeCurrentFeelsLikeTempTv.visibility = View.VISIBLE
                binding.activityHomeCurrentFeelsLikeTempTv.text =
                    applicationContext.getString(R.string.feels_like) + " " + feelsLike + applicationContext.getString(
                        R.string.degree_celsius
                    )
            } else binding.activityHomeCurrentFeelsLikeTempTv.visibility = View.GONE

            val windSpeed: String? = current.windSpeed?.toString()
            if (windSpeed != null) {
                binding.activityHomeCurrentWindContainer.visibility = View.VISIBLE
                binding.activityHomeCurrentWindValueTv.text =
                    windSpeed + applicationContext.getString(R.string.wind_unit)
            } else binding.activityHomeCurrentWindContainer.visibility = View.GONE

            val humidity: String? = current.humidity?.toString()
            if (humidity != null) {
                binding.activityHomeCurrentHumidityContainer.visibility = View.VISIBLE
                binding.activityHomeCurrentHumidityValueTv.text =
                    humidity + applicationContext.getString(R.string.percentage)
            } else binding.activityHomeCurrentHumidityContainer.visibility = View.GONE

            val uvi: String? = current.uvi?.toString()
            if (humidity != null) {
                binding.activityHomeCurrentUvIndexContainer.visibility = View.VISIBLE
                binding.activityHomeCurrentUvIndexValueTv.text = uvi
            } else binding.activityHomeCurrentUvIndexContainer.visibility = View.GONE


            binding.activityHomeCurrentPressureValueTv.text =
                Constants.getFormattedPressure(current.pressure) + applicationContext.getString(R.string.pressure_unit)

            binding.activityHomeCurrentVisibilityValueTv.text =
                Constants.getFormattedVisibility(current.visibility) + applicationContext.getString(
                    R.string.visibility_unit
                )

            binding.activityHomeCurrentDewPointValueTv.text = current.dewPoint?.roundToInt()
                ?.toString() + applicationContext.getString(R.string.degree_celsius)
        }
    }

    private fun isUserLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission granted!", Toast.LENGTH_LONG).show()
                getUserCurrentLocation()
            } else {
                Toast.makeText(applicationContext, "Permission denied!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun refreshPage() {
        this.startActivity(Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0);
    }
}