// Breno Klywer: 31/03/2025
//------------------------------------------------------------------------------------------
#include <vector>
#pragma once
//------------------------------------------------------------------------------------------
// Nesse arquivo .h est�o todos os prot�tipos das fun��es que v�o ser usadas nesse exemplo!

void printResult(long long elapsedTime);

int randomPartition(std::vector<int> & arr, int low = 0, int high = -1);

int partition(std::vector<int> & arr, int low = 0, int high = -1);

void quickSort(std::vector<int> & arr, int low = 0, int high = -1);

void parallelQuickSort(std::vector<int> & arr, int low = 0, int high = -1, int depth = 0, int maxDepth = 10);

//------------------------------------------------------------------------------------------