package glory.invoice.maker.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import glory.invoice.maker.database.dao.ItemDao
import glory.invoice.maker.model.Items

class ItemRepository(private val itemDao: ItemDao) {
    fun allItem(id: Int): LiveData<List<Items>> = itemDao.getItems(id)
    suspend fun getAllItem(id: Int): List<Items> = itemDao.getAllItems(id)

    @WorkerThread
    suspend fun insert(item: Items) {
        itemDao.item(item)
    }

    @WorkerThread
    suspend fun update(item: Items) {
        itemDao.update(item)
    }

    @WorkerThread
    suspend fun delete(item: Items) {
        itemDao.delete(item)
    }

    @WorkerThread
    suspend fun deleteItem(id: Int) {
        itemDao.deleteItem(id)
    }

    @WorkerThread
    suspend fun getItem(id: Int) {
        itemDao.getItem(id)
    }
}