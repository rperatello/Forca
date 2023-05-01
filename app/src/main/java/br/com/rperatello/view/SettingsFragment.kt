package br.com.rperatello.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import br.com.rperatello.R
import br.com.rperatello.databinding.FragmentSettingsBinding
import br.com.rperatello.model.settings.GameSettings


class SettingsFragment: Fragment() {

    private lateinit var fragmentSettingsViewBinding : FragmentSettingsBinding
    private lateinit var gameSettings: GameSettings

    lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingsViewBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        val spinner: Spinner = fragmentSettingsViewBinding.roundsOptions
        val roundList = arrayOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
        val arrayAdapter = ArrayAdapter<Int>(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, roundList)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gameSettings.round = roundList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        fragmentSettingsViewBinding.saveBt.setOnClickListener{
            gameSettings.level = if(fragmentSettingsViewBinding.level1Rb.isChecked) 1 else if(fragmentSettingsViewBinding.level2Rb.isChecked) 2 else 3


            val editor = sharedPreferences.edit()
            editor.putInt("GAME_LEVEL", gameSettings.level)
            editor.putInt("NUMBER_OF_ROUNDS", gameSettings.round)
            editor.apply()
            Log.v(
                getString(R.string.app_name),
                " | " + gameSettings.level.toString()+" | " + gameSettings.round.toString()
            )
            Toast.makeText(this@SettingsFragment.context, gameSettings.toString(), Toast.LENGTH_SHORT).show();
            activity?.supportFragmentManager?.popBackStack();
        }

        fragmentSettingsViewBinding.cancelBt.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack();
        }

        gameSettings = GameSettings(1, 1)

        sharedPreferences = requireActivity().applicationContext.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        gameSettings.level = sharedPreferences.getInt("GAME_LEVEL", 1)
        gameSettings.round = sharedPreferences.getInt("NUMBER_OF_ROUNDS", 1)

        Log.v(
            getString(R.string.app_name),
            " | " + gameSettings.level.toString()+" | " + gameSettings.round.toString()
        )

        fragmentSettingsViewBinding.roundsOptions.setSelection(gameSettings.round - 1)
        fragmentSettingsViewBinding.levelRGroup.check(if(gameSettings.level == 1) R.id.level1Rb else if(gameSettings.level == 2) R.id.level2Rb else R.id.level3Rb)

        return fragmentSettingsViewBinding.root
    }

}