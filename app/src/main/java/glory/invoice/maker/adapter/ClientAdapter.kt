package glory.invoice.maker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import glory.invoice.maker.ClientAddActivity
import glory.invoice.maker.databinding.ClientLayoutBinding
import glory.invoice.maker.model.Item
import glory.invoice.maker.databinding.ItemsLayoutBinding
import glory.invoice.maker.model.Client
import glory.invoice.maker.model.Items

class ClientAdapter(val context :Context, val item: List<Client>, private var optionsMenuClickListener : (Int,Client) ->Unit) : RecyclerView.Adapter<ClientAdapter.ItemViewHolder>() {

    class ItemViewHolder(item: ClientLayoutBinding) :
        RecyclerView.ViewHolder(item.root) {
        var binding = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ClientLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = item[position]
        holder.binding.itemName.text = item.name
        holder.binding.phone.text = item.phone
        holder.binding.item.setOnClickListener {
            val intent = Intent(context,ClientAddActivity::class.java)
            intent.putExtra("client",item)
            context.startActivity(intent)
        }
        holder.binding.imageView.setOnClickListener {
            optionsMenuClickListener(position,item)
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int {
        return item.size
    }
}