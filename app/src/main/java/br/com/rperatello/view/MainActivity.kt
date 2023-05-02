package br.com.rperatello.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import br.com.rperatello.R
import br.com.rperatello.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        supportFragmentManager.commit {
            replace(R.id.mainFcv, SettingsFragment(), "SettingsFragment")
        }
    }

    fun startGame() {
        supportFragmentManager.commit {
            addToBackStack("principal")
            add(R.id.mainFcv, MainFragment(), "MainFragment")
        }
    }
}