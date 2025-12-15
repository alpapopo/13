fun main() {
    val menedzher = LibraryManager()

    while (true) {
        vyvestiMenu()

        when (readLine()) {
            "1" -> menedzher.pokazatVseKnigi()
            "2" -> menedzher.dobavitKnigu()
            "3" -> menedzher.naytiKnigu()
            "4" -> menedzher.otmetitKakProchitannuyu()
            "5" -> menedzher.pokazatStatistiku()
            "6" -> menedzher.pokazatRekomendacii()
            "0" -> {
                println("\nДо свидания!")
                return
            }
            else -> println("Неверный выбор. Попробуйте снова.")
        }
    }
}

fun vyvestiMenu() {
    println("\n" + "=".repeat(40))
    println("=== ДОМАШНЯЯ БИБЛИОТЕКА ===")
    println("=".repeat(40))
    println("1. Все книги")
    println("2. Добавить книгу")
    println("3. Найти книгу")
    println("4. Отметить как прочитанную")
    println("5. Статистика")
    println("6. Рекомендации")
    println("0. Выход")
    println("=".repeat(40))
    print("Выберите действие: ")
}
