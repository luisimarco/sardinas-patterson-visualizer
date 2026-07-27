/**
 * Helper function to calculate the quotient (residuals) between two sets of strings.
 * Returns the set { w | target = prefix + w }.
 * * Uses Kotlin's 'buildSet' for idiomatic, read-only collection creation.
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

enum class EndState {
    EMPTY_SET,      // Successo: Finiti i residui (Codice Univoco)
    CYCLE_DETECTED, // Successo: Loop infinito innocuo (Codice Univoco)
    AMBIGUITY_FOUND // Fallimento: Trovata ambiguità (Codice NON Univoco)
}

data class SPResult(
    val response: EndState,
    val history: List<Set<String>>,   // passaggi dell'algoritmo
    val ambiguousWords: Set<String>? = null //codewords che generano ambiguità
)


/**
 * Checks if a given Code is Uniquely Decipherable (UD) using the Sardinas-Patterson algorithm.
 *
 * This implementation uses a mathematical approach based on residual sets.
 * It iteratively calculates the quotients between the code and the current set of residuals.
 *
 * @param code The Code object containing the set of words to verify.
 * @return An [SPResult] object containing the final verdict and the history of steps taken.
 */
fun checkUD(code: Code): SPResult {
    val checkedCode = code.codeWords
    // Why LinkedHashSet?
    // 1. Performance: It provides O(1) lookup time to detect infinite cycles (step 3).
    // 2. Ordering: It preserves the insertion order, allowing us to reconstruct
    //    the chronological history of the algorithm for the final report
    val steps = linkedSetOf<Set<String>>()

    // Initial Step (S_1): Calculate residuals of the code against itself.
    // S_1 = { w | exists a, b in S_0 such that a = bw }
    var currentSet = getResiduals(checkedCode, checkedCode)
    while (true) {
        // STOP CONDITION 1: Empty Set
        // If no new residuals are generated, the code is Uniquely Decipherable.
        if (currentSet.isEmpty()) return SPResult(
            response = EndState.EMPTY_SET,
            history = steps.toList() // Pass 'steps' as the history
        )
        // STOP CONDITION 2: Ambiguity Found
        // If a residual word is also present in the original code, the UD property is violated.
        // Intersection check: S_i INTERSECT C != EMPTY
        val ambiguities = currentSet.intersect(checkedCode)
        if (ambiguities.isNotEmpty()) {
            // Final failing set added to history before returning
            steps.add(currentSet)
            return SPResult(EndState.AMBIGUITY_FOUND, steps.toList(), ambiguities)
        }

        // STOP CONDITION 3: Infinite Cycle Detected
        // If we encounter a set of residuals we have already seen, we are in a loop.
        // This implies no new unique residuals will ever be generated. The code is UD.
        if (!steps.add(currentSet)) return SPResult(EndState.CYCLE_DETECTED, steps.toList() + listOf(currentSet))
        // CALCULATE NEXT STEP (S_i)
        // We generate new residuals combining the original code (S_0) and the previous residuals (S_{i-1}).
        // Formula: S_i = { w in A* | exists a in S_0, exists b in S_{i-1} such that a = bw OR b = aw }

        val residuesFromSteps =
            getResiduals(prefixes = checkedCode, targets = currentSet) // Case: b = aw (residue comes from b)
        val residuesFromCode =
            getResiduals(prefixes = currentSet, targets = checkedCode) // Case: a = bw (residue comes from a)
        currentSet = residuesFromSteps + residuesFromCode
    }

}



