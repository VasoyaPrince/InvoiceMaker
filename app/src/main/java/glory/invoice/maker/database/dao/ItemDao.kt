package glory.invoice.maker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import glory.invoice.maker.model.Items

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun item(items: Items)

    @Query("SELECT * FROM Items WHERE userId = :id")
    fun getItems(id: Int): LiveData<List<Items>>

    @Query("SELECT * FROM Items WHERE userId = :id")
    suspend fun getAllItems(id: Int): List<Items>

    @Update
    suspend fun update(items: Items)

    @Delete
    suspend fun delete(model: Items)

    @Query("DELETE FROM items WHERE id = :ID ")
    suspend fun deleteItem(ID: Int)

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItem(id:Int) : Items

}