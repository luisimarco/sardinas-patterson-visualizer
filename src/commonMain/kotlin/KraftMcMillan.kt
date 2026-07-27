import kotlin.math.pow

/**
 * Singleton object encapsulating the evaluation of the Kraft-McMillan inequality.
 *
 * The inequality states that for any uniquely decipherable code with alphabet
 * size d and codeword lengths l_i:
 *
 * Σ d^(-l_i) ≤ 1
 */
object KraftMcMillan {
    // Includes a small tolerance margin to account for floating-point inaccuracies
    private const val UPPER_BOUND = 1.000000001
    /**
     * Data container holding both the calculated sum and the evaluation result.
     *
     * @property sum The computed value of K(C) = Σ d^(-l_i).
     * @property isSatisfied True if [sum] is within the tolerance of 1.0.
     */
    data class Result(val sum: Double, val isSatisfied: Boolean)

    /**
     * Evaluates the Kraft-McMillan inequality for the given [code].
     *
     * @param code The [Code] to be evaluated.
     * @return A [Result] containing the computed sum and whether the inequality is satisfied.
     */
    fun evaluate(code: Code): Result {
        val d = code.alphabet.size.toDouble()
        // Calculate the sum of 1 / (d^length) for each codeword
        val sum = code.codeWords.sumOf { word ->
            1.0 / d.pow(word.length)
        }
        return Result(sum = sum, isSatisfied = (sum <= UPPER_BOUND))
    }
}