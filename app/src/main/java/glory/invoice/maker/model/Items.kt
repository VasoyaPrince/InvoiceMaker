package glory.invoice.maker.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "items", foreignKeys = [ForeignKey(
        entity = Users::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class Items(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "userId", index = true) val userId: Int,
    @ColumnInfo(name = "itemName") val item: String,
    @ColumnInfo(name = "rate") val rate: Int,
    @ColumnInfo(name = "qty") val qty: Int,
    @ColumnInfo(name = "price") val price: Int
) : Parcelable