package rakib.hasan.weatherapp.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import rakib.hasan.weatherapp.databinding.ActivityHomeBinding
import java.util.*


class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationManager : LocationManager


    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.homeActivityWeatherCallButton.setOnClickListener(View.OnClickListener { getUserCurrentLocation() })
    }

    private fun getUserCurrentLocation() {
        if(checkPermissions()){
            if (isUserLocationEnabled()){
                // rest of code
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) { return }
                fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.
                    if (location != null){
                        binding.homeActivityWeatherTv.setText(getCity(location.latitude, location.longitude))
                    }else{
                        Toast.makeText(applicationContext, "Sorry, can't find your location!", Toast.LENGTH_LONG).show()
                    }
                }

            }else{
                //open setting
                Toast.makeText(applicationContext, "Please, turn on the location", Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }else{
            //request permissions
            requestPermission()
        }
    }

    private fun isUserLocationEnabled(): Boolean {
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    private fun checkPermissions() : Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Permission granted!", Toast.LENGTH_LONG).show()
                getUserCurrentLocation()
            }else{
                Toast.makeText(applicationContext, "Permission denied!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getCity(latitude: Double, longitude:Double):String{
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())

        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        val address: String = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        //val city: String = addresses[0].getLocality()
       // val state: String = addresses[0].getAdminArea()
        //val country: String = addresses[0].getCountryName()
        //val postalCode: String = addresses[0].getPostalCode()
        //val knownName: String = addresses[0].getFeatureName() // Only if available else return NULL
        return addresses[0].locality;
    }

}