package com.colermanning.gompeiclicker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.colermanning.gompeiclicker.Game
import com.colermanning.gompeiclicker.Upgrade

/**
 * Database with two tables, one for Game, and one for Upgrade
 * todo, p 224
 */
@Database(entities = [ Game::class, Upgrade::class ], version = 1)
abstract class GompeiClickerDatabase : RoomDatabase() {
}