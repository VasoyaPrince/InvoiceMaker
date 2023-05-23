package glory.invoice.maker.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import glory.invoice.maker.database.dao.InvoiceDao
import glory.invoice.maker.model.Invoice

class InvoiceRepository(private val invoiceDao: InvoiceDao) {

     fun allItem(id: Int): LiveData<List<Invoice>> = invoiceDao.getInvoice(id)
     fun invoice(id: Int): Invoice = invoiceDao.invoice(id)

    @WorkerThread
    suspend fun insert(invoice: Invoice) {
        invoiceDao.item(invoice)
    }

    @WorkerThread
    suspend fun update(invoice: Invoice) {
        invoiceDao.update(invoice)
    }

    @WorkerThread
    suspend fun delete(invoice: Invoice) {
        invoiceDao.delete(invoice)
    }

}