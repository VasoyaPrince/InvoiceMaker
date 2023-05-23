package glory.invoice.maker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.adapter.ItemViewAdapter
import glory.invoice.maker.databinding.ActivityAddActivityBinding
import glory.invoice.maker.viewmodel.ItemViewModel

class AddActivityActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddActivityBinding
    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this)[ItemViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addItem.setOnClickListener {
            val intent = Intent(this@AddActivityActivity, ItemsAddActivity::class.java)
            intent.putExtra("edit", false)
            startActivity(intent)
        }

        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )
        AppManage.getInstance(this@AddActivityActivity).showNativeBanner(binding.linearLayout)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        itemViewModel.allItems(1).observe(this@AddActivityActivity) { item ->
            item?.let {
                if (it.isEmpty()) {
                    Toast.makeText(this@AddActivityActivity, "add Items", Toast.LENGTH_SHORT).show()
                } else {
                    binding.recyclerview.adapter = ItemViewAdapter(this@AddActivityActivity, it)
                }
            }
        }

    }
}