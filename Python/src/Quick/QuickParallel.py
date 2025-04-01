import random
import time
import concurrent.futures
from multiprocessing import cpu_count

# Função para realizar o QuickSort sequencial
def quick_sort(arr, low=0, high=None):
    if high is None:
        high = len(arr) - 1

    if low < high:
        pivot_index = random_partition(arr, low, high)
        quick_sort(arr, low, pivot_index - 1)
        quick_sort(arr, pivot_index + 1, high)

# Função para realizar a partição do array
def random_partition(arr, low, high):
    pivot_index = random.randint(low, high)
    arr[pivot_index], arr[high] = arr[high], arr[pivot_index]
    return partition(arr, low, high)

# Função que efetua a troca dos elementos do array
def partition(arr, low, high):
    pivot = arr[high]
    i = low - 1
    for j in range(low, high):
        if arr[j] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]
    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    return i + 1

# Função para realizar o QuickSort paralelo usando concurrent.futures
def parallel_quick_sort(arr, low=0, high=None, depth=0, max_depth=4):
    if high is None:
        high = len(arr) - 1

    if low < high:
        pivot_index = partition(arr, low, high)

        if depth < max_depth:
            # Usando ThreadPoolExecutor ou ProcessPoolExecutor para evitar o problema com processos daemon
            with concurrent.futures.ProcessPoolExecutor(max_workers=cpu_count()) as executor:
                futures = [
                    executor.submit(parallel_quick_sort, arr, low, pivot_index - 1, depth + 1, max_depth),
                    executor.submit(parallel_quick_sort, arr, pivot_index + 1, high, depth + 1, max_depth)
                ]
                concurrent.futures.wait(futures)  # Aguarda as tarefas completarem
        else:
            quick_sort(arr, low, pivot_index - 1)
            quick_sort(arr, pivot_index + 1, high)

# Função para converter tempo para diferentes unidades
def format_time(seconds):
    milliseconds = seconds * 1000
    nanoseconds = seconds * 1e9
    return f"{seconds:.6f} s | {milliseconds:.3f} ms | {nanoseconds:.0f} ns"

# Função principal onde o código será executado
if __name__ == '__main__':
    size = 2000000  # Tamanho do array de teste
    arr_sequential = [random.randint(0, 2000001) for _ in range(size)]
    arr_parallel = arr_sequential.copy()

    print(f"Tamanho do array: {size}")
    print("Iniciando ordenação...")

    # Teste QuickSort Sequencial
    start_time = time.perf_counter()
    for i in range(10):
        quick_sort(arr_sequential)
    sequential_time = (time.perf_counter() - start_time) / 10

    # Teste QuickSort Paralelo
    start_time = time.perf_counter()
    for i in range(10):
        parallel_quick_sort(arr_parallel)
    parallel_time = (time.perf_counter() - start_time) / 10

    # Exibir resultados
    print("\n--- Resultados ---")
    print(f"Tempo médio de 10 execuções do QuickSort Sequencial: {format_time(sequential_time)}")
    print(f"Tempo médio de 10 execuções do QuickSort Paralelo: {format_time(parallel_time)}")