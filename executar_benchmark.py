# Autor: Breno Klywer Olegario de Moura
# Data: 28/06/2025
# Descrição: Script de automação para conduzir benchmarks de desempenho de algoritmos de Quicksort e Multiplicação de Matrizes
# em C++, Kotlin e Python.

import os
import sys
import subprocess
import csv
import statistics
import time

# ==============================================================================
# SEÇÃO 1: CONFIGURAÇÃO DOS EXPERIMENTOS
#
# Esta seção centraliza todos os parâmetros de teste para facilitar a
# modificação e a reprodutibilidade dos experimentos.
# ==============================================================================

# Dicionário principal que define os cenários de teste.
# Cada chave de alto nível representa um algoritmo a ser testado.
TEST_CONFIG = {
    # Configurações para o algoritmo QuickSort.
    "quicksort": {
        # Lista de tamanhos de entrada (número de elementos) a serem testados.
        "sizes": [100_000, 1_000_000, 2_000_000, 5_000_000],

        # Lista de profundidades máximas para a recursão paralela.
        # Útil para analisar o trade-off entre paralelismo e overhead.
        "max_depths_default": [2, 3, 4, 5, 6, 8],

        # Lista de profundidades específica para o Python, que é mais sensível
        # a deadlocks em `ThreadPoolExecutor` com recursão profunda.
        "max_depths_python": [2, 3, 4]
    },

    # Configurações para o algoritmo de Multiplicação de Matrizes.
    "matmul": {
        # Lista de dimensões para as matrizes quadradas (N x N).
        "sizes": [100, 256, 500, 768, 1024, 1473],
    }
}

# Define o número de execuções para o aquecimento (warm-up) da JVM e dos caches.
# Estes resultados são descartados.
NUM_WARMUP_RUNS = 3

# Define o número de execuções de medição para cada cenário.
# Um número ímpar como 7 permite uma análise estatística robusta.
NUM_MEASUREMENT_RUNS = 7

# Nome do arquivo de saída que armazenará os resultados em formato CSV.
RESULTS_CSV_FILE = "resultados_benchmark.csv"


# ==============================================================================
# SEÇÃO 2: CONFIGURAÇÃO DOS COMANDOS DE EXECUÇÃO
#
# Este dicionário mapeia cada linguagem aos comandos necessários para
# compilar (se aplicável) e executar seus respectivos benchmarks.
# O uso de `os.path.join` garante compatibilidade entre sistemas operacionais.
# ==============================================================================

COMMAND_CONFIG = {
    "c++": {
        "dir": "C++",
        # A compilação do C++ é feita manualmente via Visual Studio em modo "Release"
        # para garantir o uso correto do ecossistema MSVC.
        "compile_cmd": None,
        "executable": os.path.join("x64", "Release", "c++quick.exe"),
        "run_cmd": ".\\{executable}"
    },
    "kotlin": {
        "dir": "Kotlin",
        "executable": os.path.join("build", "libs", "benchmark-paralelo-1.0.0.jar"),
        # O Gradle Wrapper garante um ambiente de build consistente.
        "compile_cmd": ".\\gradlew.bat build",
        "run_cmd": "java -jar {executable}"
    },
    "python": {
        "dir": "Python",
        "executable": os.path.join("src", "main.py"),
        "compile_cmd": None, # Python é interpretado.
        "run_cmd": "python {executable}"
    }
}


# ==============================================================================
# SEÇÃO 3: LÓGICA PRINCIPAL DO SCRIPT
#
# As funções abaixo orquestram todo o processo de benchmark, desde
# a compilação até a execução e salvamento dos resultados.
# ==============================================================================

def run_command(command, working_dir=".", timeout=600):
    """
    Executa um comando de terminal em um sub-processo.

    Args:
        command (str): O comando a ser executado.
        working_dir (str): O diretório de trabalho onde o comando será executado.
        timeout (int): Tempo máximo de espera em segundos antes de interromper o processo.

    Returns:
        subprocess.CompletedProcess: O objeto com o resultado do processo.

    Raises:
        subprocess.CalledProcessError: Se o comando retornar um código de erro.
        subprocess.TimeoutExpired: Se o comando exceder o tempo limite.
    """
    try:
        return subprocess.run(command, cwd=working_dir, check=True, capture_output=True, text=True, shell=True, timeout=timeout)
    except subprocess.TimeoutExpired as e:
        print(f"\nERRO: O comando excedeu o tempo limite de {timeout} segundos e foi interrompido.")
        raise e

def save_results(algo, lang, mode, size, max_depth, mean_time, std_dev, all_times):
    """
    Anexa uma linha de resultado ao arquivo CSV.
    """
    try:
        with open(RESULTS_CSV_FILE, "a", newline="", encoding='utf-8') as f:
            writer = csv.writer(f)
            mean_str = f"{mean_time:.8f}" if isinstance(mean_time, float) else mean_time
            std_dev_str = f"{std_dev:.8f}" if isinstance(std_dev, float) else std_dev
            writer.writerow([
                time.strftime("%Y-%m-%d %H:%M:%S"),
                algo, lang, mode, size,
                max_depth if max_depth != -1 else "N/A",
                mean_str, std_dev_str, str(all_times)
            ])
    except IOError as e:
        print(f"Erro ao salvar resultados: {e}")

