package com.example.subwaystatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.subwaystatus.data.APIService
import com.example.subwaystatus.data.Alerts
import com.example.subwaystatus.data.Keys
import com.example.subwaystatus.data.SubwayResponse
import com.example.subwaystatus.databinding.ActivityMainBinding
import com.example.subwaystatus.service.SubwayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun getCurrentsSubwayAlerts() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getSubwayResponse("serviceAlerts?json=1&client_id=5bb3a22b619f4137b212eb2f1e608cbc&client_secret=1e7c21EE603142E985d0Ca920324f551")
            val newListAlerts = call.body()
            withContext(Dispatchers.Main) {
                updateDataSubway(call, newListAlerts)
            }
        }
    }

    private fun updateDataSubway(call: Response<SubwayResponse>, newListAlerts: SubwayResponse?) {
        if (call.isSuccessful) {
            val newlist: List<Alerts>? = newListAlerts?.subwayAlerts
            if (newlist != null) {
//                val subwayResponse = call.body()?.subwayAlerts ?: emptyList()
//                Log.i("respuesta",subwayResponse.toString())
                SubwayService.subwayList = Collections.emptyList()
                SubwayService.subwayList = newlist

            }
        }

        applyAlerts()

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://apitransporte.buenosaires.gob.ar/subtes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun initUI() {
        getCurrentsSubwayAlerts()
    }


    private fun applyAlerts() {
        Log.i("respuesta", SubwayService.subwayList.toString())
        val alerts = SubwayService.subwayList
        for (alert in alerts) {
            when (alert.id) {
                Keys.keyLineaA -> {
                    updateDataAlert(binding.cardLineaA, binding.tvLineaA, alert)
                }

                Keys.keyLineaB -> {
                    updateDataAlert(binding.cardLineaB, binding.tvLineaB, alert)
                }

                Keys.keyLineaC -> {
                    updateDataAlert(binding.cardLineaC, binding.tvLineaC, alert)
                }

                Keys.keyLineaD -> {
                    updateDataAlert(binding.cardLineaD, binding.tvLineaD, alert)
                }

                Keys.keyLineaE -> {
                    updateDataAlert(binding.cardLineaE, binding.tvLineaE, alert)
                }

                Keys.keyLineaH -> {
                    updateDataAlert(binding.cardLineaH, binding.tvLineaH, alert)
                }

                Keys.keyPremetro -> {
                    updateDataAlert(binding.cardLineaP, binding.tvLineaP, alert)
                }

            }
        }
    }


    private fun updateDataAlert(card: CardView, tvLinea: TextView, alert: Alerts) {
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.statusBad))
        tvLinea.text = alert.alert.description.translation[0].text
    }

    private fun updateDataDefault(card: CardView, tvLinea: TextView, alert: Alerts) {
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.statusNormal))
        tvLinea.text = getString(R.string.Normal)
    }
}

