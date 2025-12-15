import java.io.File

class FileHandler {
    private val libraryFile = "library.txt"

    // Загрузка книг
    fun zagruzitKnigi(): MutableList<Book> {
        val knigi = mutableListOf<Book>()
        val fayl = File(libraryFile)

        if (!fayl.exists()) return knigi

        fayl.forEachLine { stroka ->
            val kniga = parseKnigu(stroka)
            if (kniga != null) knigi.add(kniga)
        }

        return knigi
    }

    private fun parseKnigu(stroka: String): Book? {
        if (stroka.isBlank()) return null

        val chasti = stroka.split("|")
        if (chasti.size < 8) return null

        return try {
            Book(
                id = chasti[0].toInt(),
                title = chasti[1],
                author = chasti[2],
                year = chasti[3].toInt(),
                genre = chasti[4],
                isRead = chasti[5].toBoolean(),
                rating = chasti[6].toIntOrNull(),
                location = chasti[7]
            )
        } catch (e: Exception) {
            null
        }
    }

    // Сохранение книг
    fun sohranitKnigi(knigi: List<Book>) {
        val soderzhanie = knigi.joinToString("\n") { kniga ->
            "${kniga.id}|${kniga.title}|${kniga.author}|${kniga.year}|" +
                    "${kniga.genre}|${kniga.isRead}|${kniga.rating ?: ""}|${kniga.location}"
        }
        File(libraryFile).writeText(soderzhanie)
    }
}