def run_single_benchmark(algo, lang, mode, size, max_depth):
    """
    Orquestra um único conjunto de testes para uma combinação específica de
    parâmetros, incluindo aquecimento, medições e análise estatística.
    """
    test_name = f"Testando: [{algo.upper()}] - [{lang.upper()}] - [{mode.upper()}] - N={size}"
    if max_depth != -1:
        test_name += f" - maxDepth={max_depth}"
    print(f"\n{test_name}")

    config = COMMAND_CONFIG[lang]
    run_cmd_template = config["run_cmd"]
    executable_path = config["executable"].replace('/', os.sep)
    run_cmd_base = run_cmd_template.format(executable=executable_path)

    # Monta a lista de argumentos corretamente para cada linguagem
    if lang == 'c++':
        args = [mode, str(size)]
    else:
        args = [algo, mode, str(size)]

    if mode == 'parallel' and max_depth != -1:
        args.append(str(max_depth))

    full_command = f"{run_cmd_base} {' '.join(args)}"

    try:
        # Fase de Aquecimento
        print(f"  -> Aquecimento ({NUM_WARMUP_RUNS}x)...", end="", flush=True)
        for _ in range(NUM_WARMUP_RUNS):
            run_command(full_command, working_dir=config["dir"])
        print(" OK.")

        # Fase de Medição
        execution_times = []
        print(f"  -> Medição ({NUM_MEASUREMENT_RUNS}x)...", end="", flush=True)
        for _ in range(NUM_MEASUREMENT_RUNS):
            result = run_command(full_command, working_dir=config["dir"])
            execution_times.append(float(result.stdout.strip()))
        print(" OK.")

        # Análise Estatística
        mean_time = statistics.mean(execution_times)
        std_dev = statistics.stdev(execution_times) if len(execution_times) > 1 else 0.0

        save_results(algo, lang, mode, size, max_depth, mean_time, std_dev, execution_times)

    except (subprocess.CalledProcessError, FileNotFoundError, subprocess.TimeoutExpired) as e:
        error_msg = f"ERRO ({type(e).__name__})"
        print(f"\n{error_msg} durante a execução do benchmark para o comando '{full_command}': {e}")
        if hasattr(e, 'stderr') and e.stderr: print(f"Saida de erro:\n{e.stderr}")
        if hasattr(e, 'stdout') and e.stdout: print(f"Saida padrao:\n{e.stdout}")
        save_results(algo, lang, mode, size, max_depth, "ERRO", "ERRO", str(e))

def main():
    """
    Ponto de entrada principal do script. Orquestra as fases de compilação
    e execução dos benchmarks.
    """
    print("--- INICIANDO SCRIPT DE BENCHMARK AUTOMATIZADO ---")

    # Inicializa o arquivo de resultados com o cabeçalho.
    try:
        with open(RESULTS_CSV_FILE, "w", newline="", encoding='utf-8') as f:
            writer = csv.writer(f)
            writer.writerow([
                "timestamp", "algoritmo", "linguagem", "modo", "tamanho_entrada",
                "max_depth", "tempo_medio_s", "desvio_padrao_s", "medicoes_s"
            ])
    except IOError as e:
        print(f"Erro ao criar arquivo de resultados: {e}")
        sys.exit(1)

    # Fase 1: Compila todos os projetos que necessitam de compilação.
    print("\n--- FASE DE COMPILAÇÃO ---")
    for lang, config in COMMAND_CONFIG.items():
        if config.get("compile_cmd"):
            print(f"Compilando projeto {lang.upper()}...")
            try:
                run_command(config["compile_cmd"], working_dir=config["dir"])
                print(f"  -> {lang.upper()} compilado com sucesso.")
            except (subprocess.CalledProcessError, FileNotFoundError) as e:
                print(f"ERRO ao compilar {lang.upper()}: {e}")
                if hasattr(e, 'stderr') and e.stderr: print(e.stderr)
                sys.exit(1)
        else:
             print(f"AVISO: Pulando compilação para {lang.upper()} (configurado como None).")

    # Fase 2: Executa a matriz de testes definida em TEST_CONFIG.
    print("\n--- FASE DE EXECUÇÃO E MEDIÇÃO ---")
    for algo, algo_config in TEST_CONFIG.items():
        for lang, config in COMMAND_CONFIG.items():
            if algo == "matmul" and lang == "c++":
                print(f"\nAVISO: Pulando testes de 'matmul' para C++ (não implementado).")
                continue

            for size in algo_config["sizes"]:
                # Executa a versão sequencial para cada tamanho.
                run_single_benchmark(algo, lang, "sequential", size, -1)

                # Executa a(s) versão(ões) paralela(s).
                if algo == "quicksort":
                    # Escolhe a lista de profundidades correta para a linguagem atual.
                    if lang == 'python':
                        depths_to_test = algo_config["max_depths_python"]
                    else:
                        depths_to_test = algo_config["max_depths_default"]

                    for depth in depths_to_test:
                        run_single_benchmark(algo, lang, "parallel", size, depth)
                else: # matmul
                    run_single_benchmark(algo, lang, "parallel", size, -1)

    print(f"\n--- SCRIPT CONCLUÍDO ---")
    print(f"Resultados salvos em: {RESULTS_CSV_FILE}")

# Ponto de entrada padrão para a execução do script.
if __name__ == "__main__":
    main()
