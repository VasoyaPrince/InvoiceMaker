package glory.invoice.maker.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = "invoice", foreignKeys = [ForeignKey(
        entity = Users::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId")
    ), ForeignKey(
        entity = Client::class, parentColumns = arrayOf("id"), childColumns = arrayOf("clientId")
    )]
)
@Parcelize
data class Invoice(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "userId", index = true) val userId: Int,
    @ColumnInfo(name = "clientId") val clientId: Int,
    @ColumnInfo(name = "invoiceNo") val invoiceNo: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "accountNo") val accountNo: String,
    @ColumnInfo(name = "receiverName") val receiverName: String,
    @ColumnInfo(name = "receiverAddress") val receiverAddress: String,
    @ColumnInfo(name = "payPal") val payPal: String,
    @ColumnInfo(name = "items") val items: List<Items>,
    @ColumnInfo(name = "senderName") val senderName: String,
    @ColumnInfo(name = "senderEmail") val senderEmail: String,
    @ColumnInfo(name = "senderPhone") val senderPhone: String,
    @ColumnInfo(name = "senderAddress") val senderAddress: String,
    @ColumnInfo(name = "total") val total: String,
    @ColumnInfo(name = "terms") val terms: String,
    @ColumnInfo(name = "tax") val tax: String,
    @ColumnInfo(name = "image") val sign: Bitmap?
) : Parcelable
