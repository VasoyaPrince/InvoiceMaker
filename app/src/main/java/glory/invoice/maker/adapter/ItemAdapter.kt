package glory.invoice.maker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import glory.invoice.maker.ClientAddActivity
import glory.invoice.maker.ItemsAddActivity
import glory.invoice.maker.model.Item
import glory.invoice.maker.databinding.ItemsLayoutBinding
import glory.invoice.maker.model.Items

class ItemAdapter(val context: Context, val item: List<Items>,
                  private var optionsMenuClickListener : (Int,Items) ->Unit) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(item: ItemsLayoutBinding) : RecyclerView.ViewHolder(item.root) {
        var binding = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = item[position]
        holder.binding.itemName.text = item.item
        holder.binding.price.text = item.price.toString()
        holder.binding.item.setOnClickListener {
            val intent = Intent(context, ItemsAddActivity::class.java)
            intent.putExtra("item", item)
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