package ru.uskov.dmitry

import ru.uskov.dmitry.nfa.containNFA

fun main(args: Array<String>) {
    val matcher = containNFA("to be")
    println(matcher.check("To be or not to be")) //true
    println(matcher.check("to to be")) //true
    println(matcher.check("to be be")) //true
    println(matcher.check("Text")) //false
    println(matcher.check("to")) //false
    println(matcher.check("be")) //false
}