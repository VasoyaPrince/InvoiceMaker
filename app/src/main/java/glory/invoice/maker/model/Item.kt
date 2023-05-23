package glory.invoice.maker.model

data class Item (
    val itemName :String,
    val price : Int,
    val qty :Int,
    val desc :String
        )