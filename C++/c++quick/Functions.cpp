// Breno Klywer: 31/03/2025
//------------------------------------------------------------------------------------------
#include "Functions.h"
#include <future>
#include <random>
using namespace std;
//------------------------------------------------------------------------------------------
// Nesse arquivo .cpp está a implementação das funções do arquivo Functions.h

// Random number generator
random_device RandomDevice;
mt19937 RandomEngine(RandomDevice());

int randomPartition(vector<int>& arr, int low, int high) {
    uniform_int_distribution<int> distribution(low, high);
    int pivotIndex = distribution(RandomEngine);
    swap(arr[pivotIndex], arr[high]);
    return partition(arr, low, high);
}

int partition(vector<int>& arr, int low, int high) {
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

void quickSort(vector<int>& arr, int low, int high) {
    if (low < high) {
        int pivotIndex = randomPartition(arr, low, high);
        quickSort(arr, low, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, high);
    }
}

void parallelQuickSort(vector<int>& arr, int low, int high, int depth, int maxDepth) {
    if (low < high) {
        int pivotIndex = randomPartition(arr, low, high);

        if (depth < maxDepth) {
            auto left = async(launch::async, parallelQuickSort, ref(arr), low, pivotIndex - 1, depth + 1, maxDepth);
            auto right = async(launch::async, parallelQuickSort, ref(arr), pivotIndex + 1, high, depth + 1, maxDepth);
            left.wait();
            right.wait();
        }
        else {
            // Quando a profundidade máxima é atingida, continua com o quicksort sequencial
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }
}

//------------------------------------------------------------------------------------------