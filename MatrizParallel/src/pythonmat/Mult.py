import time
import numpy as np

# Nesse exemplo, a multiplicação de matrizes é feita sem paralelismo!

def load_matrix_from_csv(filename):
    try:
        matrix = np.loadtxt(filename, delimiter=',')
        return matrix
    except Exception as e:
        print(f"Erro ao carregar o arquivo {filename}: {e}")
        return None

def multiply_matrices(a, b):
    result = []
    for i in range(len(a)):
        result_row = []
        for j in range(len(b[0])):
            result_row.append(sum(a[i][k] * b[k][j] for k in range(len(b))))
        result.append(result_row)
    return result

def measure_execution_time():
    # Carrega as matrizes dos arquivos CSV
    a = load_matrix_from_csv('src/matrizes/matriz3.csv')
    b = load_matrix_from_csv('src/matrizes/matriz4.csv')

    # Verificar se as matrizes foram carregadas corretamente
    if a is not None and b is not None:
        print(f"Tamanho da matriz a: {a.shape}")
        print(f"Tamanho da matriz b: {b.shape}")
    else:
        print("Erro ao carregar as matrizes. Verifique os arquivos CSV.")
        return

    num_runs = 1
    total_time = 0

    for _ in range(num_runs):
        start_time = time.perf_counter()

        # Multiplica as matrizes sem paralelismo
        final_result = multiply_matrices(a, b)

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
    #print(final_result[0])  # Exibe a primeira linha do resultado da multiplicação

if __name__ == "__main__":
    measure_execution_time()