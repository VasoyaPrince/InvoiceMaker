package glory.invoice.maker.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class Users(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "image") val image: Bitmap?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "website") val website: String,
)