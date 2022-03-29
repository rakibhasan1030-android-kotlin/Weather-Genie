package rakib.hasan.weatherapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rakib.hasan.weatherapp.R
import rakib.hasan.weatherapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}