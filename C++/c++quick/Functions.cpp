// Breno Klywer: 31/03/2025
//------------------------------------------------------------------------------------------
#include "Functions.h"
#include <iostream>
#include <future>
#include <random>
#include <iomanip>
using namespace std;
//------------------------------------------------------------------------------------------
// Nesse arquivo .cpp est� a implementa��o das fun��es do arquivo Functions.h

// Random number generator
random_device RandomDevice;
mt19937 RandomEngine(RandomDevice());

void printResult(long long elapsedTime) {
    // Calcular a m�dia
    long long averageNanoseconds = elapsedTime;
    double averageMilliseconds = averageNanoseconds / 1e6;
    double averageSeconds = averageNanoseconds / 1e9;

    // Limitar o n�mero de casas decimais
    cout << fixed << setprecision(10);

    // Exibir os resultados
    cout << "\nM�dia de tempo de execu��o em segundos: " << averageSeconds << " s" << endl;
    cout << "M�dia de tempo de execu��o em nanosegundos: " << averageNanoseconds << " ns" << endl;
    cout << "M�dia de tempo de execu��o em milissegundos: " << averageMilliseconds << " ms" << endl;
}

int randomPartition(vector<int> & arr, int low, int high) {
	if (high == -1) high = (int) arr.size() - 1; // Se o valor de high n�o for passado, assume o �ltimo elemento do vetor

    uniform_int_distribution<int> distribution(low, high);
    int pivotIndex = distribution(RandomEngine);

    // Troca o piv� aleat�rio com o �ltimo elemento
	swap(arr[pivotIndex], arr[high]);
    return partition(arr, low, high);
}

int partition(vector<int> & arr, int low, int high) {
	if (high == -1) high = (int) arr.size() - 1; // Se o valor de high n�o for passado, assume o �ltimo elemento do vetor

    int pivot = arr[high];
    int i = low - 1;
    for (int j = low; j < high; j++) {
        if (arr[j] <= pivot) {
            i++;
			swap(arr[i], arr[j]);
        }
    }
    swap(arr[i + 1], arr[high]);
    return i + 1;
}

void quickSort(vector<int> & arr, int low, int high) {
	if (high == -1) high = (int) arr.size() - 1; // Se o valor de high n�o for passado, assume o �ltimo elemento do vetor

    if (low < high) {
        int pivotIndex = randomPartition(arr, low, high);
        quickSort(arr, low, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, high);
    }
}

void parallelQuickSort(vector<int>& arr, int low, int high, int depth, int maxDepth) {
    if (high == -1) high = (int) arr.size() - 1;

    if (low < high) {
        int pivotIndex = randomPartition(arr, low, high);

        if (depth < maxDepth) {
            // Usa async para criar threads gerenciadas pelo runtime, como corrotinas
            auto left = async(launch::async, parallelQuickSort, ref(arr), low, pivotIndex - 1, depth + 1, maxDepth);
            auto right = async(launch::async, parallelQuickSort, ref(arr), pivotIndex + 1, high, depth + 1, maxDepth);

            left.wait();
            right.wait();
        }
        else {
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }
}

//------------------------------------------------------------------------------------------