package glory.invoice.maker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.adapter.ClientViewAdapter
import glory.invoice.maker.databinding.ActivityAddClientBinding
import glory.invoice.maker.viewmodel.ClientViewModel

class AddClientActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddClientBinding
    private val clientViewModel: ClientViewModel by lazy {
        ViewModelProvider(this)[ClientViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addItem.setOnClickListener {
            val intent = Intent(this@AddClientActivity, ClientAddActivity::class.java)
            intent.putExtra("edit", false)
            startActivity(intent)
        }


        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )
        AppManage.getInstance(this@AddClientActivity).showNativeBanner(binding.linearLayout)

        clientViewModel.allItems(1).observe(this@AddClientActivity) { words ->
            words?.let {
                if (it.isEmpty()) {
                    Toast.makeText(this@AddClientActivity, "add client", Toast.LENGTH_SHORT).show()
                } else {
                    binding.recyclerview.adapter = ClientViewAdapter(this@AddClientActivity, it)
                }
            }
        }
    }
}