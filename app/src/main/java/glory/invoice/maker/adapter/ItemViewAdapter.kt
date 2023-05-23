package glory.invoice.maker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import glory.invoice.maker.ItemViewActivity
import glory.invoice.maker.databinding.ListItemViewLayoutBinding
import glory.invoice.maker.model.Items

class ItemViewAdapter(val context: Context, val item: List<Items>) :
    RecyclerView.Adapter<ItemViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(item: ListItemViewLayoutBinding) : RecyclerView.ViewHolder(item.root) {
        var binding = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemViewLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = item[position]
        holder.binding.itemName.text = item.item
        holder.binding.total.text = item.price.toString()
        holder.binding.item.setOnClickListener {
            val intent = Intent(context, ItemViewActivity::class.java)
            intent.putExtra("item", item)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return item.size
    }


}