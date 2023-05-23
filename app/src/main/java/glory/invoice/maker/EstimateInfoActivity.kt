package glory.invoice.maker

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.databinding.ActivityEstimateInfoBinding

class EstimateInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEstimateInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstimateInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        //Native Ads
        AppManage.getInstance(this).showNative(
            findViewById<View>(R.id.native_container) as ViewGroup,
            AppManage.ADMOB_N[0],
            AppManage.FACEBOOK_N[0]
        )

        binding.date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this, { _, year, monthOfYear, dayOfMonth ->
                    val date = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    binding.date.text = date
                }, year, month, day
            )
            datePickerDialog.show()
        }

        binding.add.setOnClickListener {

            val invoiceNo = binding.invoiceNo.text.toString()
            val date = binding.date.text.toString()
            val accountNo = binding.accountNo.text.toString()

            if (checkAllFields()) {
                val sharedPreferences = getSharedPreferences("invoiceInfo", MODE_PRIVATE)
                val myEdit = sharedPreferences.edit()
                myEdit.putString("invoiceNo", invoiceNo)
                myEdit.putString("date", date)
                myEdit.putString("accountNo", accountNo)
                myEdit.apply()
                val intent = Intent()
                intent.putExtra("invoiceNo", invoiceNo)
                intent.putExtra("date", date)
                intent.putExtra("accountNo", accountNo)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }

    }

    private fun checkAllFields(): Boolean {

        if (binding.invoiceNo.length() == 0) {
            binding.invoiceNo.error = "This field is required"
            return false
        }
        if (binding.accountNo.length() == 0) {
            binding.accountNo.error = "This field is required"
            return false
        }
        if (binding.date.length() == 0) {
            binding.date.error = "Email is required"
            return false
        }
        return true
    }
}