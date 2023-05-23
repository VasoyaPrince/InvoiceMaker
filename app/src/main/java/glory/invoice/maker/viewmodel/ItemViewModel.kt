package glory.invoice.maker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import glory.invoice.maker.database.UserDatabase
import glory.invoice.maker.model.Items
import glory.invoice.maker.repository.ItemRepository
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepository

    init {
        val cartDao = UserDatabase.getDatabase(application).itemsDao()

        repository = ItemRepository(cartDao)
    }

    fun allItems(id: Int): LiveData<List<Items>> = repository.allItem(id)
    suspend fun getAllItems(id: Int): List<Items> = repository.getAllItem(id)
    fun insert(item: Items) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: Items) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: Items) = viewModelScope.launch {
        repository.delete(item)
    }

    fun deleteItem(id: Int) = viewModelScope.launch {
        repository.deleteItem(id)
    }

    fun getItem(id: Int) = viewModelScope.launch {
        repository.getItem(id)
    }
}

//class ItemViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ItemViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}