package ru.uskov.dmitry.nfa

import ru.uskov.dmitry.util.containsAny
import java.util.stream.Collectors

class NFA(
        private val startState: State,
        private val finalStates: Collection<State>,
        private val states: Collection<State>,
        private val alphabet: Set<Char>
) {

    fun check(w: String): Boolean {
        var currentStates = listOf(startState)
        for (char in w) {
            currentStates = currentStates.flatMap { it.getNextStates(char) }
        }
        return currentStates.containsAny(finalStates)
    }

    companion object {

        /**
         * Create NFA from String
         * String example:
         *      q0 // start state
         *      qk // fina state
         *      // table
         *      q0,0=q0,q1
         *      q0,1=q0
         *      q1,1=q2
         *      q2,0=qk
         */
        fun create(input: String): NFA {
            try {
                //line[0] is start state
                //lines[1] is list of final state
                //lines[x] x>1 is a description of states: q0,c=q1,q2,q3, q0 - state, c - char, q1,q2,q3 - next states
                val lines = filterInputLines(input.replace(" ", "").split('\n'))
                val finalStates = lines[1].split(',')
                val startStateName = lines[0]
                val table: MutableMap<String, MutableMap<Char, List<String>>> = mutableMapOf()
                for (i in 2 until lines.size) {
                    if (lines[i].isBlank()) {
                        continue
                    }
                    val split = lines[i].split('=')// q0,c=q1,q2,q3 split to split[0]={q0,c} and split[1]={q1,q2,q3}
                    val (stateName, char) = split[0].split(',')
                    val nextStates = split[1].split(',')
                    var charToNextStates = table[stateName]
                    if (charToNextStates == null) {
                        charToNextStates = mutableMapOf()
                        table[stateName] = charToNextStates
                    }
                    charToNextStates[char[0]] = nextStates
                }
                return create(table, startStateName, finalStates)
            } catch (e: Exception) {
                throw InvalidInputException(e)
            }
        }

        fun create(map: Map<String, Map<Char, List<String>>>, startStateName: String, finalStatesNames: List<String>): NFA {
            val startState: State
            val finalStates: List<State>
            val tmpStates: MutableMap<String, State> = mutableMapOf()
            val alphabetChar = mutableSetOf<Char>()
            try {
                startState = getOrCreateState(tmpStates, startStateName)
                finalStates = finalStatesNames.map { getOrCreateState(tmpStates, it) }

                for ((stateName, charToNextStates) in map) {
                    val state = getOrCreateState(tmpStates, stateName)
                    for ((char, nextStates) in charToNextStates) {
                        state.addNextStates(char, nextStates.map { getOrCreateState(tmpStates, it) })
                        alphabetChar.add(char)
                    }
                }
            } catch (e: Exception) {
                throw InvalidInputException(e)
            }

            return create(startState, finalStates, tmpStates.values, alphabetChar)
        }

        fun create(startState: State, finalStates: Collection<State>, states: Collection<State>, alphabet: Set<Char>): NFA {
            return NFA(startState, finalStates, states, alphabet)
        }

        private fun filterInputLines(inputLines: List<String>): List<String> = inputLines.stream()
                .map { it.substringBefore("//") }
                .filter { it.isNotBlank()}
                .collect(Collectors.toList())

        private fun getOrCreateState(tmpStates: MutableMap<String, State>, name: String): State {
            var state = tmpStates[name]
            if (state == null) {
                state = State(name)
                tmpStates[name] = state
            }
            return state
        }
    }
}

class InvalidInputException(cause: Throwable) : Exception(cause)

class State(val name: String) {

    private val nextStates: MutableMap<Char, MutableList<State>> = mutableMapOf()

    fun addNextStates(char: Char, newStates: Collection<State>) {
        var states = nextStates[char]
        if (states == null) {
            states = mutableListOf()
            nextStates[char] = states
        }
        states.addAll(newStates)
    }

    fun addNextState(char: Char, newState: State) {
        addNextStates(char, listOf(newState))
    }

    fun getNextStates(char: Char): List<State> {
        var states: List<State>? = nextStates[char]
        if (states == null) {
            states = listOf()
        }
        return states
    }


}