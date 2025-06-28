// src/main/kotlin/com/breno/benchmark/Quicksort.kt
package com.breno.benchmark

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

// Partição usando pivô aleatório
private fun randomPartition(arr: IntArray, low: Int, high: Int): Int {
    val pivotIndex = Random.nextInt(low, high + 1)
    arr.swap(pivotIndex, high)
    return partition(arr, low, high)
}

// Helper para a partição
private fun partition(arr: IntArray, low: Int, high: Int): Int {
    val pivot = arr[high]
    var i = low - 1
    for (j in low until high) {
        if (arr[j] <= pivot) {
            i++
            arr.swap(i, j)
        }
    }
    arr.swap(i + 1, high)
    return i + 1
}

// Helper para trocar elementos
private fun IntArray.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}


// --- Funções Públicas que serão chamadas pelo Main ---

/**
 * QuickSort Sequencial.
 */
fun quickSortSequential(arr: IntArray, low: Int = 0, high: Int = arr.size - 1) {
    if (low < high) {
        val pivotIndex = randomPartition(arr, low, high)
        quickSortSequential(arr, low, pivotIndex - 1)
        quickSortSequential(arr, pivotIndex + 1, high)
    }
}

/**
 * QuickSort Paralelo usando Corrotinas.
 */
suspend fun quickSortParallel(arr: IntArray, low: Int = 0, high: Int = arr.size - 1, depth: Int, maxDepth: Int): Unit =
    coroutineScope {
        if (low < high) {
            val pivotIndex = randomPartition(arr, low, high)

            if (depth < maxDepth) {
                // Agora o compilador não terá mais dúvidas sobre o tipo de retorno
                val leftJob = async { quickSortParallel(arr, low, pivotIndex - 1, depth + 1, maxDepth) }
                val rightJob = async { quickSortParallel(arr, pivotIndex + 1, high, depth + 1, maxDepth) }

                leftJob.await()
                rightJob.await()
            } else {
                // Atingiu a profundidade máxima, continua sequencialmente
                quickSortSequential(arr, low, pivotIndex - 1)
                quickSortSequential(arr, pivotIndex + 1, high)
            }
        }
    }