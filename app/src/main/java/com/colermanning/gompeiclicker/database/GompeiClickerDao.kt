package com.colermanning.gompeiclicker.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

/**
 * Database Access Object for the DB,
 * contains functions for each database
 * operation you want to perform
 */
@Dao
interface GompeiClickerDao {

    //todo, getPoints
    @Query("SELECT currentPoints FROM game_table")
    fun getPoints(): Int

    //todo, setPoints()
//    @Update
//    fun setPoints()

    //todo, get Upgrades...
    //  might need to make upgrade class?
    //  or get individual pieces of upgrades..?
//    @Query("SELECT * FROM upgrade ")
//    fun getUpgrades(): ...

    //todo, update a specific upgrade's bought boolean
//    @Update
//    fun updateUpgradeBoughtStatus(boolean: Boolean) //todo, not sure about arguments...


    //todo, more DAO functions...
}