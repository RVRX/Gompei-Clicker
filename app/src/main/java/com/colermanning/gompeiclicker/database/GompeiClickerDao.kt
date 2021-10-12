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

    @Query("UPDATE game_table SET currentPoints= CASE WHEN (:cost) <= currentPoints THEN currentPoints - (:cost) ELSE currentPoints END")
    fun tryBuy(cost: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUpgrade(upgrade: Upgrade)

    @Query("DELETE FROM upgrade_table")
    fun deleteAllUpgrades()

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addGame(game: Game)

    @Query("SELECT modifier FROM upgrade_table WHERE bought=1 AND upgradeType='ClickValue'")
    fun getModifiersForBoughtClickValueUpgrades(): LiveData<List<Double>>

    @Query("UPDATE upgrade_table SET bought=1 WHERE id=(:id)")
    fun buyUpgradeById(id: String)

    @Query("SELECT * FROM upgrade_table")
    fun getUpgrades() : LiveData<List<Upgrade>>

//    @Query("SELECT * FROM game_table")
//    fun getGames(id : UUID): LiveData<List<Game>>

    //todo, more DAO functions...
}