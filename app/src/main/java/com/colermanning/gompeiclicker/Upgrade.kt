package com.colermanning.gompeiclicker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Each row is a specific upgrade
 * @param id an name for the upgrade
 * @param upgradeType what kind of upgrade (Speed, value...)
 * @param bought has the upgrade been bought
 */
@Entity(tableName = "upgrade_table")
data class Upgrade(@PrimaryKey var id: String = "",
var upgradeType: String = "",
var modifier: Double = 0.0,
var bought: Boolean = false)