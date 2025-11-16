import sys
import time
import random
import numpy as np

from quicksort import quick_sort_sequential, quick_sort_parallel
from matmul import multiply_matrices_sequential, multiply_matrices_parallel

def print_usage():
    print("Uso: python main.py <algoritmo> <modo> <tamanho> [maxDepth]", file=sys.stderr)
    print("  <algoritmo> : 'quicksort' ou 'matmul'", file=sys.stderr)
    print("  <modo>      : 'sequential' ou 'parallel'", file=sys.stderr)
    print("  <tamanho>   : (quicksort) -> numero de elementos", file=sys.stderr)
    print("              : (matmul) -> dimensao da matriz (NxN)", file=sys.stderr)
    print("  [maxDepth]  : (opcional, para quicksort parallel) profundidade. Padrao: 10", file=sys.stderr)

if __name__ == "__main__":
    if len(sys.argv) < 4:
        print_usage()
        sys.exit(1)

    algorithm = sys.argv[1].lower()
    mode = sys.argv[2].lower()
    try:
        size = int(sys.argv[3])
    except ValueError:
        print("Erro: Tamanho deve ser um número inteiro.", file=sys.stderr)
        sys.exit(1)

    if algorithm == "quicksort":
        data = [random.randint(0, 100001) for _ in range(size)]
    elif algorithm == "matmul":
        A = np.random.rand(size, size)
        B = np.random.rand(size, size)
    else:
        print(f"Erro: Algoritmo '{algorithm}' desconhecido.", file=sys.stderr)
        print_usage()
        sys.exit(1)


    start_time = time.perf_counter()

    if algorithm == "quicksort":
        if mode == "sequential":
            quick_sort_sequential(data)
        elif mode == "parallel":
            max_depth = int(sys.argv[4]) if len(sys.argv) > 4 else 10
            quick_sort_parallel(data, max_depth)
        else:
            print(f"Erro: Modo '{mode}' desconhecido para 'quicksort'.", file=sys.stderr)
            sys.exit(1)

    elif algorithm == "matmul":
        if mode == "sequential":
            multiply_matrices_sequential(A, B)
        elif mode == "parallel":
            multiply_matrices_parallel(A, B)
        else:
            print(f"Erro: Modo '{mode}' desconhecido para 'matmul'.", file=sys.stderr)
            sys.exit(1)

    end_time = time.perf_counter()
    elapsed_time = end_time - start_time

    # Imprime APENAS o tempo em segundos com 8 casas decimais
    print(f"{elapsed_time:.8f}")