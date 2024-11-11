package kotlinmat

import kotlin.system.measureNanoTime

fun multiplyMatrices(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
    val result = Array(a.size) { DoubleArray(b[0].size) }
    for (i in a.indices) {              // Iterar sobre as linhas da matriz A
        for (j in b[0].indices) {       // Iterar sobre as colunas da matriz B
            result[i][j] = b.indices.sumOf { a[i][it] * b[it][j] }
        }
    }
    return result
}

fun main() {
    // Leitura das matrizes de arquivos CSV
    val a = readMatrixFromCsv("src/matrizes/matriz3.csv")
    val b = readMatrixFromCsv("src/matrizes/matriz4.csv")

    // Configurações iniciais
    val repetitions = 1             // Número de repetições para medir o tempo médio
    var totalNanoseconds = 0L       // Tempo total em nanosegundos

    // Imprimir o tamanho das matrizes
    println("Tamanho da matriz A: ${a.size}x${a[0].size}")
    println("Tamanho da matriz B: ${b.size}x${b[0].size}")

    // Repetir o processo de multiplicação de matrizes e medir o tempo médio de execução
    repeat(repetitions) {
        val elapsedTime = measureNanoTime {
            val finalResult = multiplyMatrices(a, b) // Multiplicação das matrizes sem paralelismo
        }
        totalNanoseconds += elapsedTime  // Acumula o tempo total
    }

    // Calcular a média
    val averageNanoseconds = totalNanoseconds / repetitions           // Tempo médio em nanosegundos
    val averageMilliseconds = averageNanoseconds / 1_000_000.0       // Tempo médio em milissegundos
    val averageSeconds = averageNanoseconds / 1_000_000_000.0        // Tempo médio em segundos

    // Exibir os resultados
    println("\nMédia de tempo de execução em segundos: %.10f s".format(averageSeconds))
    println("Média de tempo de execução em nanosegundos: $averageNanoseconds ns")
    println("Média de tempo de execução em milissegundos: $averageMilliseconds ms")

    // Imprimir o resultado final da multiplicação
    val finalResult = multiplyMatrices(a, b)
    println("\nResultado da multiplicação das matrizes:")
    //printMatrix(finalResult)
}