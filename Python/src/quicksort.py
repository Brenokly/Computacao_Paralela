import random
from concurrent.futures import ThreadPoolExecutor

# --- Funções Internas (privadas ao módulo) ---

def _partition(arr, low, high):
    """ Função de partição do QuickSort. """
    pivot = arr[high]
    i = low - 1
    for j in range(low, high):
        if arr[j] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]
    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    return i + 1

def _quick_sort_sequential_internal(arr, low, high):
    """ Helper recursivo para o QuickSort sequencial. """
    if low < high:
        pivot_index = _partition(arr, low, high)
        _quick_sort_sequential_internal(arr, low, pivot_index - 1)
        _quick_sort_sequential_internal(arr, pivot_index + 1, high)

def _quick_sort_parallel_internal(arr, executor, depth, max_depth):
    """ Helper recursivo para o QuickSort paralelo com threads. """
    if len(arr) <= 1:
        return arr

    # Condição de parada da paralelização
    if depth >= max_depth or len(arr) < 2000: # Limiar de tamanho para evitar overhead
        return quick_sort_sequential(arr)

    pivot = arr[len(arr) // 2]
    left = [x for x in arr if x < pivot]
    middle = [x for x in arr if x == pivot]
    right = [x for x in arr if x > pivot]

    future_left = executor.submit(_quick_sort_parallel_internal, left, executor, depth + 1, max_depth)
    future_right = executor.submit(_quick_sort_parallel_internal, right, executor, depth + 1, max_depth)

    return future_left.result() + middle + future_right.result()


# --- Funções Públicas (que serão importadas pelo main.py) ---

def quick_sort_sequential(arr):
    """ Função de topo para o QuickSort Sequencial. """
    # Cria uma cópia para não modificar o original, se necessário
    arr_copy = list(arr)
    _quick_sort_sequential_internal(arr_copy, 0, len(arr_copy) - 1)
    return arr_copy

def quick_sort_parallel(arr, max_depth):
    """ Função de topo para o QuickSort Paralelo. """
    with ThreadPoolExecutor() as executor:
        return _quick_sort_parallel_internal(list(arr), executor, 0, max_depth)