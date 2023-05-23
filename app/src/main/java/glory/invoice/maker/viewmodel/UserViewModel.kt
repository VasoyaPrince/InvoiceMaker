package glory.invoice.maker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import glory.invoice.maker.database.UserDatabase
import glory.invoice.maker.model.UserWithClient
import glory.invoice.maker.model.UserWithInvoice
import glory.invoice.maker.model.UserWithItem
import glory.invoice.maker.model.Users
import glory.invoice.maker.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val cartDao = UserDatabase.getDatabase(application).usersDao()
        repository = UserRepository(cartDao)
        val allUser: LiveData<List<Users>> = repository.allWords
    }
    fun getUserWithClient(): LiveData<List<UserWithClient>> = repository.getUserWithClient()
    fun getUserWithItems(): LiveData<List<UserWithItem>> = repository.getUserWithItems()
    fun getUserWithInvoice(): LiveData<List<UserWithInvoice>> = repository.getUserWithInvoice()
    fun insert(user: Users) = viewModelScope.launch {
        repository.insert(user)
    }
    fun update(user: Users) = viewModelScope.launch {
        repository.update(user)
    }

      fun getUserLive(id: Int) : LiveData<Users>? = repository.getUserLive(id)

    suspend fun user(id: Int) {
        repository.getUser(id)
    }

}
