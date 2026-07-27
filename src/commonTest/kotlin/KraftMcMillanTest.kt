import kotlin.test.*

class KraftMcMillanTest {

    @Test
    fun `uniform binary code satisfies inequality with sum exactly 1`() {
        // 4 codewords of length 2, alphabet size 2: sum = 4 * (1/4) = 1.0
        val code = Code(setOf("00", "01", "10", "11"))
        val result = KraftMcMillan.evaluate(code)
        assertTrue(result.isSatisfied)
        assertTrue(kotlin.math.abs(result.sum - 1.0) < 1e-9)
    }

    @Test
    fun `prefix code satisfies inequality`() {
        // sum = 1/2 + 1/4 + 1/8 + 1/8 = 1.0
        val code = Code(setOf("0", "10", "110", "111"))
        val result = KraftMcMillan.evaluate(code)
        assertTrue(result.isSatisfied)
    }

    @Test
    fun `non-UD code violates inequality`() {
        // "0","01","10","1" → sum = 0.5 + 0.25 + 0.25 + 0.5 = 1.5 > 1
        val code = Code(setOf("0", "01", "10", "1"))
        val result = KraftMcMillan.evaluate(code)
        assertFalse(result.isSatisfied)
        assertTrue(result.sum > 1.0)
    }
}