package com.example.marvelousdreamer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelousdreamer.data.local.AppDatabase
import com.example.marvelousdreamer.data.local.dao.UserDao
import com.example.marvelousdreamer.data.local.entity.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun tearDown() { db.close() }

    private fun sampleUser(id: String = "u1", username: String = "adria") =
        UserEntity(id = id, login = "adria@test.com", username = username)

    @Test
    fun insertAndGetUser() = runBlocking {
        userDao.insert(sampleUser())
        val user = userDao.getUserById("u1")
        assertNotNull(user)
        assertEquals("adria", user?.username)
    }

    @Test
    fun updateUser() = runBlocking {
        userDao.insert(sampleUser())
        userDao.update(sampleUser().copy(username = "updated", address = "Barcelona"))
        val user = userDao.getUserById("u1")
        assertEquals("updated", user?.username)
        assertEquals("Barcelona", user?.address)
    }

    @Test
    fun isUsernameTaken_detectsDuplicate() = runBlocking {
        userDao.insert(sampleUser(id = "u1", username = "adria"))
        val taken = userDao.isUsernameTaken("adria")
        assertTrue(taken)
    }

    @Test
    fun isUsernameTaken_excludesSelf() = runBlocking {
        userDao.insert(sampleUser(id = "u1", username = "adria"))
        val taken = userDao.isUsernameTaken("adria", excludeId = "u1")
        assertFalse(taken)
    }

    @Test
    fun isUsernameTaken_notTaken() = runBlocking {
        userDao.insert(sampleUser(id = "u1", username = "adria"))
        val taken = userDao.isUsernameTaken("vitor")
        assertFalse(taken)
    }

    @Test
    fun getUserById_nonExistent() = runBlocking {
        val user = userDao.getUserById("ghost")
        assertNull(user)
    }
}
