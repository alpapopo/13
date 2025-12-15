data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val year: Int,
    val genre: String,
    val isRead: Boolean = false,
    val rating: Int? = null,
    val location: String = ""
)
