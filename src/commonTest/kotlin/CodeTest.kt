import kotlin.test.*

class CodeTest {

    // --- Validation ---

    @Test
    fun `single codeword throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            Code(setOf("0"))
        }
    }

    @Test
    fun `empty set throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            Code(emptySet())
        }
    }

    @Test
    fun `empty word throws exception`() {
        assertFailsWith<IllegalArgumentException> {
            Code(setOf("0", ""))
        }
    }

    // --- Properties ---

    @Test
    fun `cardinality is correct`() {
        val code = Code(setOf("0", "10", "110", "111"))
        assertEquals(4, code.cardinality)
    }

    @Test
    fun `alphabet is correct`() {
        val code = Code(setOf("0", "10", "110", "111"))
        assertEquals(setOf('0', '1'), code.alphabet)
    }

    @Test
    fun `average length is correct`() {
        // lengths: 1 + 2 + 3 + 3 = 9, / 4 = 2.25
        val code = Code(setOf("0", "10", "110", "111"))
        assertEquals(2.25, code.averageLength)
    }
}