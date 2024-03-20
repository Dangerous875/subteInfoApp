package com.example.subwaystatus.data

import com.google.gson.annotations.SerializedName

data class SubwayResponse(@SerializedName("entity") val subwayAlerts : List<Alerts>)
