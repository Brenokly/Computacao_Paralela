import numpy as np
from multiprocessing import Pool, cpu_count

# --- Funções Públicas ---

def multiply_matrices_sequential(A, B):
    """ Multiplicação de Matrizes Sequencial usando NumPy. """
    return np.dot(A, B)

def multiply_matrices_parallel(A, B):
    """ Multiplicação de Matrizes Paralela usando multiprocessing. """
    num_procs = cpu_count()
    # Divide a matriz A em blocos de linhas para cada processo
    chunks = np.array_split(A, num_procs)

    # Prepara os argumentos para a função map do pool
    tasks = [(chunk, B) for chunk in chunks]

    with Pool(processes=num_procs) as pool:
        results = pool.map(_multiply_worker, tasks)

    return np.vstack(results)

# --- Função de Trabalho (privada ao módulo) ---
# Definida no topo para ser "picklable" pelo multiprocessing no Windows
def _multiply_worker(args):
    """ Função de trabalho que cada processo executará. """
    rows_A, B = args
    return np.dot(rows_A, B)