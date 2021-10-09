package com.colermanning.gompeiclicker

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.colermanning.gompeiclicker.database.GompeiClickerDatabase
import java.lang.IllegalStateException
import java.util.concurrent.Executors
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.RoomDatabase




private const val DATABASE_NAME = "gompei-clicker-database"
private const val TAG = "GompeiClickerRepository"

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
    ).addCallback(
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.i(TAG, "First Time DB Creation... populating defaults")
                populateDefaults() //adds upgrades and new game to DB
            }
        }
    ).build()

    private val gompeiClickerDao = database.gompeiClickerDao()
    private val executor = Executors.newSingleThreadExecutor() //remove before pushing

    //todo, add a function here for each function in the DAO
    fun getPoints(): LiveData<Int> = gompeiClickerDao.getPoints()

    fun setPoints(points: Int) {
        executor.execute {
            gompeiClickerDao.updatePoints(points)
        }
    }

    fun getUpgrades(): LiveData<List<Upgrade>> = gompeiClickerDao.getUpgrades()

    fun addUpgrade(id: String, upgradeType: String, description:String, cost: Int, modifier: Double, bought : Boolean = false) {
        executor.execute {
            gompeiClickerDao.addUpgrade(Upgrade(id, upgradeType, description, cost, modifier, bought))
        }
    }


    fun populateDefaults() {
        executor.execute {
            //empty current DB stuff
            gompeiClickerDao.deleteAllUpgrades()

            //single default game
            gompeiClickerDao.addGame(Game(currentPoints = 0))

            //Upgrade options
            addUpgrade("Hay", "ClickValue", "Hay Description...", 25, 1.5)
            addUpgrade("Sugar", "ClickValue", "Sugar Description...", 50, 2.0)
            addUpgrade("Education", "ClickValue", "Education Description...", 100,2.5)
        }
    }

    fun buyUpgradeById(id: String) {
        executor.execute {
            gompeiClickerDao.buyUpgradeById(id)
        }
    }

    fun getModifiersForBoughtClickValueUpgrades() : LiveData<List<Double>> =
        gompeiClickerDao.getModifiersForBoughtClickValueUpgrades()

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