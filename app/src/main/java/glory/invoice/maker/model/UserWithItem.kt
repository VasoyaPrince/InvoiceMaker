package glory.invoice.maker.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class UserWithItem(
    @Embedded val user:Users,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val items: List<Items>
)
