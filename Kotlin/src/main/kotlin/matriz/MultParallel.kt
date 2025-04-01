package kotlinmat

import kotlinx.coroutines.*
import java.io.File
import kotlin.system.measureNanoTime

// Nesse exemplo, tem paralelismo!

/*
    O Dispatchers.Default é um dispatcher de corrotina que distribui a execução das corrotinas para threads do pool de threads
    de background do Kotlin. Ele utiliza múltiplos threads do sistema para realizar o trabalho em paralelo.
    A criação de múltiplas corrotinas (scope.async) com esse dispatcher faz com que cada corrotina seja executada em paralelo,
    otimizando o uso dos núcleos de CPU disponíveis.
*/

// matriz1.csv e matriz2.csv são matrizes de 1473x1473

// matriz3.csv e matriz4.csv são matrizes de 500x500

// matriz5.csv e matriz6.csv são matrizes de 100x100

// Funçãpo para multiplicar matrizes
suspend fun multiplyMatrices(a: Array<DoubleArray>, b: Array<DoubleArray>, startRow: Int, endRow: Int): Array<DoubleArray> {
    val result = Array(endRow - startRow) { DoubleArray(b[0].size) }
    for (i in startRow until endRow) { // Iterar sobre as linhas da matriz A
        for (j in b[0].indices) {   // Iterar sobre as colunas da matriz B
            result[i - startRow][j] = b.indices.sumOf { a[i][it] * b[it][j] }
        }
    }
    return result
}

// Função para ler uma matriz de um arquivo CSV
fun readMatrixFromCsv(filePath: String): Array<DoubleArray> {
    val lines = File(filePath).readLines()
    return lines.map { line ->
        line.split(",").map { it.trim().toDouble() }.toDoubleArray()
    }.toTypedArray()
}

// Função para imprimir uma matriz
fun printMatrix(matrix: Array<DoubleArray>) {
    for (row in matrix) {
        println(row.joinToString(", ") { "%.2f".format(it) })
    }
}

fun main() = runBlocking {
    // Leitura das matrizes de arquivos CSV
    val a = readMatrixFromCsv("D:/GIT/Computacao_Paralela/MatrizParallel/src/matrizproblem/matrizes/matriz1.csv")
    val b = readMatrixFromCsv("D:/GIT/Computacao_Paralela/MatrizParallel/src/matrizproblem/matrizes/matriz2.csv")

    // Configurações iniciais
    val repetitions = 1
    var totalNanoseconds = 0L
    val numThreads = 12
    val rowsPerThread = a.size / numThreads
    var results = listOf<Array<DoubleArray>>()

    // Imprimir o tamanho das matrizes
    println("Tamanho da matriz A: ${a.size}x${a[0].size}")
    println("Tamanho da matriz B: ${b.size}x${b[0].size}")
    println("Número de threads: $numThreads")

    // Criando um escopo de corrotina com SupervisorJob para controlar as corrotinas
    val job = SupervisorJob() // Para não cancelar outras corrotinas em caso de falhas
    val scope = CoroutineScope(Dispatchers.Default + job)   // Escopo de corrotina

    // Repetir o processo de multiplicação de matrizes e medir o tempo médio de execução
    repeat(repetitions) {
        val elapsedTime = measureNanoTime {
            val deferredResults = mutableListOf<Deferred<Array<DoubleArray>>>()

            // Usando o escopo controlado com supervisorJob para distribuir a carga entre os threads
            for (i in 0 until numThreads) {
                val startRow = i * rowsPerThread
                val endRow = if (i == numThreads - 1) a.size else (i + 1) * rowsPerThread
                // Distribui o trabalho em diferentes threads
                deferredResults.add(scope.async {
                    multiplyMatrices(a, b, startRow, endRow)
                })
            }

            // Aguarda todos os resultados
            results = deferredResults.awaitAll()
        }
        totalNanoseconds += elapsedTime
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
    val averageNanoseconds = totalNanoseconds / repetitions
    val averageMilliseconds = averageNanoseconds / 1_000_000.0
    val averageSeconds = averageNanoseconds / 1_000_000_000.0

    // Exibir os resultados
    println("\nMédia de tempo de execução em segundos: %.10f s".format(averageSeconds))
    println("Média de tempo de execução em nanosegundos: $averageNanoseconds ns")
    println("Média de tempo de execução em milissegundos: $averageMilliseconds ms")

    // Imprimir o resultado final da multiplicação
    println("\nResultado da multiplicação das matrizes:")
    //printMatrix(finalResult)
}