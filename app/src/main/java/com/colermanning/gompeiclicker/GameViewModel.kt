package com.colermanning.gompeiclicker

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

private const val TAG = "GameViewModel"

class GameViewModel : ViewModel() {

    private val gompeiClickerRepository = GompeiClickerRepository.get()
    val pointLiveData = gompeiClickerRepository.getPoints()
    val ownedClickValueUpgrades = gompeiClickerRepository.getModifiersForBoughtClickValueUpgrades()
    var currentClickMultiplier = 1.0 //updated in Fragment, determines click multiplier

    fun setPoints(points: Int) {
        gompeiClickerRepository.setPoints(points)
    }

    /**
     * Gets the correct amount of points to add for a click
     * considering all owned upgrades
     */
    fun clickPointsToAdd(): Int {
        Log.d(TAG, "ownedClickValueUpgrades: ${ownedClickValueUpgrades.value}")
        var multiplier : Double = 1.0
        ownedClickValueUpgrades.value?.forEach {
            multiplier = multiplier.times(it)
        }
        Log.i(TAG, "clickPointsToAdd(): $multiplier / ${multiplier.roundToInt()}")
        return multiplier.roundToInt()
    }
}