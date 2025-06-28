package com.breno.benchmark

import kotlinx.coroutines.runBlocking
import java.util.Locale
import kotlin.random.Random
import kotlin.system.measureNanoTime

fun printUsage() {
    System.err.println("Uso: <programa> <algoritmo> <modo> <tamanho> [args...]")
    System.err.println("  Algoritmos : 'quicksort', 'matmul'")
    System.err.println("  Modo       : 'sequential' ou 'parallel'")
    System.err.println("  Tamanho    : (quicksort) -> numero de elementos")
    System.err.println("             : (matmul) -> dimensao da matriz (NxN)")
    System.err.println("  [maxDepth] : (opcional, para quicksort parallel) profundidade. Padrao: 10")
}

fun main(args: Array<String>) {
    if (args.size < 3) {
        printUsage()
        return
    }

    val algorithm = args[0]
    val mode = args[1]
    val size = args[2].toIntOrNull()

    if (size == null) {
        System.err.println("Erro: Tamanho invalido.")
        printUsage()
        return
    }

    // Mede o tempo de execução da tarefa escolhida
    val nanoTime = measureNanoTime {
        when (algorithm.lowercase()) {
            "quicksort" -> {
                // Prepara e executa o benchmark do QuickSort
                val data = IntArray(size) { Random.nextInt(0, 100_001) }
                if (mode == "sequential") {
                    quickSortSequential(data)
                } else {
                    val maxDepth = if (args.size > 3) args[3].toInt() else 10
                    runBlocking { quickSortParallel(data, depth = 0, maxDepth = maxDepth) }
                }
            }

            "matmul" -> {
                // Prepara e executa o benchmark da Multiplicação de Matrizes
                val A = Array(size) { DoubleArray(size) { Random.nextDouble(0.0, 100.0) } }
                val B = Array(size) { DoubleArray(size) { Random.nextDouble(0.0, 100.0) } }
                if (mode == "sequential") {
                    multiplyMatricesSequential(A, B)
                } else {
                    runBlocking { multiplyMatricesParallel(A, B) }
                }
            }

            else -> {
                System.err.println("Erro: Algoritmo '${algorithm}' desconhecido.")
                printUsage()
                return@measureNanoTime // Sai do bloco de medição
            }
        }
    }

    // Imprime APENAS o tempo em segundos, com 8 casas decimais
    val seconds = nanoTime / 1_000_000_000.0
    println("%.8f".format(Locale.US, seconds))
}