package kotlinmat

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.measureNanoTime

suspend fun multiplyMatrices(a: Array<DoubleArray>, b: Array<DoubleArray>, startRow: Int, endRow: Int): Array<DoubleArray> {
    val result = Array(endRow - startRow) { DoubleArray(b[0].size) }
    for (i in startRow ..< endRow) { // Iterar sobre as linhas da matriz A
        for (j in b[0].indices) {   // Iterar sobre as colunas da matriz B
            result[i - startRow][j] = b.indices.sumOf { a[i][it] * b[it][j] }
        }
    }
    return result
}

fun readMatrixFromCsv(filePath: String): Array<DoubleArray> {
    // Leitura básica do CSV, convertendo cada linha em uma lista de doubles
    val lines = File(filePath).readLines()
    return lines.map { line ->
        line.split(",").map { it.trim().toDouble() }.toDoubleArray()
    }.toTypedArray()
}

fun printMatrix(matrix: Array<DoubleArray>) {
    for (row in matrix) {
        println(row.joinToString(", ") { "%.2f".format(it) })
    }
}

fun main() = runBlocking {
    // Leitura das matrizes de arquivos CSV
    val a = readMatrixFromCsv("src/matrizes/matriz1.csv")
    val b = readMatrixFromCsv("src/matrizes/matriz2.csv")

    // Configurações iniciais
    val repetitions = 1                                           // Número de repetições para medir o tempo médio
    var totalNanoseconds = 0L                                     // Tempo total em nanosegundos
    val numThreads = 12                                           // Número de threads
    val rowsPerThread = a.size / numThreads                       // Número de linhas por thread
    var results = listOf<Array<DoubleArray>>()                    // Lista de resultados

    // Imprimir o tamanho das matrizes
    println("Tamanho da matriz A: ${a.size}x${a[0].size}")
    println("Tamanho da matriz B: ${b.size}x${b[0].size}")
    println("Número de threads: $numThreads")

    // Repetir o processo de multiplicação de matrizes e medir o tempo médio de execução
    repeat(repetitions) {
        val elapsedTime = measureNanoTime {
            val deferredResults = mutableListOf<Deferred<Array<DoubleArray>>>() // Lista de resultados assíncronos

            for (i in 0 ..< numThreads) {
                val startRow = i * rowsPerThread
                val endRow = if (i == numThreads - 1) a.size else (i + 1) * rowsPerThread
                deferredResults.add(async {
                    multiplyMatrices(a, b, startRow, endRow)
                })
            }

            results = deferredResults.awaitAll()
        }
        totalNanoseconds += elapsedTime  // Acumula o tempo total
    }

    // Combinar os resultados das threads em um único array (finalResult)
    val finalResult = Array(a.size) { DoubleArray(b[0].size) }
    var rowIndex = 0
    for (result in results) {
        for (i in result.indices) {
            finalResult[rowIndex++] = result[i]
        }
    }

    // Calcular a média
    val averageNanoseconds = totalNanoseconds / repetitions           // Tempo médio em nanosegundos
    val averageMilliseconds = averageNanoseconds / 1_000_000.0       // Tempo médio em milissegundos
    val averageSeconds = averageNanoseconds / 1_000_000_000.0       // Tempo médio em segundos

    // Exibir os resultados
    println("\nMédia de tempo de execução em segundos: %.10f s".format(averageSeconds))
    println("Média de tempo de execução em nanosegundos: $averageNanoseconds ns")
    println("Média de tempo de execução em milissegundos: $averageMilliseconds ms")

    // Imprimir o resultado final da multiplicação
    println("\nResultado da multiplicação das matrizes:")
    //printMatrix(finalResult)
}