package chapter_3

import java.lang.IllegalArgumentException

typealias IntUnaryOp = (Int) -> Int

fun <T, R, U> compose(f: (T) -> R, g: (U) -> T): (U) -> R = { x -> f(g(x)) }

val add: (Int) -> (Int) -> Int = { a -> { b -> a + b } }

val compose: (IntUnaryOp) -> (IntUnaryOp) -> IntUnaryOp =
    { x -> { y -> { z -> x(y(z)) } } }

fun <T, R, U> higherCompose(): ((U) -> T) -> ((R) -> U) -> ((R) -> T) =
    { f ->
        { g ->
            { z ->
                f(g(z))
            }
        }
    }

fun <T, R, U> higherAndThen(): ((T) -> R) -> ((R) -> U) -> ((T) -> U) =
    { f ->
        { g ->
            { x ->
                g(f(x))
            }
        }

    }

val square: IntUnaryOp = { it * it }
val triple: IntUnaryOp = { it * 3 }

val cos = higherCompose<Double, Double, Double>()() { x: Double -> Math.PI / 2 - x }(Math::sin)

val taxRate = 0.09
val addTax = { tax: Double ->
    { price: Double ->
        price + price * tax
    }
}

class TaxComputer(private val taxRate: Double) {
    fun compute(price: Double) = price + price * taxRate
}

// Exercise 3.7
fun <A, B, C> partialA(a: A, f: (A) -> (B) -> C): (B) -> C = f(a)


// 3.3.11
data class Product(val name: String, val price: Price, val weight: Weight)
class Price private constructor(private val value: Double) {

    override fun toString() = value.toString()
    operator fun plus(other: Price) = Price(value + other.value)
    operator fun times(num: Int) = Price(value * num)

    companion object {
        val indentity = Price(0.0)

        operator fun invoke(value: Double) {

            if (value > 0) {
                Price(value)
            } else {
                throw IllegalArgumentException("Price must be a positive")
            }

        }
    }
}

data class Weight(val value: Double) {

    operator fun plus(other: Weight) = Weight(value + other.value)
    operator fun times(num: Int) = Weight(value * num)
}

data class OrderLine(val product: Product, val count: Int) {

    fun weight() = product.weight * count
    fun amount() = product.price * count
}

val zeroWeight = Weight(0.0)
val zeroPrice = Price(0.0)
val priceAddition = { x: Double, y: Double -> x + y }

object Store {

    @JvmStatic
    fun main() {

        val toothPaste = Product("Tooth paste", 1.5, 0.5)
        val toothBrush = Product("Tooth brush", 3.5, 0.3)
        val orderLines = listOf(
            OrderLine(toothPaste, 2),
            OrderLine(toothBrush, 3))
        val weight = orderLines.sumByDouble { it.amount() }
        val price = orderLines.sumByDouble { it.weight() }
        println("Total price: $price")
        println("Total weight: $weight")
    }
}

fun main() {

    println(add(2)(5))
    val squareOfTriple = compose(square)(triple)
    squareOfTriple(3)

    println(higherCompose<Int, Int, Int>()(squareOfTriple)(triple)(1))
    println(higherAndThen<Int, Int, Int>()(triple)(squareOfTriple)(1))


    val tc9 = TaxComputer(0.09)
    println(tc9.compute(12.9))
    val tc9f = addTax(0.09)
    val price = tc9f(12.9)
}