package com.colermanning.gompeiclicker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.colermanning.gompeiclicker.database.GompeiClickerDatabase
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "gompei-clicker-database"

/**
 * GameRepository is a Singleton class that gives the app an owner for the game data
 * and provides a way to easily pass that data between controller classes.
 *
 * Repository must be initialized with initialize() before
 * calling the repository with get()
 */
class GompeiClickerRepository private constructor(context: Context) {

    private val database : GompeiClickerDatabase = Room.databaseBuilder(
        context.applicationContext,
        GompeiClickerDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val gompeiClickerDao = database.gompeiClickerDao()
    private val executor = Executors.newSingleThreadExecutor() //remove before pushing

    //todo, add a function here for each function in the DAO
    fun getPoints(): LiveData<Int> = gompeiClickerDao.getPoints()

    fun addUpgrade(id: String, upgradeType: String, modifier: Double, bought : Boolean = false) {
        executor.execute {
            gompeiClickerDao.addUpgrade(Upgrade(id, upgradeType, modifier, bought))
        }
    }

    companion object {
        private var INSTANCE: GompeiClickerRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = GompeiClickerRepository(context)
            }
        }

        fun get() : GompeiClickerRepository {
            return INSTANCE ?:
            throw IllegalStateException("GompeiClickerRepository must be initialized")
        }
    }
}