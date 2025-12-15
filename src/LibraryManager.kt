class LibraryManager {
    private val knigi = mutableListOf<Book>()
    private val fileHandler = FileHandler()
    private var sleduyushchiyId = 1

    init {
        zagruzitKnigi()
    }

    private fun zagruzitKnigi() {
        val zagruzhennyeKnigi = fileHandler.zagruzitKnigi()
        for (kniga in zagruzhennyeKnigi) {
            knigi.add(kniga)
            if (kniga.id >= sleduyushchiyId) {
                sleduyushchiyId = kniga.id + 1
            }
        }
    }

    // 1. Показать все книги
    fun pokazatVseKnigi() {
        if (knigi.isEmpty()) {
            println("\nБиблиотека пуста.")
            return
        }

        println("\n=== ВСЕ КНИГИ (${knigi.size}) ===")
        for (kniga in knigi) {
            vyvestiKnigu(kniga)
        }
    }

    private fun vyvestiKnigu(kniga: Book) {
        val statusChtenia = if (kniga.isRead) "Прочитана" else "Не прочитана"

        var ocenka = "Нет оценки"
        if (kniga.rating != null) {
            ocenka = ""
            for (i in 1..kniga.rating) {
                ocenka += "★"
            }
        }

        println("\n[ID: ${kniga.id}]")
        println("Название: ${kniga.title}")
        println("Автор: ${kniga.author}")
        println("Год: ${kniga.year}")
        println("Жанр: ${kniga.genre}")
        println("Статус: $statusChtenia")
        println("Оценка: $ocenka")
        if (kniga.location.isNotEmpty()) {
            println("Расположение: ${kniga.location}")
        }
        println("-".repeat(50))
    }

    // 2. Добавить книгу
    fun dobavitKnigu() {
        println("\n=== ДОБАВЛЕНИЕ КНИГИ ===")

        print("Введите название: ")
        val nazvanie = readLine() ?: ""

        print("Введите автора: ")
        val avtor = readLine() ?: ""

        print("Введите год издания: ")
        val god = readLine()?.toIntOrNull() ?: 2024

        print("Введите жанр: ")
        val zhanr = readLine() ?: ""

        print("Введите расположение (необязательно): ")
        val raspolozhenie = readLine() ?: ""

        val kniga = Book(
            id = sleduyushchiyId++,
            title = nazvanie,
            author = avtor,
            year = god,
            genre = zhanr,
            isRead = false,
            rating = null,
            location = raspolozhenie
        )

        knigi.add(kniga)
        fileHandler.sohranitKnigi(knigi)
        println("\nКнига успешно добавлена! ID: ${kniga.id}")
    }

    // 3. Найти книгу
    fun naytiKnigu() {
        println("\n=== ПОИСК КНИГ ===")
        println("1. По названию")
        println("2. По автору")
        println("3. По жанру")
        println("4. По году")
        println("5. По статусу прочтения")
        println("6. По рейтингу")
        print("Выберите критерий поиска: ")

        val rezultat = when (readLine()) {
            "1" -> poiskPoNazvaniju()
            "2" -> poiskPoAvtoru()
            "3" -> poiskPoZhanru()
            "4" -> poiskPoGodu()
            "5" -> filtrPoStatusu()
            "6" -> filtrPoReytingu()
            else -> {
                println("Неверный выбор.")
                return
            }
        }

        pokazatRezultaty(rezultat)
    }

    private fun poiskPoNazvaniju(): List<Book> {
        print("Введите название (или часть): ")
        val zapros = readLine()?.lowercase() ?: ""

        val rezultat = mutableListOf<Book>()
        for (kniga in knigi) {
            if (kniga.title.lowercase().contains(zapros)) {
                rezultat.add(kniga)
            }
        }
        return rezultat
    }

    private fun poiskPoAvtoru(): List<Book> {
        print("Введите автора (или часть): ")
        val zapros = readLine()?.lowercase() ?: ""

        val rezultat = mutableListOf<Book>()
        for (kniga in knigi) {
            if (kniga.author.lowercase().contains(zapros)) {
                rezultat.add(kniga)
            }
        }
        return rezultat
    }

    private fun poiskPoZhanru(): List<Book> {
        print("Введите жанр: ")
        val zhanr = readLine() ?: ""

        val rezultat = mutableListOf<Book>()
        for (kniga in knigi) {
            if (kniga.genre.equals(zhanr, ignoreCase = true)) {
                rezultat.add(kniga)
            }
        }
        return rezultat
    }

    private fun poiskPoGodu(): List<Book> {
        print("Введите год: ")
        val god = readLine()?.toIntOrNull()
        if (god == null) return emptyList()

        val rezultat = mutableListOf<Book>()
        for (kniga in knigi) {
            if (kniga.year == god) {
                rezultat.add(kniga)
            }
        }
        return rezultat
    }

    private fun filtrPoStatusu(): List<Book> {
        print("Показать прочитанные? (да/нет): ")
        val prochitannye = readLine()?.lowercase() == "да"

        val rezultat = mutableListOf<Book>()
        for (kniga in knigi) {
            if (kniga.isRead == prochitannye) {
                rezultat.add(kniga)
            }
        }
        return rezultat
    }

    private fun filtrPoReytingu(): List<Book> {
        print("Минимальный рейтинг (1-5): ")
        val minReyting = readLine()?.toIntOrNull() ?: 1

        val rezultat = mutableListOf<Book>()
        for (kniga in knigi) {
            val ocenka = kniga.rating ?: 0
            if (ocenka >= minReyting) {
                rezultat.add(kniga)
            }
        }
        return rezultat
    }

    private fun pokazatRezultaty(naydennye: List<Book>) {
        if (naydennye.isEmpty()) {
            println("\nКниги не найдены.")
        } else {
            println("\nНайдено книг: ${naydennye.size}")
            for (kniga in naydennye) {
                vyvestiKnigu(kniga)
            }

            println("\nДействия с результатами:")
            println("1. Редактировать книгу")
            println("2. Удалить книгу")
            println("0. Назад")
            print("Выбор: ")

            when (readLine()) {
                "1" -> redaktirovatKnigu()
                "2" -> udalitKnigu()
            }
        }
    }

    // Редактирование книги
    private fun redaktirovatKnigu() {
        print("\nВведите ID книги для редактирования: ")
        val id = readLine()?.toIntOrNull() ?: return

        var indeks = -1
        for (i in 0 until knigi.size) {
            if (knigi[i].id == id) {
                indeks = i
                break
            }
        }

        if (indeks == -1) {
            println("Книга не найдена.")
            return
        }

        val starayaKniga = knigi[indeks]

        print("Новое название (Enter - оставить '${starayaKniga.title}'): ")
        val nazvanieVvod = readLine()
        val nazvanie = if (nazvanieVvod.isNullOrEmpty()) starayaKniga.title else nazvanieVvod

        print("Новый автор (Enter - оставить '${starayaKniga.author}'): ")
        val avtorVvod = readLine()
        val avtor = if (avtorVvod.isNullOrEmpty()) starayaKniga.author else avtorVvod

        print("Новый жанр (Enter - оставить '${starayaKniga.genre}'): ")
        val zhanrVvod = readLine()
        val zhanr = if (zhanrVvod.isNullOrEmpty()) starayaKniga.genre else zhanrVvod

        print("Новое расположение (Enter - оставить '${starayaKniga.location}'): ")
        val raspolozhenieVvod = readLine()
        val raspolozhenie = if (raspolozhenieVvod.isNullOrEmpty()) starayaKniga.location else raspolozhenieVvod

        knigi[indeks] = starayaKniga.copy(
            title = nazvanie,
            author = avtor,
            genre = zhanr,
            location = raspolozhenie
        )

        fileHandler.sohranitKnigi(knigi)
        println("Книга успешно обновлена!")
    }

    // Удаление книги
    private fun udalitKnigu() {
        print("\nВведите ID книги для удаления: ")
        val id = readLine()?.toIntOrNull() ?: return

        var udaleno = false
        for (i in knigi.size - 1 downTo 0) {
            if (knigi[i].id == id) {
                knigi.removeAt(i)
                udaleno = true
                break
            }
        }

        if (udaleno) {
            fileHandler.sohranitKnigi(knigi)
            println("Книга ID $id успешно удалена!")
        } else {
            println("Книга не найдена.")
        }
    }

    // 4. Отметить как прочитанную
    fun otmetitKakProchitannuyu() {
        print("\nВведите ID книги: ")
        val id = readLine()?.toIntOrNull() ?: return

        var indeks = -1
        for (i in 0 until knigi.size) {
            if (knigi[i].id == id) {
                indeks = i
                break
            }
        }

        if (indeks == -1) {
            println("Книга не найдена.")
            return
        }

        val kniga = knigi[indeks]
        if (kniga.isRead) {
            println("Книга уже отмечена как прочитанная.")
            return
        }

        print("Поставьте оценку (1-5) или нажмите Enter для пропуска: ")
        val ocenkaVvod = readLine()
        var ocenka: Int? = null
        if (ocenkaVvod != null) {
            val temp = ocenkaVvod.toIntOrNull()
            if (temp != null && temp >= 1 && temp <= 5) {
                ocenka = temp
            }
        }

        knigi[indeks] = kniga.copy(isRead = true, rating = ocenka)
        fileHandler.sohranitKnigi(knigi)

        val tekstOcenki = if (ocenka != null) " с оценкой $ocenka★" else ""
        println("Книга отмечена как прочитанная$tekstOcenki!")
    }

    // 5. Статистика
    fun pokazatStatistiku() {
        if (knigi.isEmpty()) {
            println("\nБиблиотека пуста. Статистика недоступна.")
            return
        }

        val vsego = knigi.size
        var prochitano = 0
        for (kniga in knigi) {
            if (kniga.isRead) prochitano++
        }

        val neprochitano = vsego - prochitano
        val procentProchitano = (prochitano * 100.0 / vsego).toInt()

        // Подсчет книг по жанрам
        val poZhanram = mutableMapOf<String, Int>()
        for (kniga in knigi) {
            val tekushcheeKolichestvo = poZhanram[kniga.genre] ?: 0
            poZhanram[kniga.genre] = tekushcheeKolichestvo + 1
        }

        // Поиск любимого жанра
        var lyubimyyZhanr = "Нет данных"
        var maxKolichestvo = 0
        for ((zhanr, kolichestvo) in poZhanram) {
            if (kolichestvo > maxKolichestvo) {
                maxKolichestvo = kolichestvo
                lyubimyyZhanr = zhanr
            }
        }

        // Средний рейтинг
        var summaReytingov = 0.0
        var knigSReyitngom = 0
        for (kniga in knigi) {
            if (kniga.rating != null) {
                summaReytingov += kniga.rating
                knigSReyitngom++
            }
        }

        val sredniyReyting = if (knigSReyitngom > 0) {
            summaReytingov / knigSReyitngom
        } else {
            0.0
        }

        println("\n" + "=".repeat(50))
        println("=== СТАТИСТИКА БИБЛИОТЕКИ ===")
        println("=".repeat(50))
        println("Всего книг: $vsego")
        println("Прочитано: $prochitano ($procentProchitano%)")
        println("Не прочитано: $neprochitano")
        println("Средний рейтинг: ${"%.1f".format(sredniyReyting)}★")
        println("Любимый жанр: $lyubimyyZhanr")

        // Статистика по жанрам
        println("\nКниги по жанрам:")
        for ((zhanr, vsego) in poZhanram) {
            var prochitanoVZhanre = 0
            for (kniga in knigi) {
                if (kniga.genre == zhanr && kniga.isRead) {
                    prochitanoVZhanre++
                }
            }
            println("  • $zhanr: $vsego книг ($prochitanoVZhanre прочитано)")
        }

        // Топ-5 книг по рейтингу
        val knigisOcenkami = mutableListOf<Book>()
        for (kniga in knigi) {
            if (kniga.rating != null) {
                knigisOcenkami.add(kniga)
            }
        }

        // Сортировка вручную (пузырьком)
        for (i in 0 until knigisOcenkami.size - 1) {
            for (j in 0 until knigisOcenkami.size - i - 1) {
                val ocenka1 = knigisOcenkami[j].rating ?: 0
                val ocenka2 = knigisOcenkami[j + 1].rating ?: 0
                if (ocenka1 < ocenka2) {
                    val temp = knigisOcenkami[j]
                    knigisOcenkami[j] = knigisOcenkami[j + 1]
                    knigisOcenkami[j + 1] = temp
                }
            }
        }

        if (knigisOcenkami.isNotEmpty()) {
            println("\nТоп-5 книг по рейтингу:")
            val limit = if (knigisOcenkami.size < 5) knigisOcenkami.size else 5
            for (i in 0 until limit) {
                val kniga = knigisOcenkami[i]
                println("  ${kniga.rating}★ - ${kniga.title} (${kniga.author})")
            }
        }

        println("=".repeat(50))
    }

    // 6. Рекомендации
    fun pokazatRekomendacii() {
        if (knigi.isEmpty()) {
            println("\nНедостаточно данных для рекомендаций.")
            return
        }

        val prochitannye = mutableListOf<Book>()
        for (kniga in knigi) {
            if (kniga.isRead) {
                prochitannye.add(kniga)
            }
        }

        if (prochitannye.isEmpty()) {
            println("\nПрочитайте хотя бы одну книгу, чтобы получить рекомендации.")
            return
        }

        // Анализ любимых жанров (рейтинг >= 4)
        val poZhanram = mutableMapOf<String, Int>()
        for (kniga in prochitannye) {
            val ocenka = kniga.rating ?: 0
            if (ocenka >= 4) {
                val tekushcheeKolichestvo = poZhanram[kniga.genre] ?: 0
                poZhanram[kniga.genre] = tekushcheeKolichestvo + 1
            }
        }

        if (poZhanram.isEmpty()) {
            println("\nНедостаточно данных для рекомендаций. Оцените прочитанные книги!")
            return
        }

        // Сортировка жанров по популярности
        val zhanrySpisok = mutableListOf<Pair<String, Int>>()
        for ((zhanr, kolichestvo) in poZhanram) {
            zhanrySpisok.add(Pair(zhanr, kolichestvo))
        }

        // Сортировка
        for (i in 0 until zhanrySpisok.size - 1) {
            for (j in 0 until zhanrySpisok.size - i - 1) {
                if (zhanrySpisok[j].second < zhanrySpisok[j + 1].second) {
                    val temp = zhanrySpisok[j]
                    zhanrySpisok[j] = zhanrySpisok[j + 1]
                    zhanrySpisok[j + 1] = temp
                }
            }
        }

        val lyubimyeZhanry = mutableListOf<String>()
        val limit = if (zhanrySpisok.size < 3) zhanrySpisok.size else 3
        for (i in 0 until limit) {
            lyubimyeZhanry.add(zhanrySpisok[i].first)
        }

        // Поиск непрочитанных книг в любимых жанрах
        val rekomendacii = mutableListOf<Book>()
        for (kniga in knigi) {
            if (!kniga.isRead && lyubimyeZhanry.contains(kniga.genre)) {
                rekomendacii.add(kniga)
            }
        }

        println("\n=== РЕКОМЕНДАЦИИ ДЛЯ ВАС ===")
        var zhanryText = ""
        for (i in lyubimyeZhanry.indices) {
            zhanryText += lyubimyeZhanry[i]
            if (i < lyubimyeZhanry.size - 1) zhanryText += ", "
        }
        println("На основе ваших любимых жанров: $zhanryText")
        println()

        if (rekomendacii.isEmpty()) {
            println("Все книги в любимых жанрах уже прочитаны!")
            println("\nПопробуйте новые жанры:")

            val novyeZhanry = mutableListOf<String>()
            for (kniga in knigi) {
                if (!kniga.isRead && !lyubimyeZhanry.contains(kniga.genre)) {
                    if (!novyeZhanry.contains(kniga.genre)) {
                        novyeZhanry.add(kniga.genre)
                        if (novyeZhanry.size >= 3) break
                    }
                }
            }

            for (zhanr in novyeZhanry) {
                for (kniga in knigi) {
                    if (kniga.genre == zhanr && !kniga.isRead) {
                        println("• ${kniga.title} - ${kniga.author} (${kniga.genre})")
                        break
                    }
                }
            }
        } else {
            val limit = if (rekomendacii.size < 5) rekomendacii.size else 5
            for (i in 0 until limit) {
                val kniga = rekomendacii[i]
                println("${i + 1}. ${kniga.title}")
                println("   Автор: ${kniga.author}")
                println("   Жанр: ${kniga.genre}")
                println("   Год: ${kniga.year}")
                if (kniga.location.isNotEmpty()) {
                    println("   Расположение: ${kniga.location}")
                }
                println()
            }
        }
    }
}
