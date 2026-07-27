/**
 * Represents a code as a finite set of non-empty words over a given alphabet.
 *
 * Validates the input on construction and exposes statistical properties
 * such as cardinality, alphabet, and average codeword length.
 *
 * @property codeWords The set of words that form the code.
 * @throws IllegalArgumentException if [codeWords] is empty, contains fewer
 * than two words, or contains empty strings.
 */
data class Code(val codeWords: Set<String>) {
    /* INPUT VALIDATION */
    init {
        require(codeWords.isNotEmpty()) {
            "Error: Code cannot be an empty set."
        }
        require(codeWords.size > 1) {
            "Error: Code must contain more than one codeword."
        }
        require(codeWords.none { it.isEmpty() }) {
            "Error: Code cannot contain empty words."
        }
    }

    /*
     * CALCULATED PROPERTIES
     */

    /** The number of codewords in this code (cardinality |C|). */
    val cardinality: Int = codeWords.size

    /** The set of distinct characters used across all codewords (alphabet Σ). */
    val alphabet: Set<Char> by lazy {
        codeWords.flatMap { it.asIterable() }.toSet()
    }

    /** The average length of the codewords. */
    val averageLength: Double by lazy {
        codeWords.sumOf { it.length }.toDouble() / cardinality
    }

    /**
     * Returns a summary of the code's statistical properties.
     *
     * Example output: `Code Stats: Size=4, Avg Length=1.75, Alphabet=[a, b, c]`
     */
    override fun toString(): String {
    /**  "%.2f".format() to limit to 2 decimal places */
        return "Code Stats: Size=$cardinality, Avg Length=${"%.2f".format(averageLength)}, Alphabet=$alphabet"
    }
}