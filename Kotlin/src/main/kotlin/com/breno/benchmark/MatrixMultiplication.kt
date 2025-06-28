package com.breno.benchmark

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Multiplicação de Matrizes Sequencial.
 * Recebe duas matrizes A e B e retorna a matriz resultante C.
 */
fun multiplyMatricesSequential(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray> {
    val numRowsA = A.size
    val numColsA = A[0].size
    val numColsB = B[0].size
    val C = Array(numRowsA) { DoubleArray(numColsB) }

    for (i in 0 until numRowsA) {
        for (j in 0 until numColsB) {
            var sum = 0.0
            for (k in 0 until numColsA) {
                sum += A[i][k] * B[k][j]
            }
            C[i][j] = sum
        }
    }
    return C
}

/**
 * Multiplicação de Matrizes Paralela usando Corrotinas.
 * A lógica de dividir o trabalho entre threads é encapsulada aqui.
 */
suspend fun multiplyMatricesParallel(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray> =
    coroutineScope {
        val numRowsA = A.size
        val numColsB = B[0].size
        val C = Array(numRowsA) { DoubleArray(numColsB) }

        // O número de tarefas será o número de linhas. O dispatcher se encarrega
        // de distribuir eficientemente essas tarefas entre as threads disponíveis.
        val jobs = List(numRowsA) { i ->
            async(Dispatchers.Default) { // Usa o pool de threads padrão para trabalho de CPU
                for (j in 0 until numColsB) {
                    var sum = 0.0
                    for (k in 0 until A[0].size) {
                        sum += A[i][k] * B[k][j]
                    }
                    C[i][j] = sum
                }
            }
        }

        jobs.awaitAll() // Espera o cálculo de todas as linhas terminar

        return@coroutineScope C // Retorna a matriz C completa
    }