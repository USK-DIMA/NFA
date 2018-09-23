package ru.uskov.dmitry.nfa

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class NfaGeneratorKtTest {

    @Test
    fun generateContain() {
        val generateContain = containNFA("Hello World")
        assertTrue(generateContain.check("Hello World"))
        assertTrue(generateContain.check("Hello Hello World"))
        assertTrue(generateContain.check("Hello World World"))
        assertFalse(generateContain.check("Hell World"))
        assertFalse(generateContain.check(""))
        assertFalse(generateContain.check("Some text"))
        assertFalse(generateContain.check("Hello Worl"))
        assertFalse(generateContain.check("ello World"))
    }
}