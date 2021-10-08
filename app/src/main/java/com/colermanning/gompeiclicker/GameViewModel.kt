package com.colermanning.gompeiclicker

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val gompeiClickerRepository = GompeiClickerRepository.get()
    val pointLiveData = gompeiClickerRepository.getPoints()
}