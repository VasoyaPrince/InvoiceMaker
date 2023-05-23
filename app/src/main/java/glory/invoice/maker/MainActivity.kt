package glory.invoice.maker


import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.adapter.ClientViewAdapter
import glory.invoice.maker.fragment.ClientFragment
import glory.invoice.maker.fragment.EstimateFragment
import glory.invoice.maker.fragment.InvoiceFragment
import glory.invoice.maker.fragment.ItemFragment
import glory.invoice.maker.databinding.ActivityMainBinding
import glory.invoice.maker.databinding.NavHeaderMainBinding
import glory.invoice.maker.model.Users
import glory.invoice.maker.viewmodel.ClientViewModel
import glory.invoice.maker.viewmodel.UserViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private var image: Bitmap? = null
    private var name: String? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    @SuppressLint("WrongThread", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewHeader = binding.navigationView.getHeaderView(0)
        val navViewHeaderBinding: NavHeaderMainBinding = NavHeaderMainBinding.bind(viewHeader)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, binding.myDrawerLayout, R.string.nav_open, R.string.nav_close
        )

        //loadInterstitialAd
        AppManage.getInstance(this).loadInterstitialAd(
            this,
            AppManage.ADMOB_I[0],
            AppManage.FACEBOOK_I[0]
        )

        //banner ads
        AppManage.getInstance(this).showNativeBanner(
            binding.appbar.main.linearLayout, AppManage.ADMOB_I[0], AppManage.FACEBOOK_I[0]
        )
        AppManage.getInstance(this@MainActivity).showNativeBanner(binding.appbar.main.linearLayout)

        userViewModel.getUserLive(1)?.observe(this@MainActivity) {
            if (it != null) {
                image = it.image
                navViewHeaderBinding.shapeableImageView.setImageBitmap(image)
                navViewHeaderBinding.businessName.text = "${it.name}"
            }
        }



        if (ContextCompat.checkSelfPermission(
                this@MainActivity, READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity, READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(READ_EXTERNAL_STORAGE), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(READ_EXTERNAL_STORAGE), 1
                )
            }
        }


        val toggle = ActionBarDrawerToggle(
            this,
            binding.myDrawerLayout,
            binding.appbar.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.myDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)

        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, InvoiceFragment()).commit()
        binding.appbar.bottomNavView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {

                R.id.nav_estimate -> {
                    fragment = EstimateFragment()
                    binding.appbar.floatingButton.setOnClickListener {
                        startActivity(Intent(this@MainActivity, EstimateAddActivity::class.java))
                    }
                    binding.appbar.appbarTitle.text = "Estimate"
                }

                R.id.nav_client -> {
                    fragment = ClientFragment()
                    binding.appbar.floatingButton.setOnClickListener {
                        val intent = Intent(this@MainActivity, ClientAddActivity::class.java)
                        intent.putExtra("edit", false)
                        startActivity(intent)
                    }
                    binding.appbar.appbarTitle.text = "Client"
                }

                R.id.nav_items -> {
                    fragment = ItemFragment()
                    binding.appbar.floatingButton.setOnClickListener {
                        val intent = Intent(this@MainActivity, ItemsAddActivity::class.java)
                        intent.putExtra("edit", false)
                        startActivity(intent)
                    }
                    binding.appbar.appbarTitle.text = "Item"
                }
            }

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit()
            true
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_Sync -> {
            }

            R.id.nav_input -> {
            }

            R.id.Settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
        val drawer = findViewById<DrawerLayout>(R.id.my_drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            this@MainActivity, READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
                return
            }
        }
    }
}