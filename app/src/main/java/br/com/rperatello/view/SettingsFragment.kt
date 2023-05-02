package br.com.rperatello.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import br.com.rperatello.R
import br.com.rperatello.databinding.FragmentSettingsBinding
import br.com.rperatello.model.settings.GameSettings


private val roundList = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)

class SettingsFragment : Fragment() {

    private lateinit var fragmentSettingsViewBinding: FragmentSettingsBinding

    private val gameSettings: GameSettings = GameSettings.getInstance()

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSettingsViewBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        val spinner: Spinner = fragmentSettingsViewBinding.roundsOptions
        val arrayAdapter = ArrayAdapter<Int>(
            this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            roundList
        )
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gameSettings.totalRounds = roundList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        fragmentSettingsViewBinding.levelRGroup.setOnCheckedChangeListener { radioGroup, i ->
            val difficulty = when (i) {
                R.id.level1Rb -> GameSettings.Difficulty.EASY
                R.id.level2Rb -> GameSettings.Difficulty.MEDIUM
                R.id.level3Rb -> GameSettings.Difficulty.HARD
                else -> return@setOnCheckedChangeListener
            }

            gameSettings.difficulty = difficulty
        }


        fragmentSettingsViewBinding.startGameBt.setOnClickListener {
            Toast.makeText(
                this@SettingsFragment.context,
                "Rounds: " + gameSettings.totalRounds + " | Difficulty: " + gameSettings.difficulty,
                Toast.LENGTH_SHORT
            ).show()

            (activity as MainActivity).startGame()
        }

        fragmentSettingsViewBinding.roundsOptions.setSelection(gameSettings.totalRounds - 1)
        fragmentSettingsViewBinding.levelRGroup.check(if(gameSettings.difficulty.id == 1) R.id.level1Rb else if(gameSettings.difficulty.id == 2) R.id.level2Rb else R.id.level3Rb)

        return fragmentSettingsViewBinding.root
    }
}