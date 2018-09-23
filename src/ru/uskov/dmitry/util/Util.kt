package ru.uskov.dmitry.util

fun <T> Collection<T>.containsAny(col: Collection<T>): Boolean {
    for (t in col) {
        if (this.contains(t)) {
            return true
        }
    }
    return false
}