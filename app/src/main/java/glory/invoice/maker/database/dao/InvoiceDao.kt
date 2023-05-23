package glory.invoice.maker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import glory.invoice.maker.model.Invoice

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun item(invoice: Invoice)

    @Query("SELECT * FROM invoice WHERE id = :id")
     fun getInvoice(id: Int): LiveData<List<Invoice>>

     @Query("SELECT * FROM invoice WHERE id = :id")
     fun invoice(id: Int): Invoice

    @Update
    suspend fun update(invoice: Invoice)

    @Delete
    suspend fun delete(invoice: Invoice)
}