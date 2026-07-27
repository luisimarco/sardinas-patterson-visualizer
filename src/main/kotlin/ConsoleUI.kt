/**
 * Represents a menu item with a description and an executable action.
 */
data class MenuItem(
    val description: String,
    val action: () -> Unit
)


class ConsoleUI {
    companion object {
        private const val CLEAR_SCREEN_LINES = 50
        // PRE-DEFINED EXAMPLES
        private val EXAMPLES = mapOf(
            "Simple Prefix Code (UD)" to "0, 10, 110, 111",
            "Classic Infinite Cycle (UD)" to "01, 10, 011, 110",
            "Complex Code (Not UD)" to "a, c, ad, abb, bad, deb, bbcde",
            "Complex Code (UD)" to "abc, abcd, e, dba, bace, ceac, ceab, eabd",
            "Binary (Not UD)" to "010, 0001, 0110, 1100, 00011, 00110, 11110, 101011",
            "Binary Code (Not UD)" to "1, 011, 01110, 1110, 10011",
            "Binary Code UD 1" to "00, 01, 10, 11",
            "Binary Code UD 2" to "0, 10, 110, 1110",
            "Binary Code UD 3" to "00, 01, 011, 0111",
            "Binary Code NOT UD" to "0, 01, 10, 1",
            "Five symbols" to "012, 0123, 4, 310, 1024, 2402, 2401, 34013"
        )
    }

    private var isRunning = true
    private var showSteps = false

    // MENU CONFIGURATION
    private val menuOptions = listOf(
        // Option 1: User Input
        MenuItem("Enter Code") {
            runUserTest()
        },
        // Option 2: Toggle Steps View
        MenuItem("Toggle Output Mode") {
            toggleOutputMode()
        },
        // Option 3: Load Example
        MenuItem("Load Example Code") {
            runExampleSelection()
        },

        // Option 4: Load from File (Placeholder)
        MenuItem("Load from File") {
            println("\n[TODO] File loading feature coming soon.")
        }

    )





    // ENTRY POINT
    fun start() {
        println("Sardinas-Patterson check")
        while (isRunning) {
            printMenu()
            print("\nChoice > ")

            val input = readlnOrNull()?.trim() ?: ""

            if (input == "0") {
                println("Exiting... Goodbye!")
                return
            }
            val index = input.toIntOrNull()
            if (index != null && index in 1..menuOptions.size) {
                // Esegue l'azione corrispondente
                menuOptions[index - 1].action.invoke()
            } else {
                println("\n[!] Invalid choice. Please enter a number between 1 and ${menuOptions.size - 1} (or 0 to exit).")
            }
        }
    }


    // MENU
    private fun printMenu() {
        println("\n========================================")
        println("   SARDINAS-PATTERSON CHECK")
        println("   Current Mode: ${if (showSteps) "SHOW STEPS" else "RESULT ONLY"}")
        println("========================================")
        menuOptions.forEachIndexed { i, item ->
            if (i == 1) {
                // i == 1 corresponds to "Toggle Output Mode" — menu order is intentionally fixed
                println("${i + 1}. ${item.description} (Current: ${if (showSteps) "Show Steps" else "Result Only"})")
            } else {
                // Per tutte le altre opzioni, stampiamo solo numero e descrizione
                println("${i + 1}. ${item.description}")
            }
        }
        println("0. Exit")
    }

    // --- ACTIONS ---
        private fun toggleOutputMode() {
        showSteps = !showSteps
        println("\n>>> Output mode switched to: ${if (showSteps) "SHOW STEPS" else "RESULT ONLY"}")
    }


    private fun runUserTest() {
        println("\n--- USER INPUT TEST ---")
        print("Enter code words separated by comma (e.g., 0, 01, 10): ")
        //val rawInput = scanner.nextLine()
        val rawInput = readln()

        if (rawInput.isBlank()) {
            println("[!] Empty input.")
            return
        }

        try {
            // Parsing
            processInput(rawInput)
        } catch (e: Exception) {
            println("[!] Error: ${e.message}")
        }
    }

