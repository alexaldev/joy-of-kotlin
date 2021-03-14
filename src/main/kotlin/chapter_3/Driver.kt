package chapter_3

fun triple(a: Int) = a*3
fun square(a: Int) = a*a

fun <T, R, U> compose(f: (T) -> R, g: (U) -> T): (U) -> R = { x -> f(g(x)) }

val add: (Int) -> (Int) -> Int = {a -> {b -> a + b}}

fun main() {

    println(compose(::triple, ::square)(2))
    println(add(2)(5))
}