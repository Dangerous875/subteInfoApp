package com.example.subwaystatus

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Collections
import java.util.Locale

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
        updateDate()
        getCurrentsSubwayAlerts()
    }

    private fun updateDate() {
        val calendar = Calendar.getInstance()
//            val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
        val hora = formato.format(calendar.time)
        supportActionBar?.title = "Actualización: $hora hs"
    }


    private fun applyAlerts() {
        Log.i("respuesta", SubwayService.subwayList.toString())
        val alerts = SubwayService.subwayList
        setValueDefault()
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

    private fun setValueDefault() {
        updateDataDefault(binding.cardLineaA,binding.tvLineaA)
        updateDataDefault(binding.cardLineaB,binding.tvLineaB)
        updateDataDefault(binding.cardLineaC,binding.tvLineaC)
        updateDataDefault(binding.cardLineaD,binding.tvLineaD)
        updateDataDefault(binding.cardLineaE,binding.tvLineaE)
        updateDataDefault(binding.cardLineaH,binding.tvLineaH)
        updateDataDefault(binding.cardLineaP,binding.tvLineaP)
    }


    private fun updateDataAlert(card: CardView, tvLinea: TextView, alert: Alerts) {
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.statusBad))
        tvLinea.text = alert.alert.description.translation[0].text
        tvLinea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
    }

    private fun updateDataDefault(card: CardView, tvLinea: TextView) {
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.statusNormal))
        tvLinea.text = getString(R.string.Normal)
        tvLinea.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity,menu)
        if (menu is MenuBuilder){
            menu.setOptionalIconsVisible(true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_update -> {
                initUI()
                return true
            }
            R.id.item_map -> {
                return true
            }
            R.id.item_exit -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

