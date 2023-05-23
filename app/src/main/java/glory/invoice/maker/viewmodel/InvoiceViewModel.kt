package glory.invoice.maker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import glory.invoice.maker.database.UserDatabase
import glory.invoice.maker.model.Invoice
import glory.invoice.maker.repository.InvoiceRepository
import kotlinx.coroutines.launch

class InvoiceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InvoiceRepository

    init {
        val cartDao = UserDatabase.getDatabase(application).invoiceDao()
        repository = InvoiceRepository(cartDao)
    }

     fun allItems(id: Int): LiveData<List<Invoice>> = repository.allItem(id)
     fun invoice(id: Int): Invoice = repository.invoice(id)
    fun insert(invoice: Invoice) = viewModelScope.launch {
        repository.insert(invoice)
    }

    fun update(invoice: Invoice) = viewModelScope.launch {
        repository.update(invoice)
    }

    fun delete(invoice: Invoice) = viewModelScope.launch {
        repository.delete(invoice)
    }


}