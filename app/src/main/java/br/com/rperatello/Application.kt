package br.com.rperatello

import android.app.Application
import br.com.rperatello.model.settings.GameSettings

class GameApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        GameSettings.init(this)
    }
}