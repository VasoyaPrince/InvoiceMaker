package glory.invoice.maker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Native Ads
        AppManage.getInstance(this).showNative(
            findViewById<View>(R.id.native_container) as ViewGroup,
            AppManage.ADMOB_N[0],
            AppManage.FACEBOOK_N[0]
        )

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.info.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, BusinessInfoActivity::class.java))
        }

        binding.shareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Invoice Maker")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage = """
                ${shareMessage}https://play.google.com/store/apps/details?id=${applicationContext.packageName}
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Share App"))
        }

        binding.rateUs.setOnClickListener {
            val uri = Uri.parse("market://details?id=$packageName")
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, " unable to find app", Toast.LENGTH_LONG).show()
            }
        }

        binding.feedBack.setOnClickListener {
            val uri = Uri.parse("market://details?id=$packageName")
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, " unable to find app", Toast.LENGTH_LONG).show()
            }
        }
    }
}