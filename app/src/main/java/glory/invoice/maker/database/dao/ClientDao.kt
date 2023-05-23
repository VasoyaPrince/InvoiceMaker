package glory.invoice.maker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import glory.invoice.maker.model.Client

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun item(client: Client)

    @Query("SELECT * FROM client WHERE userId = :id")
    fun getItems(id: Int): LiveData<List<Client>>

    @Update
    suspend fun update(client: Client)

    @Delete
    suspend fun delete(client: Client)

    @Query("DELETE FROM client WHERE id = :ID ")
    suspend fun deleteItem(ID: Int)

}