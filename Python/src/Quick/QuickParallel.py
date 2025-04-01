import random
import time
import concurrent.futures
from multiprocessing import cpu_count, RawArray

# Função para realizar a partição do array
def partition(arr, low, high):
    pivot = arr[high]
    i = low - 1
    for j in range(low, high):
        if arr[j] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]
    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    return i + 1

# QuickSort sequencial
def quick_sort(arr, low=0, high=None):
    if high is None:
        high = len(arr) - 1
    if low < high:
        pivot_index = partition(arr, low, high)
        quick_sort(arr, low, pivot_index - 1)
        quick_sort(arr, pivot_index + 1, high)

# Função auxiliar para criar um array compartilhado
def to_shared_array(lst):
    return RawArray('i', lst)

# QuickSort paralelo usando threads
def parallel_quick_sort(shared_arr, low=0, high=None, depth=0, max_depth=4):
    if high is None:
        high = len(shared_arr) - 1

    if low < high:
        pivot_index = partition(shared_arr, low, high)

        if depth < max_depth:
            with concurrent.futures.ThreadPoolExecutor(max_workers=cpu_count()) as executor:
                left_future = executor.submit(parallel_quick_sort, shared_arr, low, pivot_index - 1, depth + 1, max_depth)
                right_future = executor.submit(parallel_quick_sort, shared_arr, pivot_index + 1, high, depth + 1, max_depth)
                concurrent.futures.wait([left_future, right_future])
        else:
            quick_sort(shared_arr, low, pivot_index - 1)
            quick_sort(shared_arr, pivot_index + 1, high)

# Teste do QuickSort Paralelo e Sequencial
if __name__ == '__main__':
    size = 1000000  # Tamanho do array de teste
    original_array = [random.randint(0, 1000001) for _ in range(size)]
    
    # Criamos um array compartilhado entre threads
    shared_array = to_shared_array(original_array)
    arr_sequential = original_array[:]  # Cópia para execução sequencial

    print(f"Tamanho do array: {size}")
    print("Iniciando ordenação...")

    # Teste QuickSort Sequencial
    start_time = time.perf_counter()
    quick_sort(arr_sequential)
    sequential_time = time.perf_counter() - start_time

    # Teste QuickSort Paralelo com Threads
    start_time = time.perf_counter()
    parallel_quick_sort(shared_array)
    parallel_time = time.perf_counter() - start_time

    # Converter o array compartilhado de volta para lista normal
    arr_parallel = list(shared_array)

    # Exibir resultados
    print("\n--- Resultados ---")
    print(f"Tempo do QuickSort Sequencial: {sequential_time:.6f} s")
    print(f"Tempo do QuickSort Paralelo: {parallel_time:.6f} s")
    print(f"Speedup: {sequential_time / parallel_time:.2f}x")

    # Verifica se o resultado é correto
    if arr_sequential == arr_parallel:
        print("Os resultados são CORRETOS.")
    else:
        print("ERRO: Os resultados são diferentes!")

    print("Ordenação concluída.")