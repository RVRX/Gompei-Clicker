package com.colermanning.gompeiclicker

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

private const val TAG = "GameViewModel"

class GameViewModel : ViewModel() {

    var weatherString: String = ""
    private val gompeiClickerRepository = GompeiClickerRepository.get()
    val pointLiveData = gompeiClickerRepository.getPoints()
    val ownedClickValueUpgrades = gompeiClickerRepository.getModifiersForBoughtClickValueUpgrades()
    var currentClickMultiplier = 1.0 //updated in Fragment, determines click multiplier

    fun setPoints(points: Int) {
        gompeiClickerRepository.setPoints(points)
    }
}