package main.kotlin

import kotlinx.coroutines.*
import kotlin.system.measureNanoTime

suspend fun multiplyMatrices(a: Array<IntArray>, b: Array<IntArray>, startRow: Int, endRow: Int): Array<IntArray> {
    val result = Array(endRow - startRow) { IntArray(b[0].size) }
    for (i in startRow until endRow) {  // Usando "until" para evitar erro de índice
        for (j in b[0].indices) {
            result[i - startRow][j] = b.indices.sumOf { a[i][it] * b[it][j] }
        }
    }
    return result
}

fun main() = runBlocking {
    val a = arrayOf(
        intArrayOf(1, 2),
        intArrayOf(3, 4),
        intArrayOf(5, 6)
    )
    val b = arrayOf(
        intArrayOf(7, 8),
        intArrayOf(9, 10)
    )

    val repetitions = 1000
    var totalNanoseconds = 0L

    repeat(repetitions) {
        totalNanoseconds += measureNanoTime {
            val numThreads = 2
            val rowsPerThread = a.size / numThreads
            val deferredResults = mutableListOf<Deferred<Array<IntArray>>>()

            for (i in 0 until numThreads) {  // Corrigido "until" aqui também
                val startRow = i * rowsPerThread
                val endRow = if (i == numThreads - 1) a.size else (i + 1) * rowsPerThread
                deferredResults.add(async {
                    multiplyMatrices(a, b, startRow, endRow)
                })
            }

            val results = deferredResults.awaitAll()
        }
    }

    val averageNanoseconds = totalNanoseconds / repetitions
    val averageMilliseconds = averageNanoseconds / 1_000_000.0
    val averageSeconds = averageNanoseconds / 1_000_000_000.0

    println("\nMédia de tempo de execução em nanosegundos: $averageNanoseconds ns")
    println("Média de tempo de execução em milissegundos: $averageMilliseconds ms")
    println("Média de tempo de execução em segundos: %.10f s".format(averageSeconds))
}