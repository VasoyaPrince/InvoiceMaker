package glory.invoice.maker


import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.adapter.ItemAdapter
import glory.invoice.maker.databinding.ActivityItemsAddBinding
import glory.invoice.maker.model.Items
import glory.invoice.maker.viewmodel.ItemViewModel


class ItemsAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityItemsAddBinding
    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this)[ItemViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )

        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("item", Items::class.java)
        } else {
            intent.getParcelableExtra("item")
        }

        val edit = intent.getBooleanExtra("edit", true)


        if (edit) {
            binding.title.text = "Edit Item"
            binding.delete.visibility = View.VISIBLE
            binding.delete.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false).setPositiveButton("Yes") { dialog, id ->
                    val name = binding.item.text.toString()
                    val rate1 = binding.price.text.toString().toInt()
                    val qty1 = binding.qty.text.toString().toInt()
                    val desc = binding.desc.text.toString()
                    val total = rate1 * qty1
                    itemViewModel.delete(
                        Items(
                            item.id,
                            item = name,
                            rate = rate1,
                            qty = qty1,
                            price = total,
                            userId = item.userId
                        )
                    )
                    Toast.makeText(this@ItemsAddActivity, "deleted !!", Toast.LENGTH_SHORT).show()
                    finish()
                }.setNegativeButton(
                    "No"
                ) { dialog, _ ->
                    dialog.cancel()
                }
                val alert: AlertDialog = builder.create()
                alert.setTitle("Ary you sure to delete your item")
                alert.show()


            }

            binding.item.setText(item.item)
            binding.price.setText(item.rate.toString())
            binding.qty.setText(item.qty.toString())


            binding.add.setOnClickListener {
                val name = binding.item.text.toString()
                val rate1 = binding.price.text.toString().toInt()
                val qty1 = binding.qty.text.toString().toInt()
                val desc = binding.desc.text.toString()
                val total = rate1 * qty1
                if (checkAllFields()) {
                    itemViewModel.update(
                        Items(
                            item.id,
                            item = name,
                            rate = rate1,
                            qty = qty1,
                            price = total,
                            userId = item.userId
                        )
                    )
                    onBackPressed()
                    Toast.makeText(this@ItemsAddActivity, "Updated !!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.add.setOnClickListener {
                if (checkAllFields()) {
                    val name = binding.item.text.toString()
                    val rate1 = binding.price.text.toString().toInt()
                    val qty1 = binding.qty.text.toString().toInt()
                    val desc = binding.desc.text.toString()
                    val total = rate1 * qty1
                    itemViewModel.insert(
                        Items(
                            null, item = name, rate = rate1, qty = qty1, price = total, userId = 1
                        )
                    )
                    onBackPressed()
                    Toast.makeText(this@ItemsAddActivity, "Add !!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun checkAllFields(): Boolean {
        if (binding.item.text.isNullOrBlank() && binding.item.text.isNullOrEmpty()) {
            binding.item.error = "This field is required"
            return false
        }
        if (binding.price.text.isNullOrBlank() && binding.price.text.isNullOrEmpty()) {
            binding.price.error = "This field is required"
            return false
        }
        if (binding.qty.text.isNullOrBlank() && binding.qty.text.isNullOrEmpty()) {
            binding.qty.error = "This field is required"
            return false
        }
        return true
    }
}
