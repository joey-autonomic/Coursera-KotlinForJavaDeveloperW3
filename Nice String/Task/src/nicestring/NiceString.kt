package nicestring

fun String.isNice(): Boolean {
    val a = this.bCheck()
    val b = this.vowelCheck()
    val c = this.doubleCheck()
    return (a && b) || (b && c) || (c && a)
}

// No "bu", "ba", "be"
fun String.bCheck(): Boolean {
    for (i in 0 until this.length-1) {
        if (this[i] == 'b'
                && (this[i+1] == 'u' || this[i+1] == 'a' || this[i+1] == 'e')) {
            return false
        }
    }
    return true
}

// Contains three vowels (a,e,i,o,u)
fun String.vowelCheck(): Boolean {
    val vowels = mutableSetOf('a','e','i','o','u')
    var count = 0
    for (c in this) {
        if (c in vowels) count++
    }
    return count >= 3
}

// Contains double letter (e.g. "bb")
fun String.doubleCheck(): Boolean {
    for (i in 0 until this.length-1) {
        if (this[i] == this[i+1]) return true
    }
    return false
}