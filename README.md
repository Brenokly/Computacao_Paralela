# 🐍 Implementação e Avaliação de Programas Paralelos usando Python, Kotlin e C++ 🚀

### Curso: Ciência da Computação 💻
### Universidade: Ufersa - Universidade Federal Rural do Semi-Árido 🌱
### Ano: 2024 📅

## 📚 Introdução

A **programação paralela** é essencial para maximizar o desempenho de aplicações modernas, explorando ao máximo os recursos de hardware, especialmente em processadores multicore. Este repositório contém os resultados de uma pesquisa sobre como implementar e avaliar programas paralelos nas linguagens **Python, Kotlin e C++**.

## 💡 Justificativa

Apesar de Python ser amplamente utilizado devido à sua simplicidade, o **Global Interpreter Lock (GIL)** pode limitar a execução paralela de threads. Para contornar essa limitação, utilizamos as bibliotecas `multiprocessing` e `concurrent.futures`.

Kotlin, por sua vez, oferece um modelo eficiente de concorrência por meio de **coroutines**, permitindo uma execução assíncrona de forma intuitiva e performática.

Já em **C++**, utilizamos `std::async` e `std::future` para implementar o paralelismo, tirando proveito da execução concorrente de tarefas.

Este estudo visa comparar **desempenho, facilidade de uso e manutenção do código** entre essas três abordagens.

## 🎯 Objetivos

- **Geral**: Implementar e avaliar programas paralelos utilizando Python, Kotlin e C++.
- **Específicos**:
  - Desenvolver programas paralelos para resolver problemas computacionais clássicos.
  - Analisar a sintaxe e os recursos de cada linguagem para programação paralela.
  - Avaliar o desempenho das implementações.
  - Documentar as diferenças na facilidade de implementação e manutenção.

## 🔍 Metodologia

1. **Revisão Bibliográfica**: Estudo sobre programação paralela e bibliotecas relevantes em Python, Kotlin e C++.
2. **Configuração do Ambiente**: Instalação e configuração das IDEs e bibliotecas necessárias.
3. **Implementação**: Desenvolvimento de soluções paralelas para problemas computacionais clássicos.
4. **Testes e Avaliação de Desempenho**: Medidas de tempo de execução e consumo de recursos.
5. **Análise Comparativa**: Comparar implementações e documentar os resultados.

## 🛠️ Algoritmos Implementados

Foram testados dois algoritmos computacionais com **threads**:

1. **Multiplicação de Matrizes**
2. **Quicksort Paralelo**

As bibliotecas utilizadas foram:

- **Kotlin**: `coroutines`
- **C++**: `std::future` com tarefas `async`
- **Python**: `multiprocessing` e `concurrent.futures`

### 📚 Multiplicação de Matrizes Paralela

A ideia geral da multiplicação de matrizes paralela consiste em dividir o cálculo das linhas entre múltiplas threads, onde cada thread calcula um subconjunto das linhas da matriz resultado.

#### Implementação em Kotlin:

```kotlin
suspend fun multiplyMatrices(a: Array<DoubleArray>, b: Array<DoubleArray>, startRow: Int, endRow: Int): Array<DoubleArray> {
    val result = Array(endRow - startRow) { DoubleArray(b[0].size) }
    for (i in startRow until endRow) {
        for (j in b[0].indices) {
            result[i - startRow][j] = b.indices.sumOf { a[i][it] * b[it][j] }
        }
    }
    return result
}
```

Na execução, cada thread recebe um intervalo de linhas e realiza a multiplicação. No final, os resultados são combinados.

Em **C++**, a abordagem é similar, utilizando `std::async` para executar a multiplicação em paralelo.

Em **Python**, utilizamos `multiprocessing` para distribuir as tarefas entre múltiplos processos.

### 📚 Quicksort Paralelo

O **Quicksort** foi implementado com paralelismo ao dividir recursivamente os subarrays em diferentes threads até um nível limite de profundidade.

#### Implementação em Kotlin:

```kotlin
suspend fun parallelQuickSort(arr: IntArray, low: Int = 0, high: Int = arr.size - 1, depth: Int = 0, maxDepth: Int = 10) {
    if (low < high) {
        val pivotIndex = randomPartition(arr, low, high)

        if (depth < maxDepth) {
            coroutineScope {
                val left = async(Dispatchers.Default) { parallelQuickSort(arr, low, pivotIndex - 1, depth + 1, maxDepth) }
                val right = async(Dispatchers.Default) { parallelQuickSort(arr, pivotIndex + 1, high, depth + 1, maxDepth) }
                left.await()
                right.await()
            }
        } else {
            quickSort(arr, low, pivotIndex - 1)
            quickSort(arr, pivotIndex + 1, high)
        }
    }
}
```

A abordagem em **C++** e **Python** segue o mesmo princípio, utilizando `std::async` e `concurrent.futures.ProcessPoolExecutor`, respectivamente.

## 🎓 Conclusões

- **Python** é mais simples de implementar, mas o GIL limita seu desempenho real.
- **Kotlin** oferece um modelo flexível e eficiente com `coroutines`, facilitando a implementação.
- **C++** apresenta a melhor performance bruta, mas requer mais complexidade na gestão das threads.

## 📑 Referências

- FORBES, Elliot. *Learning Concurrency in Python*, 2017.
- GOETZ, Brian. *Java Concurrency in Practice*, 2006.
- ELIZAROV, Roman et al. *Kotlin Coroutines: Design and Implementation*, 2021.

## 👨‍🎓 Projeto de Pesquisa

- **Orientador**: Paulo Henrique Lopes Silva
- **Discente**: Breno Klywer Olegario de Moura
- **Status**: Em Andamento

---

🌟 Fique à vontade para contribuir com o projeto e compartilhar suas ideias!
