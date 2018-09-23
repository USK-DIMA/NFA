package ru.uskov.dmitry.nfa

fun getAlphabet(): Set<Char> {
    val alphabet = mutableSetOf<Char>()
    for (c in 32..127) {
        alphabet.add(c.toChar())
    }
    return alphabet
}


fun containNFA(input: String): NFA {
    val alphabet = getAlphabet()
    if (input.isEmpty()) {
        throw IllegalArgumentException("String is empty. You should use not empty string")
    }
    val nameGenerator = StateNameGenerator()
    val states = mutableListOf<State>()
    val startState = State(nameGenerator.next())

    var previousState = startState
    states.add(previousState)
    for (ch in input) {
        val newState = State(nameGenerator.next())
        previousState.addNextState(ch, newState)
        states.add(newState)
        previousState = newState
    }
    val finalState = states.last()
    for (ch in alphabet) {
        finalState.addNextState(ch, finalState)
        startState.addNextState(ch, startState)
    }

    return NFA.create(startState, linkedSetOf(finalState), states, alphabet)
}


private class StateNameGenerator {
    private var currentIndex = 0
    private val prefix = "q"

    fun next(): String = prefix + (currentIndex++)

}