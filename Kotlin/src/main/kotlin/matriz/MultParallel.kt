package matriz

import kotlinx.coroutines.*
import quick.printResult
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


fun main() = runBlocking {
    // Leitura das matrizes de arquivos CSV
    val a = readMatrixFromCsv("src/main/kotlin/matriz/matrizes/matriz1.csv")
    val b = readMatrixFromCsv("src/main/kotlin/matriz/matrizes/matriz1.csv")

    // Configurações Gerais
    val repetitions = 10
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
    val finalResultParallel = Array(a.size) { DoubleArray(b[0].size) }
    var rowIndex = 0
    for (result in results) {
        for (i in result.indices) {
            finalResultParallel[rowIndex++] = result[i]
        }
    }

    // Exibir os resultados da multiplicação paralela
    println("\nResultado da multiplicação paralela:")
    printResult(totalNanoseconds / repetitions)

    totalNanoseconds = 0L  // Reinicia o tempo total para a multiplicação sequencial

    var finalResultSequence = Array(0) { doubleArrayOf() }  // Matriz resultante da multiplicação
    // Repetir o processo de multiplicação de matrizes e medir o tempo médio de execução
    repeat(repetitions) {
        val elapsedTime = measureNanoTime {
            finalResultSequence = multiplyMatrices(a, b) // Multiplicação das matrizes sem paralelismo
        }
        totalNanoseconds += elapsedTime  // Acumula o tempo total
    }

    // Exibir os resultados da multiplicação sequencial
    println("\nResultado da multiplicação sequencial:")
    printResult(totalNanoseconds / repetitions)

    // Verifica se os resultados são iguais
    if (finalResultParallel.contentDeepEquals(finalResultSequence)) {
        println("Os resultados são iguais!")
    } else {
        println("Os resultados são diferentes!")
    }
}