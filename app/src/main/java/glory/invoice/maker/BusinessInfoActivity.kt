package glory.invoice.maker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.databinding.ActivityBusinessInfoBinding
import glory.invoice.maker.model.Users
import glory.invoice.maker.viewmodel.UserViewModel
import java.io.File
import java.io.FileNotFoundException


class BusinessInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityBusinessInfoBinding
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }
    private var image: Bitmap? = null
    private var istrue = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            onBackPressed()
        }

        //loadInterstitialAd
        AppManage.getInstance(this).loadInterstitialAd(
            this, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )

        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )
        AppManage.getInstance(this@BusinessInfoActivity).showNativeBanner(binding.linearLayout)

        binding.shapeableImageView.setOnClickListener {
            chooseImageGallery()
        }

        userViewModel.getUserLive(1)?.observe(this@BusinessInfoActivity) { user ->
            if (user != null) {
                binding.name.setText(user.name)
                binding.email.setText(user.email)
                binding.address.setText(user.address)
                binding.phone.setText(user.phone)
                binding.website.setText(user.website)
                binding.shapeableImageView.setImageBitmap(user.image)
            } else {
                istrue = false
            }
        }

        binding.add.setOnClickListener {
            if (istrue) {

                val name = binding.name.text.toString()
                val email = binding.email.text.toString()
                val address = "${binding.address.text} \n ${binding.address2.text}"
                val phone = binding.phone.text.toString()
                val website = binding.website.text.toString()


                if (checkAllFields()) {
                    if (image != null) {
                        val user = Users(
                            1, image, name, email, phone, address, website
                        )
                        userViewModel.update(user)
                        onBackPressed()
                        Toast.makeText(this@BusinessInfoActivity, "Done !!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Toast.makeText(this@BusinessInfoActivity, "Select image !!", Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                val name = binding.name.text.toString()
                val email = binding.email.text.toString()
                val address = "${binding.address.text} \n ${binding.address2.text}"
                val phone = binding.phone.text.toString()
                val website = binding.website.text.toString()

                if (checkAllFields()) {
                    val user = Users(
                        1, image, name, email, phone, address, website
                    )

                    userViewModel.insert(user)
                    onBackPressed()
                    Toast.makeText(this@BusinessInfoActivity, "Done !!", Toast.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
                val myEdit = sharedPreferences.edit()
                myEdit.putString("logo", data.data.toString())
                myEdit.apply()
                image = decodeUriAsBitmap(this@BusinessInfoActivity, data.data!!)
                binding.shapeableImageView.setImageURI(data.data)
            }
        }
    }

    private fun decodeUriAsBitmap(context: Context, uri: Uri): Bitmap? {
        val bitmap: Bitmap? = try {
            BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(uri)
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
        return bitmap
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100)
    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }
}