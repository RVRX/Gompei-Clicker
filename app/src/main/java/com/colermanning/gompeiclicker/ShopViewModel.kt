package com.colermanning.gompeiclicker

import androidx.lifecycle.ViewModel

class ShopViewModel : ViewModel(){
    private val shopRepository = GompeiClickerRepository.get()
    var shopListLiveData = shopRepository.getUpgrades()


    fun fillUpgrades() {
        shopRepository.populateDefaults()
    }

    fun buyUpgrade(upgrade: Upgrade) {
        shopRepository.buyUpgradeById(upgrade.id)
    }
}