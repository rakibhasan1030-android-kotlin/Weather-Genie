package rakib.hasan.weatherapp.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import rakib.hasan.weatherapp.R
import rakib.hasan.weatherapp.databinding.ActivityHomeBinding
import rakib.hasan.weatherapp.services.utils.Constants
import java.util.jar.Manifest

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserCurrentLocation();
    }

    private fun getUserCurrentLocation() {
        if(checkPermissions()){
            if (isUserLocationEnabled()){
                // rest of code
                Toast.makeText(applicationContext, "May day!", Toast.LENGTH_LONG).show()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

}