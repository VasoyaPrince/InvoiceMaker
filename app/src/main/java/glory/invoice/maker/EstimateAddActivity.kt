package glory.invoice.maker

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.pesonal.adsdk.AppManage
import glory.invoice.maker.adapter.ItemAddAdapter
import glory.invoice.maker.adapter.PdfDocumentAdapter
import glory.invoice.maker.databinding.ActivityEstimateAddBinding
import glory.invoice.maker.model.Invoice
import glory.invoice.maker.model.Items
import glory.invoice.maker.viewmodel.InvoiceViewModel
import glory.invoice.maker.viewmodel.ItemViewModel
import glory.invoice.maker.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class EstimateAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityEstimateAddBinding
    private val itemViewModel: ItemViewModel by lazy {
        ViewModelProvider(this)[ItemViewModel::class.java]
    }
    private val invoiceViewModel: InvoiceViewModel by lazy {
        ViewModelProvider(this)[InvoiceViewModel::class.java]
    }
    private lateinit var filePhoto: File
    private lateinit var imageUri: Uri
    private val outputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }
    private var subtotal = 0
    private var discount = 0
    private var tax = 0
    private var total = 0
    private var invoiceNo: String = ""
    private var accountNo: String = ""
    private var date: String = ""
    private var clientName: String = ""
    private var email: String = ""
    private var clientEmail: String = ""
    private var address1: String = ""
    private var clientAddress: String = ""
    private var address2: String = ""
    private var phone: String = ""
    private var clientPhone: String = ""
    private var clientId: String = ""
    private var website = ""
    private var logo = ""
    private var taxString = ""
    private var image: Bitmap? = null
    private lateinit var items: ArrayList<Items>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstimateAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getStringExtra("invoiceId")

        if (item != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val item = invoiceViewModel.invoice(item.toInt())
                binding.invoiceNo.text = item.invoiceNo
                binding.createdDate.text = item.date
                binding.dueDate.text = item.accountNo
                binding.name.text = item.senderName
                binding.clientName.text = item.receiverName
                binding.Item.adapter = ItemAddAdapter(this@EstimateAddActivity, item.items)
                for (i in item.items.indices) {
                    subtotal += item.items[i].price
                }
                binding.subTotal.text = "₹ $subtotal"
                binding.taxadd.text = item.tax
                binding.mainTotal.text = item.total
                binding.signatureView.setImageBitmap(item.sign)
                binding.trems.text = item.terms
                binding.paymentDetiles.text = item.payPal
            }
        }

        val client = getSharedPreferences("client", MODE_PRIVATE)
        val user = getSharedPreferences("user", MODE_PRIVATE)
        val invoiceInfo = getSharedPreferences("invoiceInfo", MODE_PRIVATE)
        if (client != null) {
            clientId = client.getString("id", "")!!
            clientName = client.getString("name", "")!!
            clientEmail = client.getString("email", "")!!
            clientAddress = client.getString("address1", "")!!
            clientPhone = client.getString("phone", "")!!
            binding.clientName.text = clientName
        }
        invoiceNo = invoiceInfo.getString("invoiceNo", "")!!
        date = invoiceInfo.getString("date", "")!!
        accountNo = invoiceInfo.getString("accountNo", "")!!
        binding.invoiceNo.text = invoiceNo
        binding.dueDate.text = accountNo
        binding.createdDate.text = date
        Log.d("TAG", "onCreate:$invoiceNo , $date , $accountNo ")
        taxString = user.getString("term", "")!!
        userViewModel.getUserLive(1)?.observe(this@EstimateAddActivity) { user ->
            if (user != null) {
                binding.name.text = user.name
                email = user.email
                phone = user.phone
                address1 = user.address
            }
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        userViewModel.getUserLive(1)?.observe(this@EstimateAddActivity) {
            image = it.image
        }

        items = ArrayList()
        CoroutineScope(Dispatchers.Main).launch {
            val item1 = itemViewModel.getAllItems(1)
            binding.Item.adapter = ItemAddAdapter(this@EstimateAddActivity, item1)
            for (i in item1.indices) {
                subtotal += item1[i].price
            }
            binding.subTotal.text = "₹ $subtotal"
            binding.mainTotal.text = "₹ $subtotal"
            items.addAll(item1)
        }

        binding.cardView2.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@EstimateAddActivity, EstimateInfoActivity::class.java
                ), 100
            )
        }

        binding.from.setOnClickListener {
            startActivity(Intent(this@EstimateAddActivity, BusinessInfoActivity::class.java))
        }

        binding.billTo.setOnClickListener {
            startActivity(Intent(this@EstimateAddActivity, AddClientActivity::class.java))
        }


        binding.tax.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Tax")

            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.alert_dilog_box, null)
            builder.setView(customLayout)

            builder.setPositiveButton("SAVE") { _: DialogInterface?, _: Int ->
                val editText = customLayout.findViewById<EditText>(R.id.discount)
                val TAX = editText.text.toString().toInt()
                binding.taxadd.text = "$TAX"
                if (editText.text.isNotEmpty() && editText.text.isNotBlank()) {

                    tax = subtotal * TAX / 100
                    total = subtotal - tax
                    binding.mainTotal.text = "₹ $total"
                } else {
                    editText.error = "This field is required"
                }
            }

            builder.setNegativeButton("CANCEL") { dialog: DialogInterface?, _: Int ->
                dialog!!.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.Terms.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Terms & Conditions")

            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.terms_layout, null)
            builder.setView(customLayout)
            builder.setTitle("Terms & Conditions Details")
            val editText = customLayout.findViewById<EditText>(R.id.discount)
            editText.hint = "Terms & condition"
            builder.setPositiveButton("SAVE") { _: DialogInterface?, _: Int ->
                if (editText.text.isNotEmpty() && editText.text.isNotBlank()) {
                    val sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
                    val myEdit = sharedPreferences.edit()
                    myEdit.putString("term", editText.text.toString())
                    myEdit.apply()
                    binding.trems.text = "${editText.text}"
                } else {
                    editText.error = "This field is required"
                }
            }

            builder.setNegativeButton("CANCEL") { dialog: DialogInterface?, _: Int ->
                dialog!!.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.payment.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Payment Method")

            // set the custom layout
            val customLayout: View = layoutInflater.inflate(R.layout.terms_layout, null)
            builder.setView(customLayout)
            builder.setTitle("Payment Details")
            val editText = customLayout.findViewById<EditText>(R.id.discount)
            editText.hint = "Payment details"
            builder.setPositiveButton("SAVE") { _: DialogInterface?, _: Int ->
                if (editText.text.isNotEmpty() && editText.text.isNotBlank()) {
                    val sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
                    val myEdit = sharedPreferences.edit()
                    myEdit.putString("payment", editText.text.toString())
                    myEdit.apply()
                    binding.paymentDetiles.text = "${editText.text}"
                } else {
                    editText.error = "This field is required"
                }
            }
            builder.setNegativeButton("CANCEL") { dialog: DialogInterface?, _: Int ->
                dialog!!.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }

        total = subtotal - discount - tax
        binding.mainTotal.text = "₹ $total"

        binding.addItem.setOnClickListener {
            startActivity(Intent(this@EstimateAddActivity, AddActivityActivity::class.java))
        }

        binding.signature.setOnClickListener {
            //startActivity(Intent(this@EstimateAddActivity, AddSignatureActivity::class.java))
            chooseImageGallery()
        }

        binding.save.setOnClickListener {

            AppManage.getInstance(this@EstimateAddActivity).showInterstitialAd(
                this@EstimateAddActivity, {
                    binding.progressCircular.visibility = View.VISIBLE

                    if (items.isNullOrEmpty()) {
                        Toast.makeText(this@EstimateAddActivity, " loading", Toast.LENGTH_SHORT)
                            .show()
                        binding.progressCircular.visibility = View.GONE
                    } else {
                        binding.progressCircular.visibility = View.GONE
                        //val invoiceNo = binding.invoiceNo.text.toString()
                        val invoiceDate = binding.createdDate.text.toString()
                        val invoiceAccount = binding.dueDate.text.toString()
                        val receiverName = binding.name.text.toString()
                        val receiverAddress = "qw"
                        val paypal = binding.paymentDetiles.text.toString()
                        val senderName = binding.name.text.toString()
                        val senderEmail = clientEmail
                        val senderPhone = clientPhone
                        val senderAddress = clientAddress
                        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                            contentResolver, Uri.parse(imageUri.toString())
                        )
                        invoiceViewModel.insert(
                            Invoice(
                                id = null,
                                userId = 1,
                                clientId = clientId.toInt(),
                                invoiceNo = invoiceNo,
                                date = invoiceDate,
                                accountNo = invoiceAccount,
                                receiverName = receiverName,
                                receiverAddress = receiverAddress,
                                payPal = paypal,
                                senderName = senderName,
                                senderEmail = senderEmail,
                                senderPhone = senderPhone,
                                senderAddress = senderAddress,
                                items = items,
                                total = total.toString(),
                                sign = bitmap,
                                tax = binding.paymentDetiles.text.toString(),
                                terms = taxString
                            )
                        )
                    }

                    savePdf()

                    onBackPressedDispatcher.onBackPressed()
                }, "", AppManage.app_mainClickCntSwAd
            )

        }
        binding.preview.setOnClickListener {

            AppManage.getInstance(this@EstimateAddActivity).showInterstitialAd(
                this@EstimateAddActivity, {
                    createPdf(items)
                    val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
                    try {
                        val printDocumentAdapter: PrintDocumentAdapter =
                            PdfDocumentAdapter(outputStream)
                        printManager.print(
                            "Document", printDocumentAdapter, PrintAttributes.Builder().build()
                        )
                    } catch (ex: Exception) {
                        Toast.makeText(
                            this@EstimateAddActivity, "Can't read pdf file", Toast.LENGTH_SHORT
                        ).show()
                    }
                }, "", AppManage.app_mainClickCntSwAd
            )
        }

    }

    private fun createPdf(items: List<Items>) {
        outputStream.reset()
        val writer = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        val invoiceGreen = DeviceRgb(51, 204, 51)
        val invoiceColor = DeviceRgb(220, 220, 220)

        val drawable1 = getDrawable(R.drawable.thankyou)
        val bitmap1 = drawable1!!.toBitmap()
        val stream1 = ByteArrayOutputStream()
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1)
        val bitmapData1 = stream1.toByteArray()

        val image5 = ImageDataFactory.create(bitmapData1)
        val imgView5 = Image(image5)
        imgView5.setHeight(100f)
        imgView5.setWidth(100f)


        val drawable5 = getDrawable(R.drawable.amazon)
        val bitmap5 = drawable5!!.toBitmap()
        val stream5 = ByteArrayOutputStream()
        //bitmap5.compress(Bitmap.CompressFormat.PNG, 100, stream5)
        image!!.compress(Bitmap.CompressFormat.PNG, 100, stream5)
        val bitmapData5 = stream5.toByteArray()

        val image6 = ImageDataFactory.create(bitmapData5)
        val imgView = Image(image6)
        imgView.setHeight(100f)
        imgView.setWidth(100f)

        val mainTable = Table(1)
        mainTable.width = UnitValue.createPercentValue(100f)

        val column: FloatArray = floatArrayOf(5f, 5f)
        val table1 = Table(column)
        table1.setFixedLayout()
        table1.width = UnitValue.createPercentValue(100f)
        table1.addCell(
            Cell().add(
                imgView
            ).setBorder(Border.NO_BORDER)
        )

        val column2: FloatArray = floatArrayOf(1f, 1f)
        val table2 = Table(UnitValue.createPercentArray(column2))
        table2.width = UnitValue.createPercentValue(100f)


        table2.addCell(
            Cell(
                1, 2
            ).add(
                Paragraph("Invoice")
            ).setFontColor(invoiceGreen).setFontSize(26f).setBorder(Border.NO_BORDER)
        )


        table2.addCell(
            Cell().add(
                Paragraph("Invoice No :")
            ).setBorder(Border.NO_BORDER)
        )
        table2.addCell(
            Cell().add(
                Paragraph(invoiceNo)
            ).setBorder(Border.NO_BORDER)
        )


        table2.addCell(
            Cell().add(
                Paragraph("Invoice Date :")
            ).setBorder(Border.NO_BORDER)
        )

        table2.addCell(
            Cell().add(
                Paragraph(date)
            ).setBorder(Border.NO_BORDER)
        )


        table2.addCell(
            Cell().add(
                Paragraph("Account No :")
            ).setBorder(Border.NO_BORDER)
        )

        table2.addCell(
            Cell().add(
                Paragraph(accountNo)
            ).setBorder(Border.NO_BORDER)
        )

        table1.addCell(
            Cell().add(
                table2
            ).setBorder(Border.NO_BORDER)
        )

        val column3: FloatArray = floatArrayOf(5f, 5f)
        val table3 = Table(column3)
        table3.width = UnitValue.createPercentValue(100f)
        table3.setFixedLayout()

        val column5: FloatArray = floatArrayOf(1f)
        val table5 = Table(column5)
        table5.width = UnitValue.createPercentValue(100f)


        table5.addCell(
            Cell().add(
                Paragraph("\n")
            ).setBorder(Border.NO_BORDER)
        )

        table5.addCell(
            Cell().add(
                Paragraph("To")
            ).setBorder(Border.NO_BORDER)
        )
        table5.addCell(
            Cell().add(
                Paragraph(clientName)
            ).setBorder(Border.NO_BORDER)
        )
        table5.addCell(
            Cell().add(
                Paragraph(clientAddress)
            ).setBorder(Border.NO_BORDER)
        )
        table5.addCell(
            Cell().add(
                Paragraph("Gujarat")
            ).setBorder(Border.NO_BORDER)
        )
        table3.addCell(
            Cell().add(
                table5
            ).setBorder(Border.NO_BORDER)
        )

        val column4: FloatArray = floatArrayOf(1f)
        val table4 = Table(UnitValue.createPercentArray(column4))
        table4.width = UnitValue.createPercentValue(100f)

        table4.addCell(
            Cell().add(
                Paragraph("\n")
            ).setBorder(Border.NO_BORDER)
        )

        table4.addCell(
            Cell().add(
                Paragraph("Payment Method :")
            ).setBorder(Border.NO_BORDER).setBold()
        )
        table4.addCell(
            Cell().add(
                Paragraph("Paypal : ${binding.paymentDetiles.text}")
            ).setBorder(Border.NO_BORDER)
        )
        table4.addCell(
            Cell().add(
                Paragraph("Card Payment : We accept Visa ,surat ")
            ).setBorder(Border.NO_BORDER)
        )

        table3.addCell(
            Cell().add(
                table4
            ).setBorder(Border.NO_BORDER)
        )


        val column6: FloatArray = floatArrayOf(1f, 1f, 1f, 1f, 1f)
        val table6 = Table(UnitValue.createPercentArray(column6))
        table6.width = UnitValue.createPercentValue(100f)

        table6.addCell(
            Cell().add(
                Paragraph("\nS.No.").setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.WHITE)
            ).setBackgroundColor(
                invoiceGreen
            )
        )
        table6.addCell(
            Cell().add(
                Paragraph("\nItems Description").setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.WHITE)
            ).setBackgroundColor(
                invoiceGreen
            )
        )
        table6.addCell(
            Cell().add(
                Paragraph("\nRate").setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.WHITE)
            ).setBackgroundColor(
                invoiceGreen
            )
        )
        table6.addCell(
            Cell().add(
                Paragraph("\nQty").setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.WHITE)
            ).setBackgroundColor(
                invoiceGreen
            )
        )
        table6.addCell(
            Cell().add(
                Paragraph("\nPrice").setTextAlignment(TextAlignment.CENTER)
            ).setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceGreen)
        )

        //data
        for (item in items) {
            item.let {
                table6.addCell(
                    Cell().add(Paragraph("${it.id.toString().toInt()}"))
                        .setBackgroundColor(invoiceColor)
                )
                table6.addCell(
                    Cell().add(Paragraph(it.item)).setBackgroundColor(invoiceColor)
                )
                table6.addCell(
                    Cell().add(Paragraph("${it.rate}")).setBackgroundColor(invoiceColor)
                )
                table6.addCell(
                    Cell().add(Paragraph("${it.qty}")).setBackgroundColor(invoiceColor)
                )
                table6.addCell(
                    Cell().add(Paragraph("${it.rate * it.qty}")).setBackgroundColor(invoiceColor)
                )
            }
        }


        table6.addCell(
            Cell().add(
                Paragraph("")
            ).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceRgb(255, 255, 255))
        )
        table6.addCell(
            Cell().add(
                Paragraph("")
            ).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceRgb(255, 255, 255))
        )
        table6.addCell(
            Cell().add(
                Paragraph("")
            ).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceRgb(255, 255, 255))
        )
        table6.addCell(
            Cell().add(
                Paragraph("Sub-Total")
            ).setBackgroundColor(
                invoiceGreen
            )
        )


        table6.addCell(
            Cell().add(
                Paragraph("$subtotal")
            ).setBackgroundColor(
                invoiceGreen
            )
        )

        table6.addCell(
            Cell(1, 3).add(
                Paragraph("Terms & condition")
            ).setBorder(Border.NO_BORDER).setBold().setBackgroundColor(DeviceRgb(255, 255, 255))
        )
        table6.addCell(
            Cell().add(
                Paragraph("GST (${binding.taxadd.text}%)")
            ).setBackgroundColor(
                invoiceGreen
            )
        )
        val tax1 = binding.taxadd.text.toString().toInt()
        tax = subtotal * tax1 / 100

        table6.addCell(
            Cell().add(
                Paragraph("$tax")
            ).setBackgroundColor(
                invoiceGreen
            )
        )
        table6.addCell(
            Cell(1, 3).add(
                Paragraph(binding.trems.text.toString())
            ).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceRgb(255, 255, 255))

        )
        table6.addCell(
            Cell().add(
                Paragraph("Grand Total").setBold()
            ).setBackgroundColor(
                invoiceGreen
            )
        )

        total = subtotal - tax

        table6.addCell(
            Cell().add(
                Paragraph("$total").setBold()
            ).setBackgroundColor(
                invoiceGreen
            )
        )

        val drawable2 = getDrawable(R.drawable.gmail)
        val bitmap2 = drawable2!!.toBitmap()
        val stream2 = ByteArrayOutputStream()
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2)
        val bitmapData2 = stream2.toByteArray()

        val image2 = ImageDataFactory.create(bitmapData2)
        val imgView2 = Image(image2)
        imgView2.setHeight(20f)
        imgView2.setWidth(20f)

        val drawable3 = getDrawable(R.drawable.telephone)
        val bitmap3 = drawable3!!.toBitmap()
        val stream3 = ByteArrayOutputStream()
        bitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream3)
        val bitmapData3 = stream3.toByteArray()

        val image3 = ImageDataFactory.create(bitmapData3)
        val imgView3 = Image(image3)
        imgView3.setHeight(20f)
        imgView3.setWidth(20f)

        val drawable4 = getDrawable(R.drawable.location)
        val bitmap4 = drawable4!!.toBitmap()
        val stream4 = ByteArrayOutputStream()
        bitmap4.compress(Bitmap.CompressFormat.PNG, 100, stream4)
        val bitmapData4 = stream4.toByteArray()

        val image4 = ImageDataFactory.create(bitmapData4)
        val imgView4 = Image(image4)
        imgView4.setHeight(20f)
        imgView4.setWidth(20f)

        val column9: FloatArray = floatArrayOf(5f, 5f)
        val table9 = Table(UnitValue.createPercentArray(column9))
        table9.width = UnitValue.createPercentValue(100f)
        table9.setFixedLayout()

        val column8: FloatArray = floatArrayOf(1f, 4f)
        val table8 = Table(UnitValue.createPercentArray(column8))
        table8.width = UnitValue.createPercentValue(100f)
        table8.setFixedLayout()

        table8.addCell(
            Cell().add(
                imgView2.setHorizontalAlignment(HorizontalAlignment.CENTER)
            ).setHorizontalAlignment(HorizontalAlignment.CENTER).setBorder(Border.NO_BORDER)
        )
        table8.addCell(
            Cell().add(
                Paragraph(email)
            ).setBorder(Border.NO_BORDER)
        )

        table8.addCell(
            Cell().add(
                imgView3.setHorizontalAlignment(HorizontalAlignment.CENTER)
            ).setHorizontalAlignment(HorizontalAlignment.CENTER).setBorder(Border.NO_BORDER)
        )
        table8.addCell(
            Cell().add(
                Paragraph(phone)
            ).setBorder(Border.NO_BORDER)
        )

        table8.addCell(
            Cell().add(
                imgView4.setHorizontalAlignment(HorizontalAlignment.CENTER)
            ).setBorder(Border.NO_BORDER)
        )
        table8.addCell(
            Cell().add(
                Paragraph(address1)
            ).setBorder(Border.NO_BORDER)
        )

        table8.addCell(
            Cell().add(
                Paragraph("")
            ).setBorder(Border.NO_BORDER)
        )
        table8.addCell(
            Cell().add(
                Paragraph(address2)
            ).setBorder(Border.NO_BORDER)
        )

        table9.addCell(
            Cell().add(
                table8
            ).setBorder(Border.NO_BORDER)
        )
        table9.addCell(
            Cell().add(
                imgView5.setHorizontalAlignment(HorizontalAlignment.RIGHT)
            ).setBorder(Border.NO_BORDER)
        )

        mainTable.addCell(
            Cell().add(
                table1
            ).setBorder(Border.NO_BORDER)
        )
        mainTable.addCell(
            Cell().add(
                table3
            ).setBorder(Border.NO_BORDER)
        )
        mainTable.addCell(
            Cell().add(
                Paragraph("\n")
            ).setBorder(Border.NO_BORDER)
        )
        mainTable.addCell(
            Cell().add(
                table6
            ).setBorder(Border.NO_BORDER)
        )
        mainTable.addCell(
            Cell().add(
                Paragraph("\n\n\n").setTextAlignment(
                    TextAlignment.RIGHT
                )
            ).setBorder(Border.NO_BORDER)
        )

        val image1 = imageUri
        val bitmap: Bitmap =
            MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(image1.toString()))
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()
        val image = ImageDataFactory.create(bitmapData)
        val imgView0 = Image(image)
        imgView0.setHeight(50f)
        imgView0.setWidth(60f)

        mainTable.addCell(
            Cell().add(
                imgView0.setHorizontalAlignment(HorizontalAlignment.RIGHT)
            ).setBorder(Border.NO_BORDER)
        )
        mainTable.addCell(
            Cell().add(
                Paragraph("\n(Authorised signatory)\n\n\n").setTextAlignment(
                    TextAlignment.RIGHT
                )
            ).setBorder(Border.NO_BORDER)
        )
        mainTable.addCell(
            Cell().add(
                table9
            ).setBorder(Border.NO_BORDER)
        )
        document.add(mainTable)
        document.close()

    }

    private fun savePdf() {
        createPdf(items)
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val pdfPath = applicationInfo.dataDir
        val file = File(pdfPath, "${System.currentTimeMillis()}.pdf")
        val outputStream1 = FileOutputStream(file)
        outputStream.writeTo(outputStream1)
        val pdfPath1 =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()
        val file1 = File(pdfPath1, "${System.currentTimeMillis()}.pdf")
        val outputStream2 = FileOutputStream(file1)
        Toast.makeText(this@EstimateAddActivity, " Saved !!", Toast.LENGTH_SHORT).show()
        outputStream.writeTo(outputStream2)
        val intent = Intent(Intent.ACTION_VIEW)
        val uri: Uri =
            FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file1)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            invoiceNo = "${data!!.getStringExtra("invoiceNo")}"
            binding.invoiceNo.text = invoiceNo
            accountNo = "${data.getStringExtra("accountNo")}"
            binding.dueDate.text = accountNo
            date = "${data.getStringExtra("date")}"
            binding.createdDate.text = date
            if (data.data != null) {
                filePhoto = getPhotoFile("FILE_NAME.png")
                val takenPhoto = BitmapFactory.decodeFile(filePhoto.absolutePath)
                //binding.signatureView.setImageBitmap(takenPhoto)
                binding.signatureView.setImageURI(data.data)
                imageUri = data.data!!
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
            }
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }
}