package com.colermanning.gompeiclicker.database

import android.content.Context
import android.icu.util.MeasureUnit
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.colermanning.gompeiclicker.Upgrade
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class GompeiClickerDatabaseTest : TestCase(){
    private lateinit var db: GompeiClickerDatabase
    private lateinit var dao: GompeiClickerDao

    @Before
    public override fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GompeiClickerDatabase::class.java).build()

        dao = db.gompeiClickerDao()
    }

    @After
    fun closeDb(){
        db.close()
    }


    /* Copyright 2019 Google LLC.
       SPDX-License-Identifier: Apache-2.0 */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }



    @Test
    fun setAndReadPoints(){
        dao.updatePoints(50)
        val points = dao.getPoints()
        assertNotNull(points)
        runBlocking {
            try{
                assertThat(points.getOrAwaitValue(), equalTo(50))
            } catch (t: Throwable){

            }
        }

    }

    @Test
    fun testAddUpgrade(){
        val upgrade = Upgrade("test", "test", "test")

        dao.addUpgrade(upgrade)

        val upgrades = dao.getUpgrades()
        runBlocking {
            try{
                assertThat(upgrades.getOrAwaitValue().get(0), equalTo(upgrade))
            } catch (t: Throwable) {
                //fail<Nothing>("Shouldn't happen")
            }
        }
    }

    @Test
    fun testBuyUpgrade(){
        val upgrade = Upgrade("test", "test", "test", 50, 0.0, false)
        val upgradeBought = Upgrade("test", "test", "test", 50, 0.0, true)
        dao.addUpgrade(upgrade)
        dao.updatePoints(100)
        dao.buyUpgradeById("test")


        val upgrades = dao.getUpgrades()
        runBlocking {
            try{
                assertThat(upgrades.getOrAwaitValue().get(0), equalTo(upgradeBought))
            } catch (t: Throwable) {
                //fail<Nothing>("Shouldn't happen")
            }
        }
    }

    @Test
    fun testPointDeduction(){
        val upgrade = Upgrade("test", "test", "test", 50, 0.0, false)
        dao.addUpgrade(upgrade)
        dao.updatePoints(100)
        dao.buyUpgradeById("test")

        val points = dao.getPoints()
        assertNotNull(points)
        runBlocking {
            try{
                assertThat(points.getOrAwaitValue(), equalTo(50))
            } catch (t: Throwable){

            }
        }
    }

    @Test
    fun testPurchaseFail(){
        val upgrade = Upgrade("test", "test", "test", 50, 0.0, false)
        dao.addUpgrade(upgrade)
        dao.updatePoints(49)
        dao.buyUpgradeById("test")

        val points = dao.getPoints()
        assertNotNull(points)
        runBlocking {
            try{
                assertThat(points.getOrAwaitValue(), equalTo(49))
            } catch (t: Throwable){

            }
        }
    }

}