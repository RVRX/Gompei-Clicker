package com.colermanning.gompeiclicker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Will just be one row, and will contain current game info
 */
@Entity
data class Game(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var currentPoints: Int = 0,
                var multiplier: Double = 0.0)