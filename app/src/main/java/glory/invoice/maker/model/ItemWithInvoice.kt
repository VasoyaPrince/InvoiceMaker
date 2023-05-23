package glory.invoice.maker.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class ItemWithInvoice(
    @Embedded val user:Users,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val client: List<Client>
)
