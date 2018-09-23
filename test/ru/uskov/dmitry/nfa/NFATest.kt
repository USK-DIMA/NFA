package ru.uskov.dmitry.nfa

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.uskov.dmitry.nfa.NFA

internal class NFATest {

    @Test
    fun stringInputTest() {
        val input =
                "q0\n" +
                        "qk\n" +
                        "q0,0=q0,q1\n" +
                        "q0,1=q0\n" +
                        "q1,1=q2\n" +
                        "q2,0=qk\n" +
                        "qk,0=qk\n" +
                        "qk,1=qk\n"

        executeStringInputTest(input)
    }

    @Test
    fun stringCommentInputTest() {
        val input =
                " //some comment1\n" +
                        "q0\n" +
                        "qk //some comment 2\n" +
                        "\n" +
                        "q0,0=q0,q1\n" +
                        "q0,1=q0\n" +
                        "q1,1=q2\n" +
                        "q2,0=qk\n" +
                        "qk,0=qk\n" +
                        "qk,1=qk\n"

        executeStringInputTest(input)
    }

    private fun executeStringInputTest(input: String) {
        val nfa = NFA.create(input)
        assertTrue(nfa.check("010"))
        assertTrue(nfa.check("0101"))
        assertTrue(nfa.check("000010"))
        assertTrue(nfa.check("0101010"))
        assertFalse(nfa.check("1"))
        assertFalse(nfa.check("01"))
        assertFalse(nfa.check("011"))
    }
}