package com.colermanning.gompeiclicker

import android.content.Context
import java.lang.IllegalStateException

/**
 * GameRepository is a Singleton class that gives the app an owner for the game data
 * and provides a way to easily pass that data between controller classes.
 *
 * Repository must be initialized with initialize() before
 * calling the repository with get()
 */
class GompeiClickerRepository private constructor(context: Context) {
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