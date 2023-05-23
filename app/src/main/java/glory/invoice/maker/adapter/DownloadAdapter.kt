package glory.invoice.maker.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import glory.invoice.maker.databinding.InvoiceListLayoutBinding
import java.io.File


class DownloadAdapter(
    private var context: Context,
    private var file :MutableList<File>
) : RecyclerView.Adapter<DownloadAdapter.DownloadViewModel>() {

    class DownloadViewModel(itemView: InvoiceListLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewModel {
        val binding = InvoiceListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return DownloadViewModel(binding)
    }

    override fun onBindViewHolder(holder: DownloadViewModel, position: Int) {
        val pdfDetails = file[position]
        holder.binding.senderName.text = pdfDetails.name
        holder.binding.invoiceNo.setOnClickListener {


        }
    }

    override fun getItemCount(): Int {
        return file.size
    }
}