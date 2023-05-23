package glory.invoice.maker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import glory.invoice.maker.database.UserDatabase
import glory.invoice.maker.model.Client
import glory.invoice.maker.model.UserWithClient
import glory.invoice.maker.repository.ClientRepository
import kotlinx.coroutines.launch

class ClientViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: ClientRepository

    init {
        val productDao = UserDatabase.getDatabase(application).clientDao()
        repository = ClientRepository(productDao)
    }

    fun allItems(id: Int): LiveData<List<Client>> = repository.allClient(id)

    fun insert(item: Client) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: Client) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: Client) = viewModelScope.launch {
        repository.delete(item)
    }

    fun deleteItem(id: Int) = viewModelScope.launch {
        repository.deleteItem(id)
    }

}

//class ClientViewModelFactory(private val repository: ClientRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ClientViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}