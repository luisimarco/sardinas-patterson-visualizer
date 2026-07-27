/**
 * Represents a code as a finite set of non-empty words over a given alphabet.
 *
 * Validates the input on construction and exposes statistical properties
 * such as cardinality, alphabet, and average codeword length.
 *
 * @property codeWords The set of words that form the code.
 * @throws IllegalArgumentException if [codeWords] contains fewer
 * than two words, or contains empty strings.
 */
data class Code(val codeWords: Set<String>) {
    init {
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
        // String.format() is JVM-only and unavailable in commonMain (Kotlin Multiplatform).
        // averageLength is rounded to up to 3 decimal places; trailing zeros are omitted.
        val formattedAvg = (kotlin.math.round(averageLength * 1000) / 1000.0).toString()
        return "Code Stats: Size=$cardinality, Avg Length=$formattedAvg, Alphabet=$alphabet"
    }
}