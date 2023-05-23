package glory.invoice.maker

import android.app.Application
import glory.invoice.maker.database.UserDatabase
import glory.invoice.maker.repository.ClientRepository
import glory.invoice.maker.repository.ItemRepository
import glory.invoice.maker.repository.UserRepository

class App : Application() {
    private val database by lazy { UserDatabase.getDatabase(this) }
    val repository by lazy { UserRepository(database.usersDao()) }
    val itemRepository by lazy { ItemRepository(database.itemsDao()) }
    val clientRepository by lazy { ClientRepository(database.clientDao()) }
}