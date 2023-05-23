package glory.invoice.maker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.databinding.ActivityItemViewBinding
import glory.invoice.maker.model.Items
import glory.invoice.maker.viewmodel.ItemViewModel

class ItemViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityItemViewBinding
    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this)[ItemViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("item", Items::class.java)
        } else {
            intent.getParcelableExtra("item")
        }

        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )

        binding.item.setText(item.item)
        binding.price.setText(item.rate.toString())
        binding.qty.setText(item.qty.toString())

        binding.add.setOnClickListener {
            val name = binding.item.text.toString()
            val rate = binding.price.text.toString().toInt()
            val qty = binding.qty.text.toString().toInt()
            val desc = binding.desc.text.toString()
            val total = rate * qty
            if (checkAllFields()) {
                val items = Items(item.id, item.userId, name, rate, qty, total)
                val intent = intent.putExtra("items", items)
                setResult(101, intent)
                Toast.makeText(this@ItemViewActivity, "done !!", Toast.LENGTH_SHORT).show()
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