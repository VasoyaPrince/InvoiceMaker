package glory.invoice.maker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import glory.invoice.maker.database.dao.ClientDao
import glory.invoice.maker.database.dao.InvoiceDao
import glory.invoice.maker.database.dao.ItemDao
import glory.invoice.maker.database.dao.UsersDao
import glory.invoice.maker.model.Client
import glory.invoice.maker.model.Converters
import glory.invoice.maker.model.Invoice
import glory.invoice.maker.model.Items
import glory.invoice.maker.model.Users


@Database(
    entities = [Users::class, Items::class, Client::class, Invoice::class], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
    abstract fun itemsDao(): ItemDao
    abstract fun clientDao(): ClientDao
    abstract fun invoiceDao(): InvoiceDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, UserDatabase::class.java, "user_database.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

