/**
 * Computes the residual set as defined by the Sardinas-Patterson algorithm.
 *
 * Implements the suffix quotient operation: for each pair (prefix, target),
 * if target = prefix + w with |w| > 0, then w is added to the result.
 *
 * @param prefixes The set of strings to use as prefixes.
 * @param targets The set of strings to strip prefixes from.
 * @return The set { w | ∃ prefix ∈ prefixes, ∃ target ∈ targets : target = prefix + w }.
 */
fun getResiduals(prefixes: Set<String>, targets: Set<String>): Set<String> {
    return buildSet {
        for (target in targets) {
            for (prefix in prefixes) {
                // Logic: A residual exists only if the 'target' word is strictly longer
                // than the 'prefix' word and starts with it.
                if (target.length > prefix.length && target.startsWith(prefix)) {
                    add(target.removePrefix(prefix))
                }
            }
        }
    }
}


/**
 * Represents the terminal condition reached by the Sardinas-Patterson algorithm.
 */
enum class EndState {
    /** The residual set became empty: the code is uniquely decipherable. */
    EMPTY_SET,
    /** A previously seen residual set was encountered: the code is uniquely decipherable. */
    CYCLE_DETECTED,
    /** A residual word is also a codeword: the code is NOT uniquely decipherable. */
    AMBIGUITY_FOUND
}


/**
 * Holds the outcome of the Sardinas-Patterson algorithm.
 *
 * @property endState The terminal condition that caused the algorithm to halt.
 * @property history The sequence of residual sets computed at each step (S1, S2, ...).
 * @property ambiguousWords The codewords that caused the ambiguity, or null if the code is UD.
 */
data class SPResult(
    val response: EndState,
    val history: List<Set<String>>,
    val ambiguousWords: Set<String>? = null
)


/**
 * Checks if a given [Code] is Uniquely Decipherable (UD) using the Sardinas-Patterson algorithm.
 *
 * This implementation uses a mathematical approach based on residual sets.
 * It iteratively computes residual sets until a terminal condition is reached.
 *
 * @param code The [Code] to verify.
 * @return An [SPResult] object containing the final verdict and the history of steps taken.
 */
fun checkUD(code: Code): SPResult {
    val checkedCode = code.codeWords

    // LinkedHashSet serves two purposes:
    // 1. O(1) lookup to detect repeated sets (cycle detection).
    // 2. Insertion-order preservation for chronological history reconstruction.
    val steps = linkedSetOf<Set<String>>()


    // S1: initial residual set — suffixes left after removing a codeword prefix from another codeword.
    // S1 = { w | exists a, b in S_0 such that a = bw }
    var currentSet = getResiduals(checkedCode, checkedCode)

    while (true) {
        // Terminal condition 1: empty residual set → code is UD.
        if (currentSet.isEmpty()) return SPResult(
            response = EndState.EMPTY_SET,
            history = steps.toList() // Pass 'steps' as the history
        )
        // Terminal condition 2: a residual word is also a codeword → code is NOT UD.
        // Intersection check: S_i INTERSECT C != EMPTY
        val ambiguities = currentSet.intersect(checkedCode)
        if (ambiguities.isNotEmpty()) {
            // Final failing set added to history before returning
            steps.add(currentSet)
            return SPResult(EndState.AMBIGUITY_FOUND, steps.toList(), ambiguities)
        }

        // Terminal condition 3: residual set already seen → infinite cycle → code is UD.
        if (!steps.add(currentSet)) return SPResult(EndState.CYCLE_DETECTED, steps.toList() + listOf(currentSet))
        // CALCULATE NEXT STEP (S_i)
        // We generate new residuals combining the original code (S_0) and the previous residuals (S_{i-1}).
        // Formula: S_i = { w in A* | exists a in S_0, exists b in S_{i-1} such that a = bw OR b = aw }

        // Compute next residual set S_i from both directions (a ∈ C, b ∈ S_{i-1}):
        // - { w | a = bw } : codeword a starts with residual b, w is the remainder
        // - { w | b = aw } : residual b starts with codeword a, w is the remainder
        val residuesFromSteps =
            getResiduals(prefixes = checkedCode, targets = currentSet) // Case: b = aw
        val residuesFromCode =
            getResiduals(prefixes = currentSet, targets = checkedCode) // Case: a = bw
        currentSet = residuesFromSteps + residuesFromCode
    }

}



