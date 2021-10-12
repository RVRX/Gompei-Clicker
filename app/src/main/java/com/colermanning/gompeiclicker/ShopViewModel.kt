package com.colermanning.gompeiclicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class ShopViewModel : ViewModel(){
    private val shopRepository = GompeiClickerRepository.get()
    var shopListLiveData = shopRepository.getUpgrades()


    var pointLiveData: LiveData<Int> = shopRepository.getPoints()

    fun fillUpgrades() {
        shopRepository.populateDefaults()
    }

    fun buyUpgrade(upgrade: Upgrade){
        shopRepository.buyUpgradeById(upgrade.id)
        shopRepository.tryBuy(upgrade.cost)
    }
}