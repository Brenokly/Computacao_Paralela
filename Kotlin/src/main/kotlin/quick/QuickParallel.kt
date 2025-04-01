package quick

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureNanoTime

// Benchmark das versões sequencial e paralela
fun main() = runBlocking {
    val size = 1_000_000
    val arraySequential = IntArray(size) { Random.nextInt(0, 100_001) }
    val arrayParallel = arraySequential.copyOf()

    println("Tamanho do array: $size")
    println("Iniciando ordenação...")

    var sequencialTimeTotal = 0L
    var parallelTimeTotal = 0L

    for (i in 0 until 10) {
        val sequentialTime = measureNanoTime {
            quickSort(arraySequential)
        }
        sequencialTimeTotal += sequentialTime
    }

    for (i in 0 until 10) {
        val parallelTime = measureNanoTime {
            parallelQuickSort(arrayParallel)
        }
        parallelTimeTotal += parallelTime
    }

    println("\n--- Resultados ---")
    println("QuickSort Sequencial:")
    printResult(sequencialTimeTotal / 10)
    println("\nQuickSort Paralelo:")
    printResult(parallelTimeTotal / 10)
}