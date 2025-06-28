// Breno Klywer: 27/06/2025
// Versão refatorada para benchmarking
//------------------------------------------------------------------------------------------
#include <iostream>
#include <vector>
#include <string>
#include <chrono>
#include <random>
#include <iomanip>
#include "Functions.h"

// Função para imprimir instruções de uso
void printUsage() {
    std::cerr << "Uso: ./quicksort_cpp <modo> <tamanho> [maxDepth]" << std::endl;
    std::cerr << "  <modo>      : 'sequential' ou 'parallel'" << std::endl;
    std::cerr << "  <tamanho>   : numero de elementos a ordenar" << std::endl;
    std::cerr << "  [maxDepth]  : (opcional, para modo parallel) profundidade maxima da recursao. Padrao: 10" << std::endl;
}

int main(int argc, char* argv[]) {
    // --- Passo 1: Validar e ler os argumentos da linha de comando ---
    if (argc < 3) {
        printUsage();
        return 1; // Retorna erro
    }

    std::string mode = argv[1];
    long long size;
    try {
        size = std::stoll(argv[2]);
    }
    catch (const std::exception& e) {
        std::cerr << "Erro: Tamanho invalido." << std::endl;
        printUsage();
        return 1;
    }

    int maxDepth = 10; // Valor padrão para maxDepth
    if (mode == "parallel" && argc > 3) {
        try {
            maxDepth = std::stoi(argv[3]);
        }
        catch (const std::exception& e) {
            std::cerr << "Erro: maxDepth invalido." << std::endl;
            printUsage();
            return 1;
        }
    }

    // --- Passo 2: Preparar o vetor com dados aleatórios ---
    std::vector<int> data(size);
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<int> dist(0, 100001);

    for (long long i = 0; i < size; i++) {
        data[i] = dist(gen);
    }

    // --- Passo 3: Executar e medir o tempo do algoritmo escolhido ---
    auto start_time = std::chrono::high_resolution_clock::now();

    if (mode == "sequential") {
        quickSort(data, 0, data.size() - 1);
    }
    else if (mode == "parallel") {
        parallelQuickSort(data, 0, data.size() - 1, 0, maxDepth);
    }
    else {
        std::cerr << "Erro: Modo '" << mode << "' desconhecido." << std::endl;
        printUsage();
        return 1;
    }

    auto end_time = std::chrono::high_resolution_clock::now();

    // --- Passo 4: Calcular e imprimir APENAS o tempo em segundos ---
    std::chrono::duration<double> elapsed = end_time - start_time;

    // Imprime o tempo com 8 casas decimais para alta precisão.
    // Esta será a única saída do programa em caso de sucesso.
    std::cout << std::fixed << std::setprecision(8) << elapsed.count() << std::endl;

    return 0; // Sucesso
}