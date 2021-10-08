package com.colermanning.gompeiclicker.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.colermanning.gompeiclicker.Game
import com.colermanning.gompeiclicker.Upgrade
import java.util.*

/**
 * Database Access Object for the DB,
 * contains functions for each database
 * operation you want to perform
 */
@Dao
interface GompeiClickerDao {

    @Query("SELECT currentPoints FROM game_table")
    fun getPoints(): LiveData<Int>

    @Query("UPDATE game_table SET currentPoints=(:points)")
    fun updatePoints(points: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUpgrade(upgrade: Upgrade)

    @Query("DELETE FROM upgrade_table")
    fun deleteAllUpgrades()



//    @Query("SELECT * FROM upgrade_table")
//    fun getUpgrades() : LiveData<List<Upgrade>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addGame(game: Game)
//
//    @Query("SELECT * FROM game_table")
//    fun getGames(id : UUID): LiveData<List<Game>>

    //todo, setPoints()
//    @Update
//    fun updateGame()

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