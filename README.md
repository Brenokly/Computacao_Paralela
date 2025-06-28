🚀 Implementação e Avaliação de Programas Paralelos: C++, Kotlin e Python
Curso: Ciência da Computação 💻
Universidade: Ufersa - Universidade Federal Rural do Semi-Árido 🌱
Status do Projeto: Andamento - 2025
📄 Resumo do Projeto

Este repositório apresenta uma análise comparativa de desempenho da paralelização de algoritmos clássicos — QuickSort e Multiplicação de Matrizes — utilizando C++, Kotlin e Python. O objetivo foi quantificar os trade-offs de desempenho, escalabilidade e complexidade de implementação em arquiteturas multicore modernas.

Os resultados, obtidos através de uma metodologia de benchmark rigorosa, demonstram que C++ e Kotlin oferecem desempenho e escalabilidade significativamente superiores para tarefas intensivas em CPU, enquanto as bibliotecas padrão de Python se mostraram inadequadas para este tipo de paralelismo de alta performance.
🏆 Principais Contribuições e Descobertas

Este estudo resultou em três contribuições principais para o entendimento prático do paralelismo em diferentes ecossistemas:

    Identificação do Ponto de Inflexão em C++: Demonstramos quantitativamente o "ponto ideal" (maxDepth ótimo) no QuickSort paralelo, onde o overhead de criação de threads com std::async supera os ganhos do paralelismo, resultando em uma degradação de performance.

    Demonstração do Risco de Deadlock em Python: Comprovamos empiricamente que o uso de concurrent.futures.ThreadPoolExecutor em algoritmos recursivos, como o QuickSort, leva a um deadlock à medida que a profundidade da recursão aumenta, travando a aplicação.

    Análise Comparativa de Gargalos:

        Python (GIL): O Global Interpreter Lock torna o paralelismo com threads ineficaz para tarefas de CPU.

        Python (IPC): O custo de serialização de dados (IPC) na biblioteca multiprocessing anula completamente os ganhos do paralelismo em tarefas com comunicação frequente, como a multiplicação de matrizes.

        Kotlin (Corrotinas): O modelo de corrotinas sobre a JVM, com seu scheduler inteligente (work-stealing), mostrou-se extremamente robusto, produtivo e escalável.

🛠️ Algoritmos e Abordagens Técnicas

Foram implementados dois algoritmos para testar diferentes cenários de paralelismo.
1. QuickSort Paralelo

Um algoritmo de "dividir para conquistar", onde a paralelização intensiva de pequenas tarefas testa a eficiência do gerenciador de threads e o custo de overhead.

    C++: std::async com a política std::launch::async para garantir a criação de threads nativas.

    Kotlin: Corrotinas com async e await sobre o Dispatchers.Default, um pool de threads otimizado para CPU.

    Python: concurrent.futures.ThreadPoolExecutor para avaliar o overhead e os limites do paralelismo baseado em threads sob o GIL.

// Exemplo da implementação em Kotlin, mostrando a criação de tarefas assíncronas
suspend fun parallelQuickSort(arr: IntArray, ..., depth: Int, maxDepth: Int) {
    if (low < high) {
        val pivotIndex = partition(arr, low, high)
        if (depth < maxDepth) {
            coroutineScope {
                val left = async(Dispatchers.Default) { parallelQuickSort(arr, low, pivotIndex - 1, depth + 1, maxDepth) }
                val right = async(Dispatchers.Default) { parallelQuickSort(arr, pivotIndex + 1, high, depth + 1, maxDepth) }
                left.await()
                right.await()
            }
        } else {
            // Recorre à ordenação sequencial para evitar overhead excessivo
            sequentialQuickSort(arr, low, pivotIndex - 1)
            sequentialQuickSort(arr, pivotIndex + 1, high)
        }
    }
}

2. Multiplicação de Matrizes

Uma tarefa "embaraçosamente paralela", ideal para avaliar a escalabilidade e o paralelismo bruto, contornando o GIL em Python.

    Kotlin: O cálculo de cada linha da matriz resultante foi distribuído como uma corrotina em um pool de threads.

    Python: A biblioteca multiprocessing com Pool de processos foi utilizada para permitir a execução paralela real, contornando o GIL.

🔬 Metodologia de Benchmark

Para garantir a validade estatística e a justiça da comparação, foi adotada uma metodologia rigorosa:

    Ambiente de Teste:

        CPU: AMD Ryzen 5 5600G (6 Núcleos, 12 Threads) @ 3.9GHz

        RAM: 16 GB DDR4

        SO: Windows 11 Pro 24H2

        Versões: C++17 (MSVC v19.38), Kotlin 2.0.0 (JVM 23), Python 3.13.3

    Processo de Coleta:

        Aquecimento (Warm-up): Foram realizadas 3 execuções iniciais cujos resultados foram descartados para mitigar efeitos de cold caches e compilação JIT (na JVM).

        Medições: O tempo de execução de 7 medições independentes foi registrado para cada cenário.

        Análise: Os resultados apresentados no artigo correspondem à média e ao desvio padrão das 7 medições.

📈 Conclusões Detalhadas

    C++: Desempenho Bruto com Responsabilidade: Oferece a performance máxima, mas exige sintonia fina do programador para gerenciar o overhead e evitar a degradação de desempenho. É a escolha ideal quando cada milissegundo conta.

    Kotlin: Robustez e Produtividade: Foi o grande destaque. Seu modelo de corrotinas se mostrou resiliente, produtivo e altamente escalável, provando ser uma alternativa moderna e poderosa para computação de alto desempenho, abstraindo grande parte da complexidade do gerenciamento de threads.

    Python: A Ferramenta Certa para a Tarefa Certa: As bibliotecas padrão (threading, multiprocessing) não são adequadas para paralelismo de CPU de alta performance devido a gargalos arquitetônicos (GIL e custo de IPC). O poder do Python para computação numérica reside em bibliotecas como NumPy e Numba, que delegam o trabalho pesado para código C/Fortran otimizado.

📑 Referências do Artigo

O trabalho completo se baseia e cita os seguintes artigos:

    Buluç, A., & Gilbert, J. R. (2008). Challenges and Advances in Parallel Sparse Matrix-Matrix Multiplication.

    Cheng, D. R., et al. (2007). A Novel Parallel Sorting Algorithm for Contemporary Architectures.

    Hendrickson, B., et al. (1995). An Efficient Parallel Algorithm for Matrix-Vector Multiplication.

    Sanders, P., & Hansch, T. (1997). On the Efficient Implementation of Massively Parallel Quicksort.

    Schatz, M. D., et al. (2016). Parallel Matrix Multiplication: A Systematic Journey.

    Tsigas, P., & Zhang, Y. (2001). A Simple, Fast Parallel Implementation of Quicksort and its Performance Evaluation.

👨‍🎓 Informações do Projeto

    Orientador: Prof. Paulo Henrique Lopes Silva

    Discente: Breno Klywer Olegario de Moura
