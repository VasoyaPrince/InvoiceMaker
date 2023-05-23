package glory.invoice.maker.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import glory.invoice.maker.EstimateAddActivity
import glory.invoice.maker.databinding.ClientViewLayoutBinding
import glory.invoice.maker.model.Client


class ClientViewAdapter(val context: Context, val item: List<Client>) :
    RecyclerView.Adapter<ClientViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(item: ClientViewLayoutBinding) : RecyclerView.ViewHolder(item.root) {
        var binding = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ClientViewLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = item[position]
        holder.binding.clientName.text = item.name
        holder.binding.phone.text = item.phone
        holder.binding.client.setOnClickListener {
            holder.binding.isSelected.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {

                }
            }
            val sharedPreferences = context.getSharedPreferences(
                "client", AppCompatActivity.MODE_PRIVATE
            )
            val myEdit = sharedPreferences.edit()
            myEdit.putString("name", item.name)
            myEdit.putString("email", item.email)
            myEdit.putString("phone", item.phone)
            myEdit.putString("address1", item.address)
            myEdit.putString("id", item.id.toString())
            myEdit.putString("userId", item.userId.toString())
            myEdit.apply()
            context.startActivity(Intent(context, EstimateAddActivity::class.java))
            (context as Activity).finish()
        }
    }


    override fun getItemCount(): Int {
        return item.size
    }


}