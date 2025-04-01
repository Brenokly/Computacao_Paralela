package matriz

import java.io.File

// Função para multiplicar matrizes em um intervalo e linhas especificado
suspend fun multiplyMatrices(a: Array<DoubleArray>, b: Array<DoubleArray>, startRow: Int, endRow: Int): Array<DoubleArray> {
    val result = Array(endRow - startRow) { DoubleArray(b[0].size) }
    for (i in startRow until endRow) { // Iterar sobre as linhas da matriz A
        for (j in b[0].indices) {           // Iterar sobre as colunas da matriz B
            result[i - startRow][j] = b.indices.sumOf { a[i][it] * b[it][j] }
        }
    }
    return result
}

// Multiplicação sem paralelismo
fun multiplyMatrices(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
    val result = Array(a.size) { DoubleArray(b[0].size) }
    for (i in a.indices) {              // Iterar sobre as linhas da matriz A
        for (j in b[0].indices) {       // Iterar sobre as colunas da matriz B
            result[i][j] = b.indices.sumOf { a[i][it] * b[it][j] }
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