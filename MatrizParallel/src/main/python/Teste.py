from concurrent.futures import ThreadPoolExecutor
import time

def multiply_matrices(a, b, start_row, end_row):
    result = []
    for i in range(start_row, end_row):
        result_row = []
        for j in range(len(b[0])):
            result_row.append(sum(a[i][k] * b[k][j] for k in range(len(b))))
        result.append(result_row)
    return result

def measure_execution_time():
    A = [[1, 2], [3, 4], [5, 6]]
    B = [[7, 8], [9, 10]]
    num_runs = 1000  # Defina um número alto para ter uma média precisa
    total_time = 0

    for _ in range(num_runs):
        # Começa a medir o tempo de execução
        start_time = time.perf_counter()

        with ThreadPoolExecutor() as executor:
            futures = []
            num_rows = len(A)
            num_threads = 2
            rows_per_thread = num_rows // num_threads

            # Agora medimos apenas o tempo de cálculo, dentro do loop
            for i in range(num_threads):
                start_row = i * rows_per_thread
                end_row = (i + 1) * rows_per_thread if i != num_threads - 1 else num_rows

                # Medir o tempo de cálculo individualmente
                futures.append(executor.submit(multiply_matrices, A, B, start_row, end_row))

            # Espera todas as tarefas finalizarem e obtém os resultados
            results = [future.result() for future in futures]

        # Finaliza a medição do tempo de execução
        end_time = time.perf_counter()

        # Adiciona o tempo total de cálculo (excluindo alocação de variáveis)
        total_time += (end_time - start_time)

    # Média de tempo de execução
    average_seconds = total_time / num_runs
    average_milliseconds = average_seconds * 1000
    average_nanoseconds = average_seconds * 1_000_000_000

    # Exibindo a média de tempo de execução
    print(f"Média de tempo de execução em segundos: {average_seconds:.10f} s")
    print(f"Média de tempo de execução em milissegundos: {average_milliseconds} ms")
    print(f"Média de tempo de execução em nanossegundos: {average_nanoseconds} s")


if __name__ == "__main__":
    measure_execution_time()