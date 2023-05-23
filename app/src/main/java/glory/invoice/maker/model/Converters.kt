package glory.invoice.maker.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.io.ByteArrayOutputStream

class Converters {
    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    @TypeConverter
    fun fromBitmap(bmp: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 5, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun listToJson(value: List<Items>): String {
        val value1 = Gson().toJson(value)
        Log.d("TAG", "listToJson: $value1")
        return value1
    }

    @TypeConverter
    fun jsonToList(value: String): List<Items> =
        Gson().fromJson(value, Array<Items>::class.java).toList()
}