package quick

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

// Funções usadas nos Testes

suspend fun parallelQuickSort(arr: IntArray, low: Int = 0, high: Int = arr.size - 1, depth: Int = 0, maxDepth: Int = 10) {
    if (low < high) {
        val pivotIndex = randomPartition(arr, low, high)

        if (depth < maxDepth) {
            // Paraleliza apenas até um certo nível de profundidade
            coroutineScope {
                val left = async(Dispatchers.Default) { parallelQuickSort(arr, low, pivotIndex - 1, depth + 1, maxDepth) }
                val right = async(Dispatchers.Default) { parallelQuickSort(arr, pivotIndex + 1, high, depth + 1, maxDepth) }
                left.await()
                right.await()
            }
        } else {
            // Se atingiu a profundidade máxima, volta ao QuickSort normal
            quickSort(arr, low, pivotIndex - 1)
            quickSort(arr, pivotIndex + 1, high)
        }
    }
}

// QuickSort sequencial
fun quickSort(arr: IntArray, low: Int = 0, high: Int = arr.size - 1) {
    if (low < high) {
        val pivotIndex = randomPartition(arr, low, high)
        quickSort(arr, low, pivotIndex - 1)
        quickSort(arr, pivotIndex + 1, high)
    }
}

fun partition(arr: IntArray, low: Int, high: Int): Int {
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

// Troca de elementos no array
fun IntArray.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

// Função de partição usando pivô aleatório
fun randomPartition(arr: IntArray, low: Int, high: Int): Int {
    val pivotIndex = Random.nextInt(low, high + 1)
    // Troca o pivô aleatório com o último elemento
    arr.swap(pivotIndex, high)
    return partition(arr, low, high)
}

fun printResult(elapsedTimeQuick: Long) {
    // Calcular a média
    val averageNanoseconds = elapsedTimeQuick
    val averageMilliseconds = averageNanoseconds / 1_000_000.0       // Tempo médio em milissegundos
    val averageSeconds = averageNanoseconds / 1_000_000_000.0        // Tempo médio em segundos

    // Exibir os resultados
    println("\nMédia de tempo de 10 execuções em segundos: %.10f s".format(averageSeconds))
    println("Média de tempo de 10 execuções  em nanosegundos: $averageNanoseconds ns")
    println("Média de tempo de 10 execuções  em milissegundos: $averageMilliseconds ms")
}