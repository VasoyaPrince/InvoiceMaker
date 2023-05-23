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
import glory.invoice.maker.ClientAddActivity
import glory.invoice.maker.R
import glory.invoice.maker.adapter.ClientAdapter
import glory.invoice.maker.databinding.FragmentClientBinding
import glory.invoice.maker.model.Client
import glory.invoice.maker.viewmodel.ClientViewModel
import glory.invoice.maker.viewmodel.ItemViewModel
import glory.invoice.maker.viewmodel.UserViewModel


class ClientFragment : Fragment() {
    lateinit var binding: FragmentClientBinding
    private lateinit var activityContext: Context
    val clientViewModel: ClientViewModel by lazy {
        ViewModelProvider(this)[ClientViewModel::class.java]
    }
    val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentClientBinding.inflate(inflater, container, false)
        activityContext = requireActivity()


        userViewModel.getUserWithClient().observe(activityContext as FragmentActivity) { client ->
            for (i in client) {
                binding.noData.visibility = View.GONE
                binding.recyclerview.adapter =
                    ClientAdapter(activityContext, i.client, ::performOptionsMenuClick)
            }
        }
        return binding.root
    }

    private fun performOptionsMenuClick(position: Int, itemData: Client) {
        val popupMenu =
            PopupMenu(requireContext(), binding.recyclerview[position].findViewById(R.id.imageView))
        popupMenu.inflate(R.menu.menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {

            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.delete -> {
                        clientViewModel.deleteItem(itemData.id!!)
                        Toast.makeText(activityContext, "delete", Toast.LENGTH_SHORT).show()
                        return true
                    }

                    R.id.edit -> {
                        val intent = Intent(activityContext, ClientAddActivity::class.java)
                        intent.putExtra("client", itemData)
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