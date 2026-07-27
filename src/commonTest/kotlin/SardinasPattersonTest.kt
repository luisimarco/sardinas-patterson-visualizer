import kotlin.test.*

class SardinasPattersonTest {

    // --- getResiduals ---

    @Test
    fun `getResiduals returns empty when no prefix matches`() {
        val result = getResiduals(prefixes = setOf("a", "b"), targets = setOf("c", "d"))
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getResiduals returns empty when prefix equals target`() {
        val result = getResiduals(prefixes = setOf("ab"), targets = setOf("ab"))
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getResiduals ignores prefix longer than target`() {
        val result = getResiduals(prefixes = setOf("abc"), targets = setOf("ab"))
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getResiduals returns correct suffixes`() {
        val result = getResiduals(prefixes = setOf("a"), targets = setOf("ab", "ac"))
        assertEquals(setOf("b", "c"), result)
    }

    @Test
    fun `getResiduals handles multiple prefixes`() {
        val result = getResiduals(prefixes = setOf("a", "b"), targets = setOf("ac", "bd"))
        assertEquals(setOf("c", "d"), result)
    }

    // --- checkUD: Uniquely Decipherable ---

    @Test
    fun `prefix code is UD with empty residual set`() {
        val code = Code(setOf("0", "10", "110", "111"))
        val result = checkUD(code)
        assertEquals(EndState.EMPTY_SET, result.response)
        assertNull(result.ambiguousWords)
    }

    @Test
    fun `uniform binary code is UD`() {
        val code = Code(setOf("00", "01", "10", "11"))
        val result = checkUD(code)
        assertEquals(EndState.EMPTY_SET, result.response)
        assertNull(result.ambiguousWords)
    }

    @Test
    fun `code with infinite cycle is UD`() {
        // S1 = {"0"}, S2 = {"0"} == S1 → cycle detected
        val code = Code(setOf("10", "00", "11", "110"))
        val result = checkUD(code)
        assertEquals(EndState.CYCLE_DETECTED, result.response)
        assertNull(result.ambiguousWords)
    }

    // --- checkUD: NOT Uniquely Decipherable ---

    @Test
    fun `ambiguous binary code is not UD`() {
        val code = Code(setOf("0", "01", "10", "1"))
        val result = checkUD(code)
        assertEquals(EndState.AMBIGUITY_FOUND, result.response)
        // Local val required for smart cast: property access prevents automatic null inference
        val ambiguities = result.ambiguousWords
        assertNotNull(ambiguities)
        assertTrue(ambiguities.isNotEmpty())
    }

    @Test
    fun `complex non-UD code is not UD`() {
        val code = Code(setOf("1", "011", "01110", "1110", "10011"))
        val result = checkUD(code)
        assertEquals(EndState.AMBIGUITY_FOUND, result.response)
    }

    // --- History ---

    @Test
    fun `history is empty when residual set is immediately empty`() {
        val code = Code(setOf("0", "10", "110", "111"))
        val result = checkUD(code)
        assertTrue(result.history.isEmpty())
    }

    @Test
    fun `history is populated for multi-step execution`() {
        val code = Code(setOf("01", "10", "011", "110"))
        val result = checkUD(code)
        assertTrue(result.history.isNotEmpty())
    }
}