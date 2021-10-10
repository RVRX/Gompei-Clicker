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
            addUpgrade("Grass", "ClickValue", "Gompei eats a bit of fresh grass off of the ground. All natural! Each click now earns 1.25x the amount of innovation bucks.", 20, 1.25)
            addUpgrade("Straw", "ClickValue", "Gompei eats some straw that he found! He's not quite full, but it's still satisfying enough. Each click earns 1.5x the amount of innovation bucks.", 25, 1.5)
            addUpgrade("Sugar", "ClickValue", "Gompei eats a sugar cube! He's become a bit hyperactive, now he earns 2x the amount of innovation bucks!", 50, 2.0)
            addUpgrade("Basic Education", "ClickValue", "Gompei receives some basic education! He's learned the basics of how to innovate, and now earns 2.5x the amount of innovation bucks!", 100,2.5)
            addUpgrade("Hay", "ClickValue", "Gompei gets some hay to eat! He thinks it's delicious and is motivated to work harder than before! He now earns 3x points!", 500,3.0)
            addUpgrade("Watermelon", "ClickValue", "You give Gompei some watermelon as a sweet dessert. He loves it! Each click now earns 3.5x the amount of innovation bucks.", 1000, 3.5)
            addUpgrade("Advanced Education", "ClickValue", "Gompei gets smarter every day! New he's one of the smartest goats around! Each click now earns 4x the amount of innovation bucks.", 1500, 4.0)
            addUpgrade("Steak", "ClickValue", "Goats are normally herbivores, but Gompei has decided to experiment a little bit. Each click now earns 5x the amount of innovation bucks.", 2500, 5.0)
            addUpgrade("Vegetarian Buffet!", "ClickValue", "You give Gompei an assortment of the finest veggies around. He couldn't be happier! Each click now earns 6x points!", 5000, 6.0)
            addUpgrade("Higher Education", "ClickValue", "Gompei has received a world class education at WPI. He is now a true innovator! Each click now earns 10x points!", 52320, 10.0)
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