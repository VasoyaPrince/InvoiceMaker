package glory.invoice.maker

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
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
import glory.invoice.maker.databinding.ActivityFromBinding
import glory.invoice.maker.model.Item
import glory.invoice.maker.model.Items
import java.io.File
import java.io.FileOutputStream


class FromActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFromBinding
    private var itemList = ArrayList<Items>()
    private val outputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    private var item = 1
    private var subtotal = 0
    private var total = 0
    private var gst = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFromBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addItems.setOnClickListener {
            addLine()
        }

        binding.submit.setOnClickListener {
            saveData()
        }

    }

    private fun addLine() {
        item++
        val inflater = LayoutInflater.from(this).inflate(R.layout.item_layout, null)
        binding.linear.addView(inflater, binding.linear.childCount)
    }

    private fun saveData() {
        itemList.clear()
        val count = binding.linear.childCount
        var v: View?

        for (i in 0 until count) {
            v = binding.linear.getChildAt(i)

            val description: EditText = v.findViewById(R.id.description)
            val rate: EditText = v.findViewById(R.id.rate)
            val qty: EditText = v.findViewById(R.id.qty)

//            val language = Item(
//                item,
//                desc = description.text.toString(),
//                rate.text.toString().toInt(),
//                qty.text.toString().toInt(),
//                rate.text.toString().toInt() + qty.text.toString().toInt()
//            )
//            itemList.add(language)
//            invoiceTemplate1(
////                Invoice(
////                    binding.invoiceNo.text.toString(),
////                    binding.invoiceDate.text.toString(),
////                    binding.accountNo.text.toString(),
////                    binding.ReceiverName.text.toString(),
////                    binding.ReceiverAddress.text.toString(),
////                    binding.paypal.text.toString(),
////                    itemList,
////                    binding.email.text.toString(),
////                    binding.phoneNo.text.toString(),
////                    binding.senderAddress.text.toString(),
////                )
//            )

        }
    }

