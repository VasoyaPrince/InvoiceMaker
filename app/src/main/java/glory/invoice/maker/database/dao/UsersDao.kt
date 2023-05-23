package glory.invoice.maker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import glory.invoice.maker.model.UserWithClient
import glory.invoice.maker.model.UserWithInvoice
import glory.invoice.maker.model.UserWithItem
import glory.invoice.maker.model.Users

@Dao
interface UsersDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<Users>>

    @Query("SELECT * FROM user where id = :id")
     fun getUserLive(id: Int): LiveData<Users>?

    @Query("SELECT * FROM user where id = :id")
    suspend fun getUser(id: Int): Users?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: Users)

    @Delete
    suspend fun delete(users: Users)

    @Update
    suspend fun update(users: Users)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM user")
    fun getUserWithClient(): LiveData<List<UserWithClient>>
    @Transaction
    @Query("SELECT * FROM user")
    fun getUserWithItems(): LiveData<List<UserWithItem>>

    @Transaction
    @Query("SELECT * FROM user")
    fun getUserWithInvoice(): LiveData<List<UserWithInvoice>>
}