// Breno Klywer: 28/06/2025
//------------------------------------------------------------------------------------------
#pragma once
#include <vector>

// Particiona o vetor e retorna o índice do pivô.
int partition(std::vector<int>& arr, int low, int high);

// Particiona usando um pivô aleatório.
int randomPartition(std::vector<int>& arr, int low, int high);

// Função de topo do QuickSort Sequencial
// Os valores padrão permitem chamar quickSort(vetor)
void quickSort(std::vector<int>& arr, int low = 0, int high = -1);

// Função de topo do QuickSort Paralelo
// Os valores padrão foram restaurados para consistência
void parallelQuickSort(std::vector<int>& arr, int low = 0, int high = -1, int depth = 0, int maxDepth = 10);
//------------------------------------------------------------------------------------------