//    private fun invoiceTemplate1(invoice: Invoice) {
//        val pdfPath1 =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                .toString()
////        val file1 = File(pdfPath1, "${System.currentTimeMillis()}.pdf")
//        val file1 = File(pdfPath1, "bill.pdf")
//        val outputStream2 = FileOutputStream(file1)
//        outputStream.writeTo(outputStream2)
//
//        val writer = PdfWriter(file1)
//        val pdfDocument = PdfDocument(writer)
//        val document = Document(pdfDocument)
//
//        val invoiceGreen = DeviceRgb(51, 204, 51)
//        val invoiceColor = DeviceRgb(220, 220, 220)
//
//        val drawable1 = getDrawable(R.drawable.thankyou)
//        val bitmap1 = drawable1!!.toBitmap()
//        val stream1 = ByteArrayOutputStream()
//        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1)
//        val bitmapData1 = stream1.toByteArray()
//
//        val image5 = ImageDataFactory.create(bitmapData1)
//        val imgView5 = Image(image5)
//        imgView5.setHeight(100f)
//        imgView5.setWidth(100f)
//
//        val drawable5 = getDrawable(R.drawable.amazon)
//        val bitmap5 = drawable5!!.toBitmap()
//        val stream5 = ByteArrayOutputStream()
//        bitmap5.compress(Bitmap.CompressFormat.PNG, 100, stream5)
//        val bitmapData5 = stream5.toByteArray()
//
//        val image6 = ImageDataFactory.create(bitmapData5)
//        val imgView = Image(image6)
//        imgView.setHeight(100f)
//        imgView.setWidth(100f)
//
//        val mainTable = Table(1)
//        mainTable.width = UnitValue.createPercentValue(100f)
//
//        val column: FloatArray = floatArrayOf(5f, 5f)
//        val table1 = Table(column)
//        table1.setFixedLayout()
//        table1.width = UnitValue.createPercentValue(100f)
//        table1.addCell(
//            Cell().add(
//                imgView
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        val column2: FloatArray = floatArrayOf(1f, 1f)
//        val table2 = Table(UnitValue.createPercentArray(column2))
//        table2.width = UnitValue.createPercentValue(100f)
//
//
//        table2.addCell(
//            Cell(
//                1, 2
//            ).add(
//                Paragraph("Invoice")
//            ).setFontColor(invoiceGreen).setFontSize(26f).setBorder(Border.NO_BORDER)
//        )
//
//
//        table2.addCell(
//            Cell().add(
//                Paragraph("Invoice No :")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table2.addCell(
//            Cell().add(
//                Paragraph("${invoice.invoiceNo}")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//
//        table2.addCell(
//            Cell().add(
//                Paragraph("Invoice Date :")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table2.addCell(
//            Cell().add(
//                Paragraph(invoice.date)
//            ).setBorder(Border.NO_BORDER)
//        )
//
//
//        table2.addCell(
//            Cell().add(
//                Paragraph("Account No :")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table2.addCell(
//            Cell().add(
//                Paragraph("${invoice.accountNo}")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table1.addCell(
//            Cell().add(
//                table2
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        val column3: FloatArray = floatArrayOf(5f, 5f)
//        val table3 = Table(column3)
//        table3.width = UnitValue.createPercentValue(100f)
//        table3.setFixedLayout()
//
//        val column5: FloatArray = floatArrayOf(1f)
//        val table5 = Table(column5)
//        table5.width = UnitValue.createPercentValue(100f)
//
//
//        table5.addCell(
//            Cell().add(
//                Paragraph("\n")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table5.addCell(
//            Cell().add(
//                Paragraph("To")
//            ).setBorder(Border.NO_BORDER)
//        )
//        table5.addCell(
//            Cell().add(
//                Paragraph(invoice.receiverName)
//            ).setBorder(Border.NO_BORDER)
//        )
//        table5.addCell(
//            Cell().add(
//                Paragraph(invoice.receiverAddress)
//            ).setBorder(Border.NO_BORDER)
//        )
//        table5.addCell(
//            Cell().add(
//                Paragraph("Gujarat")
//            ).setBorder(Border.NO_BORDER)
//        )
//        table3.addCell(
//            Cell().add(
//                table5
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        val column4: FloatArray = floatArrayOf(1f)
//        val table4 = Table(UnitValue.createPercentArray(column4))
//        table4.width = UnitValue.createPercentValue(100f)
//
//        table4.addCell(
//            Cell().add(
//                Paragraph("\n")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table4.addCell(
//            Cell().add(
//                Paragraph("Payment Method :")
//            ).setBorder(Border.NO_BORDER).setBold()
//        )
//        table4.addCell(
//            Cell().add(
//                Paragraph("Paypal : ${invoice.payPal}")
//            ).setBorder(Border.NO_BORDER)
//        )
//        table4.addCell(
//            Cell().add(
//                Paragraph("Card Payment : We accept Visa ,surat ")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table3.addCell(
//            Cell().add(
//                table4
//            ).setBorder(Border.NO_BORDER)
//        )
//
//
//        val column6: FloatArray = floatArrayOf(1f, 1f, 1f, 1f, 1f)
//        val table6 = Table(UnitValue.createPercentArray(column6))
//        table6.width = UnitValue.createPercentValue(100f)
//
//        table6.addCell(
//            Cell().add(
//                Paragraph("\nS.No.").setTextAlignment(TextAlignment.CENTER)
//                    .setFontColor(ColorConstants.WHITE)
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("\nItems Description").setTextAlignment(TextAlignment.CENTER)
//                    .setFontColor(ColorConstants.WHITE)
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("\nRate").setTextAlignment(TextAlignment.CENTER)
//                    .setFontColor(ColorConstants.WHITE)
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("\nQty").setTextAlignment(TextAlignment.CENTER)
//                    .setFontColor(ColorConstants.WHITE)
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("\nPrice").setTextAlignment(TextAlignment.CENTER)
//            ).setFontColor(ColorConstants.WHITE).setBackgroundColor(invoiceGreen)
//        )
//
//        //data
//
////        for (i in 0 until invoice.items.size) {
////            table6.addCell(
////                Cell().add(Paragraph("${i + 1}")).setBackgroundColor(invoiceColor)
////            )
////            table6.addCell(
////                Cell().add(Paragraph(invoice.items[i].item)).setBackgroundColor(invoiceColor)
////            )
////            table6.addCell(
////                Cell().add(Paragraph("${invoice.items[i].rate}")).setBackgroundColor(invoiceColor)
////            )
////            table6.addCell(
////                Cell().add(Paragraph("${invoice.items[i].qty}")).setBackgroundColor(invoiceColor)
////            )
////            table6.addCell(
////                Cell().add(Paragraph("${invoice.items[i].rate * invoice.items[i].qty}"))
////                    .setBackgroundColor(invoiceColor)
////            )
////        }
//
//
//        table6.addCell(
//            Cell().add(
//                Paragraph("")
//            ).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceRgb(255, 255, 255))
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("")
//            ).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceRgb(255, 255, 255))
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("")
//            ).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceRgb(255, 255, 255))
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("Sub-Total")
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//
////        for (i in 0 until invoice.items.size) {
////            subtotal += invoice.items[i].rate * invoice.items[i].qty
////        }
//
//        table6.addCell(
//            Cell().add(
//                Paragraph("$subtotal")
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//
//        table6.addCell(
//            Cell(1, 3).add(
//                Paragraph("Terms & condition")
//            ).setBorder(Border.NO_BORDER).setBold().setBackgroundColor(DeviceRgb(255, 255, 255))
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("GST (12%)")
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//
//        gst = subtotal * 12 / 100
//
//        table6.addCell(
//            Cell().add(
//                Paragraph("$gst")
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//        table6.addCell(
//            Cell(1, 3).add(
//                Paragraph("Goods sold are not returnable and exchangeable")
//            ).setBorder(Border.NO_BORDER).setBackgroundColor(DeviceRgb(255, 255, 255))
//
//        )
//        table6.addCell(
//            Cell().add(
//                Paragraph("Grand Total").setBold()
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//
//        total = subtotal - gst
//
//        table6.addCell(
//            Cell().add(
//                Paragraph("$total").setBold()
//            ).setBackgroundColor(
//                invoiceGreen
//            )
//        )
//
//        val drawable2 = getDrawable(R.drawable.gmail)
//        val bitmap2 = drawable2!!.toBitmap()
//        val stream2 = ByteArrayOutputStream()
//        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2)
//        val bitmapData2 = stream2.toByteArray()
//
//        val image2 = ImageDataFactory.create(bitmapData2)
//        val imgView2 = Image(image2)
//        imgView2.setHeight(20f)
//        imgView2.setWidth(20f)
//
//        val drawable3 = getDrawable(R.drawable.telephone)
//        val bitmap3 = drawable3!!.toBitmap()
//        val stream3 = ByteArrayOutputStream()
//        bitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream3)
//        val bitmapData3 = stream3.toByteArray()
//
//        val image3 = ImageDataFactory.create(bitmapData3)
//        val imgView3 = Image(image3)
//        imgView3.setHeight(20f)
//        imgView3.setWidth(20f)
//
//        val drawable4 = getDrawable(R.drawable.location)
//        val bitmap4 = drawable4!!.toBitmap()
//        val stream4 = ByteArrayOutputStream()
//        bitmap4.compress(Bitmap.CompressFormat.PNG, 100, stream4)
//        val bitmapData4 = stream4.toByteArray()
//
//        val image4 = ImageDataFactory.create(bitmapData4)
//        val imgView4 = Image(image4)
//        imgView4.setHeight(20f)
//        imgView4.setWidth(20f)
//
//        val column9: FloatArray = floatArrayOf(5f, 5f)
//        val table9 = Table(UnitValue.createPercentArray(column9))
//        table9.width = UnitValue.createPercentValue(100f)
//        table9.setFixedLayout()
//
//        val column8: FloatArray = floatArrayOf(1f, 4f)
//        val table8 = Table(UnitValue.createPercentArray(column8))
//        table8.width = UnitValue.createPercentValue(100f)
//        table8.setFixedLayout()
//
//        table8.addCell(
//            Cell().add(
//                imgView2.setHorizontalAlignment(HorizontalAlignment.CENTER)
//            ).setHorizontalAlignment(HorizontalAlignment.CENTER).setBorder(Border.NO_BORDER)
//        )
//        table8.addCell(
//            Cell().add(
//                Paragraph("demo@gmail.com")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table8.addCell(
//            Cell().add(
//                imgView3.setHorizontalAlignment(HorizontalAlignment.CENTER)
//            ).setHorizontalAlignment(HorizontalAlignment.CENTER).setBorder(Border.NO_BORDER)
//        )
//        table8.addCell(
//            Cell().add(
//                Paragraph("+911234567890")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table8.addCell(
//            Cell().add(
//                imgView4.setHorizontalAlignment(HorizontalAlignment.CENTER)
//            ).setBorder(Border.NO_BORDER)
//        )
//        table8.addCell(
//            Cell().add(
//                Paragraph("121,demo,vesu")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table8.addCell(
//            Cell().add(
//                Paragraph("")
//            ).setBorder(Border.NO_BORDER)
//        )
//        table8.addCell(
//            Cell().add(
//                Paragraph("Gujarat")
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        table9.addCell(
//            Cell().add(
//                table8
//            ).setBorder(Border.NO_BORDER)
//        )
//        table9.addCell(
//            Cell().add(
//                imgView5.setHorizontalAlignment(HorizontalAlignment.RIGHT)
//            ).setBorder(Border.NO_BORDER)
//        )
//
//        mainTable.addCell(
//            Cell().add(
//                table1
//            ).setBorder(Border.NO_BORDER)
//        )
//        mainTable.addCell(
//            Cell().add(
//                table3
//            ).setBorder(Border.NO_BORDER)
//        )
//        mainTable.addCell(
//            Cell().add(
//                Paragraph("\n")
//            ).setBorder(Border.NO_BORDER)
//        )
//        mainTable.addCell(
//            Cell().add(
//                table6
//            ).setBorder(Border.NO_BORDER)
//        )
//        mainTable.addCell(
//            Cell().add(
//                Paragraph("\n\n\n\n\n\n(Authorised signatory)\n\n\n").setTextAlignment(
//                    TextAlignment.RIGHT
//                )
//            ).setBorder(Border.NO_BORDER)
//        )
//        mainTable.addCell(
//            Cell().add(
//                table9
//            ).setBorder(Border.NO_BORDER)
//        )
//        document.add(mainTable)
//        document.close()
//        Toast.makeText(this@FromActivity, "done!!", Toast.LENGTH_SHORT).show()
//        //view pdf
////        val intent = Intent(Intent.ACTION_VIEW)
////        val uri: Uri =
////            FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", file1)
////        intent.setDataAndType(uri, "application/pdf")
////        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
////        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
////        startActivity(intent)
//    }
}