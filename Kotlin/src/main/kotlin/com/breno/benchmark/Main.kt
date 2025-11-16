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
    val size = args[2].toIntOrNull()!! // Assumindo que não é nulo após a verificação

    // Mede o tempo de execução da tarefa escolhida
    val nanoTime = when (algorithm.lowercase()) {
        "quicksort" -> {
            // Prepara os dados SÓ para o quicksort
            val data = IntArray(size) { Random.nextInt(0, 100_001) }

            // Mede APENAS a execução
            measureNanoTime {
                if (mode == "sequential") {
                    quickSortSequential(data)
                } else {
                    val maxDepth = if (args.size > 3) args[3].toInt() else 10
                    runBlocking { quickSortParallel(data, depth = 0, maxDepth = maxDepth) }
                }
            }
        }

        "matmul" -> {
            // Prepara os dados SÓ para matmul
            val A = Array(size) { DoubleArray(size) { Random.nextDouble(0.0, 100.0) } }
            val B = Array(size) { DoubleArray(size) { Random.nextDouble(0.0, 100.0) } }

            // Mede APENAS a execução
            measureNanoTime {
                if (mode == "sequential") {
                    multiplyMatricesSequential(A, B)
                } else {
                    runBlocking { multiplyMatricesParallel(A, B) }
                }
            }
        }

        else -> {
            System.err.println("Erro: Algoritmo '${algorithm}' desconhecido.")
            printUsage()
            0L // Retorna 0 para nanoTime, pois não houve medição
        }
    }

    if (nanoTime > 0) {
        val seconds = nanoTime / 1_000_000_000.0
        println("%.8f".format(Locale.US, seconds))
    }
}