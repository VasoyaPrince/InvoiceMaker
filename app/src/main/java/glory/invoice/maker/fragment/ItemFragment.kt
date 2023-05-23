package glory.invoice.maker.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import glory.invoice.maker.App
import glory.invoice.maker.ItemsAddActivity
import glory.invoice.maker.R
import glory.invoice.maker.adapter.ClientAdapter
import glory.invoice.maker.adapter.ItemAdapter
import glory.invoice.maker.databinding.FragmentItemBinding
import glory.invoice.maker.model.Items
import glory.invoice.maker.viewmodel.ItemViewModel
import glory.invoice.maker.viewmodel.UserViewModel


class ItemFragment : Fragment() {
    lateinit var binding: FragmentItemBinding
    private lateinit var activityContext: Context
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }
    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this)[ItemViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        activityContext = requireContext()

        userViewModel.getUserWithItems().observe(activityContext as FragmentActivity) { client ->
            for (i in client) {
                binding.noData.visibility = View.GONE
                binding.recyclerview.adapter =
                    ItemAdapter(activityContext, i.items, ::performOptionsMenuClick)
            }
        }

        return binding.root
    }

    private fun performOptionsMenuClick(position: Int, itemData: Items) {
        val popupMenu =
            PopupMenu(requireContext(), binding.recyclerview[position].findViewById(R.id.imageView))
        popupMenu.inflate(R.menu.menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {

            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.delete -> {
                        itemViewModel.deleteItem(id)
                        Toast.makeText(activityContext, "delete", Toast.LENGTH_SHORT).show()
                        return true
                    }

                    R.id.edit -> {
                        val intent = Intent(activityContext, ItemsAddActivity::class.java)
                        intent.putExtra("item", itemData)
                        startActivity(intent)
                        return true
                    }
                }
                return false
            }

        })
        popupMenu.show()
    }
}