    private fun runExampleSelection() {
        println("\n--- SELECT EXAMPLE ---")
        val keys = EXAMPLES.keys.toList()

        keys.forEachIndexed { index, name ->
            println("${index + 1}. $name")
            println("   Code: { ${EXAMPLES[name]} }")
        }

        print("\nSelect number (0 to cancel) > ")
        //val input = scanner.nextLine().toIntOrNull()
        val input = readln().toIntOrNull()
        if (input != null && input > 0 && input <= keys.size) {
            val selectedKey = keys[input - 1]
            val codeString = EXAMPLES[selectedKey] ?: return

            println("\nLoading example: $selectedKey")
            processInput(codeString)
        } else if (input != 0) {
            println("[!] Invalid selection.")
        }
    }

    // --- CORE LOGIC ---
    private fun processInput(rawInput: String) {
        if (rawInput.isBlank()) {
            println("[!] Empty input.")
            return
        }

        try {
            // 1. Parse and Validate Input
            val codeWords = parseInputString(rawInput)
            val code = Code(codeWords)

            println("\nProcessing Code: $codeWords")

            // 2. Run Algorithm (Direct call to the top-level function)
            val result = checkUD(code)

            // 3. Display Results
            printSPTable(code.codeWords, result,showSteps)
            printResult(result)


        } catch (e: IllegalArgumentException) {
            // Captures errors from Code class init block (e.g., empty code)
            println("[!] Validation Error: ${e.message}")
        } catch (e: Exception) {
            println("[!] Unexpected Error: ${e.message}")
        }
    }


    /**
     * Prints the Sardinas-Patterson table.
     *
     * @param codeWords Il codice originale (S0).
     * @param result Algorithm's result.
     * @param showSteps If true, it clears the screen and redraws the table.
     */

    private fun printSPTable(codeWords: Set<String>, result: SPResult, showSteps: Boolean) {

        // 1. PREPARE DATA
        // Merge S0 and History into a single list of columns
        val allColumns = mutableListOf<List<String>>()
        allColumns.add(codeWords.sorted()) // S0
        result.history.forEach { stepSet ->
            allColumns.add(stepSet.sorted()) // S1..Sn
        }

        // 2. DEFINE PRINTING LOGIC (Local Function)
        // Prints a subset of columns (from 0 to limit)
        fun printTableSnapshot(limit: Int) {
            val currentColumns = allColumns.subList(0, limit)

            // Calculate dimensions
            val maxRows = currentColumns.maxOf { it.size }
            val colWidths = currentColumns.mapIndexed { index, col ->
                val titleLen = "S$index".length
                val maxWordLen = col.maxOfOrNull { it.length } ?: 0
                maxOf(titleLen, maxWordLen) + 2
            }

            println("\n--- SARDINAS-PATTERSON TABLE ---")

            // Header
            currentColumns.indices.forEach { i ->
                print("S$i".padEnd(colWidths[i]) + "| ")
            }
            println()

            // Separator
            currentColumns.indices.forEach { i ->
                print("-".repeat(colWidths[i]) + "+-")
            }
            println()

            // Rows
            for (row in 0..<maxRows) {
                for (col in currentColumns.indices) {
                    val word = currentColumns[col].getOrNull(row) ?: ""
                    print(word.padEnd(colWidths[col]) + "| ")
                }
                println()
            }
        }

        // 3. EXECUTE DISPLAY
        if (showSteps) {
            // Animation Loop: Print 1 col, wait, clear, print 2 cols...
            for (i in 1..allColumns.size) {
                // Fake "Clear Screen" (prints 50 empty lines to push old text up)
                println("\n".repeat(50))

                printTableSnapshot(i)

                if (i < allColumns.size) {
                    print("\n>> Step S${i-1} shown. Press ENTER for next step...")
                    readln()
                } else {
                    println("\n>> Table Complete.")
                }
            }
        } else {
            // Instant Mode: Just print everything once
            printTableSnapshot(allColumns.size)
        }
    }

    private fun parseInputString(input: String): Set<String> {
        return input.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()
    }
    private fun printResult(result: SPResult) {
        println("\n--- FINAL VERDICT ---")
        when (result.response) {
            EndState.EMPTY_SET -> println("✅ UNIQUE DECIPHERABILITY (UD). Reason: Residual set became empty.")
            EndState.CYCLE_DETECTED -> println("✅ UNIQUE DECIPHERABILITY (UD). Reason: Infinite loop detected.")
            EndState.AMBIGUITY_FOUND -> {
                println("❌ NOT UD (Ambiguous).")
                if (!result.ambiguousWords.isNullOrEmpty()) {
                    println("Ambiguity found: '${result.ambiguousWords.first()}' is both a code word and a residual.")
                }
            }
        }
    }
}

