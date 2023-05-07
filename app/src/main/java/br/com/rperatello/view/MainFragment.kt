package br.com.rperatello.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.rperatello.R
import br.com.rperatello.databinding.FragmentMainBinding
import br.com.rperatello.model.settings.GameSettings
import br.com.rperatello.viewmodel.ForcaViewModel
import br.com.rperatello.viewmodel.ForcaViewModelState
import br.com.rperatello.viewmodel.RoundState

class MainFragment : Fragment() {

    private lateinit var fragmentMainViewBinding: FragmentMainBinding
    private lateinit var forcaViewModel: ForcaViewModel
    private var gameSettings = GameSettings.getInstance()
    private var currentRound : Int = 1

    private val attemptsImage = arrayOf(R.mipmap.f0, R.mipmap.f1, R.mipmap.f2, R.mipmap.f3, R.mipmap.f4, R.mipmap.f5, R.mipmap.f6)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMainViewBinding = FragmentMainBinding.inflate(inflater, container, false)

        forcaViewModel = ViewModelProvider(this).get(ForcaViewModel::class.java)

        currentRound = 1

        Log.v(getString(R.string.app_name), "startGame()")
        startGame()
        listenToViewModelState()

        return fragmentMainViewBinding.root
    }

    private fun listenToViewModelState() {
        forcaViewModel.forcaViewModelState.observe(this) {
            when (it) {
                ForcaViewModelState.Loading -> {
                    Log.v(getString(R.string.app_name), "ForcaViewModelState.Loading")
                }

                is ForcaViewModelState.Game -> {

                    Log.v(getString(R.string.app_name), "ForcaViewModelState.Game")
                    Log.v(getString(R.string.app_name), forcaViewModel.forcaViewModelState.value.toString())

                    fragmentMainViewBinding.gameStatus.setImageResource(attemptsImage[it.attempts])
                    fragmentMainViewBinding.inputTxtTv.text = it.wordToShow

                    if(it.roundState == RoundState.PLAYING){
                        fragmentMainViewBinding.keyboard.visibility = View.VISIBLE
                        fragmentMainViewBinding.nextRoundLL.visibility = View.GONE
                        fragmentMainViewBinding.nextRoundBt.visibility = View.GONE

                    } else {

                        fragmentMainViewBinding.wordTv.text = it.word.palavra
                        fragmentMainViewBinding.keyboard.visibility = View.GONE
                        resetKeyboard()

                        if (currentRound < gameSettings.totalRounds){
                            clickNextRound()
                        }
                        else {
                            forcaViewModel.nextRound()
                        }

                    }
                }

                is ForcaViewModelState.GameOver -> {
                    Log.v(getString(R.string.app_name), "ForcaViewModelState.GameOver")
                    fragmentMainViewBinding.Section2.visibility = View.VISIBLE
                    fragmentMainViewBinding.hitsNumber.text = "Total de Acertos: ${it.hits.count()}"
                    fragmentMainViewBinding.hitsList.text = "Correto: ${it.hits.joinToString()}"
                    fragmentMainViewBinding.missedNumber.text = "Total de Erros: ${it.missed.count()}"
                    fragmentMainViewBinding.missedList.text = "Errado: ${it.missed.joinToString()}"

                    newGame()
                }

                is ForcaViewModelState.Error -> {
                    Log.v(getString(R.string.app_name), "ForcaViewModelState.Error")
                    Log.e(getString(R.string.app_name), it.error)
                }
            }
        }
    }

    private fun startGame() {
        Log.v(getString(R.string.app_name), "startGame()")
        fragmentMainViewBinding.keyboard.visibility = View.VISIBLE
        fragmentMainViewBinding.wordTv.visibility = View.VISIBLE
        fragmentMainViewBinding.inputTxtTv.visibility = View.VISIBLE

        showRound()
        clickKeyboard()
        forcaViewModel.startGame()
    }

    private fun newGame(){
        Log.v(getString(R.string.app_name), "newGame()")
        fragmentMainViewBinding.newGameLL.visibility = View.VISIBLE
        fragmentMainViewBinding.newGameBt.visibility = View.VISIBLE
        fragmentMainViewBinding.newGameBt.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun clickNextRound(){
        Log.v(getString(R.string.app_name), "clickNextRound()")
        fragmentMainViewBinding.nextRoundLL.visibility = View.VISIBLE
        fragmentMainViewBinding.nextRoundBt.visibility = View.VISIBLE
        fragmentMainViewBinding.nextRoundBt.setOnClickListener {
            currentRound += 1
            showRound()
            fragmentMainViewBinding.wordTv.text = ""
            forcaViewModel.nextRound()
            return@setOnClickListener
        }
    }

    private fun showRound() {
        Toast.makeText(
            this@MainFragment.context,
            "Rodada $currentRound",
            Toast.LENGTH_SHORT
        ).show()
        return
    }

    private fun clickKeyboard(){
        with(fragmentMainViewBinding){
            QbT.setOnClickListener { forcaViewModel.inputLetter('Q'); QbT.isEnabled = false }
            WbT.setOnClickListener { forcaViewModel.inputLetter('W'); WbT.isEnabled = false }
            EbT.setOnClickListener { forcaViewModel.inputLetter('E'); EbT.isEnabled = false }
            RbT.setOnClickListener { forcaViewModel.inputLetter('R'); RbT.isEnabled = false  }
            TbT.setOnClickListener { forcaViewModel.inputLetter('T'); TbT.isEnabled = false  }
            YbT.setOnClickListener { forcaViewModel.inputLetter('Y'); YbT.isEnabled = false  }
            UbT.setOnClickListener { forcaViewModel.inputLetter('U'); UbT.isEnabled = false  }
            IbT.setOnClickListener { forcaViewModel.inputLetter('I'); IbT.isEnabled = false  }
            ObT.setOnClickListener { forcaViewModel.inputLetter('O'); ObT.isEnabled = false  }
            PbT.setOnClickListener { forcaViewModel.inputLetter('P'); PbT.isEnabled = false  }

            AbT.setOnClickListener { forcaViewModel.inputLetter('A'); AbT.isEnabled = false  }
            SbT.setOnClickListener { forcaViewModel.inputLetter('S'); SbT.isEnabled = false  }
            DbT.setOnClickListener { forcaViewModel.inputLetter('D'); DbT.isEnabled = false  }
            FbT.setOnClickListener { forcaViewModel.inputLetter('F'); FbT.isEnabled = false  }
            GbT.setOnClickListener { forcaViewModel.inputLetter('G'); GbT.isEnabled = false  }
            HbT.setOnClickListener { forcaViewModel.inputLetter('H'); HbT.isEnabled = false  }
            JbT.setOnClickListener { forcaViewModel.inputLetter('J'); JbT.isEnabled = false  }
            KbT.setOnClickListener { forcaViewModel.inputLetter('K'); KbT.isEnabled = false  }
            LbT.setOnClickListener { forcaViewModel.inputLetter('L'); LbT.isEnabled = false  }

            ZbT.setOnClickListener { forcaViewModel.inputLetter('Z'); ZbT.isEnabled = false  }
            XbT.setOnClickListener { forcaViewModel.inputLetter('X'); XbT.isEnabled = false  }
            CbT.setOnClickListener { forcaViewModel.inputLetter('C'); CbT.isEnabled = false  }
            VbT.setOnClickListener { forcaViewModel.inputLetter('V'); VbT.isEnabled = false  }
            BbT.setOnClickListener { forcaViewModel.inputLetter('B'); BbT.isEnabled = false  }
            NbT.setOnClickListener { forcaViewModel.inputLetter('N'); NbT.isEnabled = false  }
            MbT.setOnClickListener { forcaViewModel.inputLetter('M'); MbT.isEnabled = false  }
        }
        return
    }

    private fun resetKeyboard(){
        fragmentMainViewBinding.QbT.isEnabled = true
        fragmentMainViewBinding.WbT.isEnabled = true
        fragmentMainViewBinding.EbT.isEnabled = true
        fragmentMainViewBinding.RbT.isEnabled = true
        fragmentMainViewBinding.TbT.isEnabled = true
        fragmentMainViewBinding.YbT.isEnabled = true
        fragmentMainViewBinding.UbT.isEnabled = true
        fragmentMainViewBinding.IbT.isEnabled = true
        fragmentMainViewBinding.ObT.isEnabled = true
        fragmentMainViewBinding.PbT.isEnabled = true

        fragmentMainViewBinding.AbT.isEnabled = true
        fragmentMainViewBinding.SbT.isEnabled = true
        fragmentMainViewBinding.DbT.isEnabled = true
        fragmentMainViewBinding.FbT.isEnabled = true
        fragmentMainViewBinding.GbT.isEnabled = true
        fragmentMainViewBinding.HbT.isEnabled = true
        fragmentMainViewBinding.JbT.isEnabled = true
        fragmentMainViewBinding.KbT.isEnabled = true
        fragmentMainViewBinding.LbT.isEnabled = true

        fragmentMainViewBinding.ZbT.isEnabled = true
        fragmentMainViewBinding.XbT.isEnabled = true
        fragmentMainViewBinding.CbT.isEnabled = true
        fragmentMainViewBinding.VbT.isEnabled = true
        fragmentMainViewBinding.BbT.isEnabled = true
        fragmentMainViewBinding.NbT.isEnabled = true
        fragmentMainViewBinding.MbT.isEnabled = true
        return
    }

}