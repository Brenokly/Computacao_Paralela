// Breno Klywer: 31/03/2025
//------------------------------------------------------------------------------------------
#include <iostream>
#include <chrono>
#include <random>
#include "Functions.h"
using namespace std;
//------------------------------------------------------------------------------------------
// Arquivo main, contendo o uso das funções implementadas no arquivo Functions.cpp

std::random_device rd;
std::mt19937 gen(rd());
std::uniform_int_distribution<int> dist(0, 100001);

int main() {
    system("chcp 1252 > nul");

    long size = 100000; 
	vector<int> arraySequential(size);
	vector<int> arrayParallel(size);
	// Inicializando o vetor com valores aleatórios
	for (int i = 0; i < size; i++) {
        arraySequential[i] = dist(gen);
	}
    
	// Copiando o vetor sequencial para o vetor paralelo
	copy(arraySequential.begin(), arraySequential.end(), arrayParallel.begin());

    cout << "Tamanho do array: " << size << endl;
    cout << "Iniciando ordenação..." << endl;
        
    auto inicio1 = chrono::high_resolution_clock::now();
    for (int i = 0; i < 10; i++) {
        quickSort(arraySequential);
    }
    auto fim1 = chrono::high_resolution_clock::now();
    auto sequentialTime = chrono::duration_cast<chrono::nanoseconds>(fim1 - inicio1).count() / 10LL;

    auto inicio2 = chrono::high_resolution_clock::now();
    for (int i = 0; i < 10; i++) {
        parallelQuickSort(arrayParallel);
    }
    auto fim2 = chrono::high_resolution_clock::now();
    auto parallelTime = chrono::duration_cast<chrono::nanoseconds>(fim2 - inicio2).count() / 10LL;

    cout << "\n--- Resultados ---" << endl;
    cout << "QuickSort Sequencial:" << endl;
    printResult(sequentialTime);
    cout << "\nQuickSort Paralelo:" << endl;
    printResult(parallelTime);

    return 0;
}