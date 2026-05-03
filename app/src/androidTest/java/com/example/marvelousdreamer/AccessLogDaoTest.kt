package com.example.marvelousdreamer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelousdreamer.data.local.AppDatabase
import com.example.marvelousdreamer.data.local.dao.AccessLogDao
import com.example.marvelousdreamer.data.local.entity.AccessLogEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccessLogDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var accessLogDao: AccessLogDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        accessLogDao = db.accessLogDao()
    }

    @After
    fun tearDown() { db.close() }

    @Test
    fun insertAndGetLogs() = runBlocking {
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGIN", timestamp = 1000L))
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGOUT", timestamp = 2000L))

        val logs = accessLogDao.getLogsByUser("u1")
        assertEquals(2, logs.size)
    }

    @Test
    fun logsOrderedByTimestampDesc() = runBlocking {
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGIN", timestamp = 1000L))
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGOUT", timestamp = 3000L))
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGIN", timestamp = 2000L))

        val logs = accessLogDao.getLogsByUser("u1")
        assertEquals(3000L, logs[0].timestamp)
        assertEquals(2000L, logs[1].timestamp)
        assertEquals(1000L, logs[2].timestamp)
    }

    @Test
    fun getLogsByUser_onlyReturnsUserLogs() = runBlocking {
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGIN", timestamp = 1000L))
        accessLogDao.insert(AccessLogEntity(userId = "u2", action = "LOGIN", timestamp = 2000L))
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGOUT", timestamp = 3000L))

        val logs = accessLogDao.getLogsByUser("u1")
        assertEquals(2, logs.size)
    }

    @Test
    fun autoGenerateId() = runBlocking {
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGIN", timestamp = 1000L))
        accessLogDao.insert(AccessLogEntity(userId = "u1", action = "LOGOUT", timestamp = 2000L))

        val logs = accessLogDao.getLogsByUser("u1")
        assertNotEquals(logs[0].id, logs[1].id)
    }
}
