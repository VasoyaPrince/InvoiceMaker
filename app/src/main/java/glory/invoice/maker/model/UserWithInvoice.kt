package glory.invoice.maker.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithInvoice(
    @Embedded val user:Users,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val invoice: List<Invoice>
)
