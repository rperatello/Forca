package br.com.rperatello.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.rperatello.model.service.ServiceApi
import br.com.rperatello.model.entity.Word
import br.com.rperatello.model.entity.WordIdList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class ForcaViewModel (application: Application): AndroidViewModel(application) {



    val wordIdListMLD: MutableLiveData<WordIdList> = MutableLiveData()
    val wordMLD: MutableLiveData<ArrayList<Word>> = MutableLiveData()
    var wordIdMLD: MutableLiveData<Int> = MutableLiveData()
    var wordIdList : MutableList<Int> = ArrayList()

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())



    fun getIdListByLevel(id: Int){
        coroutineScope.launch {
            serviceApi.retrieveIdentifiers(id).enqueue(object: Callback<WordIdList> {
                override fun onResponse(
                    call: Call<WordIdList>,
                    response: Response<WordIdList>
                ) {
                    wordIdListMLD.postValue(response.body())
                    Log.v("Jogo da Forca", response.body().toString())
                }

                @SuppressLint("LongLogTag")
                override fun onFailure(call: Call<WordIdList>, t: Throwable) {
                    Log.e("$BASE_URL", t.message.toString())
                }
            })
        }
    }

    fun getIdList(totalRounds: Int){
        coroutineScope.launch {
            var rounds: Int = 0
            while (rounds != totalRounds) {
                val random = Random()
                val randomValue = random.nextInt(wordIdListMLD.value!!.size - 1)
                val id =  wordIdListMLD.value!![randomValue].toString()
                if (!wordIdList.contains(id.toInt())) {
                    wordIdList.add(id.toInt())
                    wordIdMLD.postValue(id.toInt())
                    rounds++
                }
            }
            Log.v("Jogo da Forca", "random IdList: $wordIdList")
        }
    }

    fun getWordById(id: Int){
        coroutineScope.launch {
            serviceApi.retrieveWord(id).enqueue(object: Callback<ArrayList<Word>> {
                override fun onResponse(
                    call: Call<ArrayList<Word>>,
                    response: Response<ArrayList<Word>>
                ) {
                    wordMLD.postValue(response.body())
                    Log.v("Jogo da Forca", "word received: " + response.body().toString())
                }

                @SuppressLint("LongLogTag")
                override fun onFailure(call: Call<ArrayList<Word>>, t: Throwable) {
                    Log.e("$BASE_URL", t.message.toString())
                }

            })
        }
    }
}