package glory.invoice.maker.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import glory.invoice.maker.ClientAddActivity
import glory.invoice.maker.EstimateAddActivity
import glory.invoice.maker.ItemViewActivity
import glory.invoice.maker.ItemsAddActivity
import glory.invoice.maker.R
import glory.invoice.maker.databinding.InvoiceListLayoutBinding
import glory.invoice.maker.databinding.ItemViewLayoutBinding
import glory.invoice.maker.model.Item
import glory.invoice.maker.databinding.ItemsLayoutBinding
import glory.invoice.maker.databinding.ListItemViewLayoutBinding
import glory.invoice.maker.model.Invoice
import glory.invoice.maker.model.Items

class InvoiceAdapter(val context: Context, val item: List<Invoice>) :
    RecyclerView.Adapter<InvoiceAdapter.ItemViewHolder>() {

    class ItemViewHolder(item: InvoiceListLayoutBinding) : RecyclerView.ViewHolder(item.root) {
        var binding = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = InvoiceListLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = item[position]
        holder.binding.invoiceNo.text = item.invoiceNo
        holder.binding.total.text = "${item.total}₹"
        holder.binding.date.text = item.date
        holder.binding.senderName.text = item.senderName
//        holder.binding.invoice.setOnLongClickListener {
//            true
//        }

    }

    override fun getItemCount(): Int {
        return item.size
    }

}