package glory.invoice.maker.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import glory.invoice.maker.database.dao.UsersDao
import glory.invoice.maker.model.UserWithClient
import glory.invoice.maker.model.UserWithInvoice
import glory.invoice.maker.model.UserWithItem
import glory.invoice.maker.model.Users

class UserRepository(private val userDao: UsersDao) {

    val allWords: LiveData<List<Users>> = userDao.getAll()

    fun getUserWithClient(): LiveData<List<UserWithClient>> = userDao.getUserWithClient()
    fun getUserWithItems(): LiveData<List<UserWithItem>> = userDao.getUserWithItems()
    fun getUserWithInvoice(): LiveData<List<UserWithInvoice>> = userDao.getUserWithInvoice()

    @WorkerThread
     fun getUserLive(id: Int) : LiveData<Users>? = userDao.getUserLive(id)


    @WorkerThread
    suspend fun insert(user: Users) {
        userDao.insert(user)
    }
    @WorkerThread
    suspend fun update(user: Users) {
        userDao.update(user)
    }

    @WorkerThread
    suspend fun getUser(id: Int) {
        userDao.getUser(id)
    }

}