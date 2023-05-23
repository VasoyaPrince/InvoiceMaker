package glory.invoice.maker.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import glory.invoice.maker.database.dao.ClientDao
import glory.invoice.maker.model.Client
import glory.invoice.maker.model.UserWithClient

class ClientRepository(private val clientDao: ClientDao) {
    fun allClient(id: Int): LiveData<List<Client>> = clientDao.getItems(id)

    @WorkerThread
    suspend fun insert(client: Client) {
        clientDao.item(client)
    }

    @WorkerThread
    suspend fun update(client: Client) {
        clientDao.update(client)
    }

    @WorkerThread
    suspend fun delete(client: Client) {
        clientDao.delete(client)
    }

    @WorkerThread
    suspend fun deleteItem(id:Int) {
        clientDao.deleteItem(id)
    }
}