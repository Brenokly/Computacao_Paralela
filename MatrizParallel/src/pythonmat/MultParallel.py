import time
from multiprocessing import Pool, cpu_count
import numpy as np

# Esse exemplo possuí paralelismo!

# Aqui está o cerne do paralelismo:
# Pool.map() distribui as tarefas de forma que cada processo possa executar a função parallel_multiply simultaneamente.
# parallel_multiply recebe uma tupla com a, b, start_row, e end_row, e chama multiply_matrices para calcular apenas uma
# parte da matriz resultante.
# Cada processo calcula a multiplicação parcial de forma independente dos demais.


# Função para carregar uma matriz de um CSV
def load_matrix_from_csv(filename):
    try:
        matrix = np.loadtxt(filename, delimiter=',')
        return matrix
    except Exception as e:
        print(f"Erro ao carregar o arquivo {filename}: {e}")
        return None

# Função que multiplica duas matrizes de forma parcial
def multiply_matrices(a, b, start_row, end_row):
    result = []
    for i in range(start_row, end_row):
        result_row = []
        for j in range(len(b[0])):
            result_row.append(sum(a[i][k] * b[k][j] for k in range(len(b))))
        result.append(result_row)
    return result

# Função que desembrulha os parâmetros
def parallel_multiply(a_b_tuple):
    a, b, start_row, end_row = a_b_tuple
    return multiply_matrices(a, b, start_row, end_row)

# Main()
def measure_execution_time():
    # Carrega as matrizes dos arquivos CSV
    a = load_matrix_from_csv('src/matrizes/matriz1.csv')
    b = load_matrix_from_csv('src/matrizes/matriz2.csv')

    # Verificar se as matrizes foram carregadas corretamente
    if a is not None and b is not None:
        print(f"Tamanho da matriz a: {a.shape}")
        print(f"Tamanho da matriz b: {b.shape}")
    else:
        print("Erro ao carregar as matrizes. Verifique os arquivos CSV.")
        return

    num_runs = 1                                    # Número de vezes que o código será executado
    total_time = 0                                  # Tempo total de execução
    num_processes = cpu_count()                     # Número de processos a serem usados
    num_rows = len(a)                               # Número de linhas da matriz a
    final_result = []                               # Resultado da multiplicação
    rows_per_process = num_rows // num_processes    # Número de linhas por processos

    print("Número de processos: ", num_processes)

    for _ in range(num_runs):
        tasks = []

        # Cria as tuplas com as tarefas de cada processo
        for i in range(num_processes):
            start_row = i * rows_per_process
            end_row = (i + 1) * rows_per_process if i != num_processes - 1 else num_rows
            tasks.append((a, b, start_row, end_row))

        # Inicia a contagem
        start_time = time.perf_counter()

        # Executa os processos com o Pool
        with Pool(processes=num_processes) as pool:
            results = pool.map(parallel_multiply, tasks)

        # Combina os resultados usando np.vstack() para empilhar as linhas
        final_result = np.vstack(results)  # Combina as partes das matrizes multiplicadas

        # finaliza a contagem
        end_time = time.perf_counter()
        total_time += (end_time - start_time)

    # Média de tempo de execução
    average_seconds      = total_time / num_runs
    average_milliseconds = average_seconds * 1000
    average_nanoseconds  = average_seconds * 1_000_000_000

    print(f"\nMédia de tempo de execução em segundos: {average_seconds:.10f} s")
    print(f"Média de tempo de execução em milissegundos: {average_milliseconds} ms")
    print(f"Média de tempo de execução em nanossegundos: {average_nanoseconds} ns")

    # Formatação dos resultados
    print("\nPrimeira linha do resultado da multiplicação:")
    #print(final_result)  # Exibe a primeira linha formatada

if __name__ == "__main__":
    measure_execution_time()