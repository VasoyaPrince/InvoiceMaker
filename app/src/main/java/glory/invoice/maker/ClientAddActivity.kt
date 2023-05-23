package glory.invoice.maker

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.databinding.ActivityClientAddBinding
import glory.invoice.maker.model.Client
import glory.invoice.maker.model.Items
import glory.invoice.maker.viewmodel.ClientViewModel
import glory.invoice.maker.viewmodel.ItemViewModel

class ClientAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientAddBinding
    private val clientViewModel: ClientViewModel by lazy {
        ViewModelProvider(this)[ClientViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )
        AppManage.getInstance(this@ClientAddActivity).showNativeBanner(binding.linearLayout)

        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("client", Client::class.java)
        } else {
            intent.getParcelableExtra("client")
        }
        val edit = intent.getBooleanExtra("edit", true)
        binding.back.setOnClickListener {
            onBackPressed()
        }
        if (edit) {
            binding.title.text = "Client"
            binding.name.setText(item!!.name)
            binding.email.setText(item.email)
            binding.phone.setText(item.phone)
            binding.address.setText(item.address)
            binding.delete.visibility = View.VISIBLE
            binding.delete.setOnClickListener {
                val name = binding.name.text.toString()
                val email = binding.email.text.toString()
                val address = "${binding.address.text} \n ${binding.address2.text}"
                val phone = binding.phone.text.toString()
                if(checkAllFields()){
                    val user = Client(
                        item.id, item.userId, name, email, phone, address
                    )
                    clientViewModel.delete(user)
                    onBackPressed()
                    Toast.makeText(this@ClientAddActivity, "deleted !!", Toast.LENGTH_SHORT).show()
                }
            }

            binding.add.setOnClickListener {
                val name = binding.name.text.toString()
                val email = binding.email.text.toString()
                val address = "${binding.address.text} \n ${binding.address2.text}"
                val phone = binding.phone.text.toString()
                if(checkAllFields()){
                    val user = Client(
                        item.id, item.userId, name, email, phone, address
                    )
                    clientViewModel.update(user)
                    onBackPressed()
                    Toast.makeText(this@ClientAddActivity, "updated !!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {

            binding.add.setOnClickListener {
                val name = binding.name.text.toString()
                val email = binding.email.text.toString()
                val address = "${binding.address.text} \n ${binding.address2.text}"
                val phone = binding.phone.text.toString()

                if(checkAllFields()){
                    val user = Client(
                        null, 1, name, email, phone, address
                    )
                    clientViewModel.insert(user)
                    onBackPressed()
                    Toast.makeText(this@ClientAddActivity, "Done !!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun checkAllFields(): Boolean {

        if (binding.name.length() == 0) {
            binding.name.error = "This field is required"
            return false
        }
        if (binding.address.length() == 0) {
            binding.address.error = "This field is required"
            return false
        }
        if (binding.email.length() == 0) {
            binding.email.error = "Email is required"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.text!!.trim()).matches()) {
            binding.email.error = "Email is No Match"
            return false
        }

        if (binding.phone.length() == 0) {
            binding.phone.error = "Phone Number is required"
            return false
        }
        return true
    }
}