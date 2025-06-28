# 🐍 Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python 🚀

### Curso: Ciência da Computação 💻

### Universidade: UFERSA - Universidade Federal Rural do Semi-Árido 🌱

### Status: Pesquisa Concluída ✅

## 👥 Autores

* **Discente:** Breno Klywer Olegario de Moura
* **Orientador:** Paulo Henrique Lopes Silva

---

## 📚 Introdução

A **computação paralela** é fundamental para extrair o máximo de desempenho de processadores multicore. A escolha da linguagem e do paradigma de programação impacta diretamente a eficiência, escalabilidade e complexidade do desenvolvimento.

Este estudo apresenta uma **análise comparativa de desempenho** da paralelização dos algoritmos **Quicksort** e **Multiplicação de Matrizes** utilizando **C++, Kotlin e Python**.

---

## 🎯 Objetivos

* **Geral:** Avaliar comparativamente a eficiência de linguagens modernas em tarefas de paralelismo.
* **Específicos:**

  * Medir desempenho e escalabilidade das linguagens em hardware multicore.
  * Analisar gargalos como GIL e overhead de criação de threads.
  * Identificar pontos críticos de desempenho e robustez das abordagens.

---

## 🔍 Metodologia

1. **Ambiente de Teste:**

   * **CPU:** AMD Ryzen 5 5600G (6 Núcleos, 12 Threads)
   * **RAM:** 16 GB DDR4
   * **SO:** Windows 11 Pro 24H2
   * **Versões:** C++17 (MSVC v19.38), Kotlin 2.0.0 (JVM 23), Python 3.13.3

2. **Processo de Benchmark:**

   * 3 execuções de aquecimento (descartadas)
   * 7 medições independentes para média e desvio padrão

3. **Métricas:**

   * **Tempo de Execução (s)**
   * **Speedup:** $T_{sequencial} / T_{paralelo}$

---

## 🛠️ Algoritmos Implementados

1. **Quicksort Paralelo**
2. **Multiplicação de Matrizes Paralela**

### C++

* `std::async` e `std::future`

### Kotlin

* Corrotinas (`kotlinx.coroutines`)

### Python

* `ThreadPoolExecutor` (quicksort)
* `multiprocessing` (matmul)

---

## 📈 Resultados e Análise

### Quicksort Paralelo

| Linguagem  | Tamanho (N) | Seq. (s) | Paralelo (s) | Speedup   |
| ---------- | ----------- | -------- | ------------ | --------- |
| **C++**    | 5.000.000   | 0.3589   | **0.1057**   | **3.39x** |
| **Kotlin** | 5.000.000   | 0.4808   | 0.5151       | 0.93x     |
| **Python** | 5.000.000   | 20.5430  | 21.2038      | 0.97x     |

**Destaques:**

* C++ teve o melhor desempenho.
* Kotlin sofreu com overhead.
* Python travou com `ThreadPoolExecutor` em `maxDepth > 5`.

### Multiplicação de Matrizes

| Linguagem  | Tamanho (N x N) | Seq. (s) | Paralelo (s) | Speedup   |
| ---------- | --------------- | -------- | ------------ | --------- |
| **Kotlin** | 1473            | 10.6651  | **1.8342**   | **5.82x** |
| **Python** | 1473            | 0.0666   | 0.6225       | 0.11x     |

**Destaques:**

* Kotlin escalou muito bem.
* Python sofreu com o overhead do IPC.

---

## 🎓 Conclusões

* **C++:** Melhor desempenho absoluto, porém exige sintonia com `maxDepth`.
* **Kotlin:** Robusto, produtivo e escalável.
* **Python:** Simples, mas limitado pelo GIL e overhead do `multiprocessing`.

---

## 🔮 Trabalhos Futuros

* Usar bibliotecas industriais como **OpenMP (C++)** e **Numba (Python)**.
* Criar um modelo preditivo para o `maxDepth` ótimo no Quicksort.

---

## 📑 Referência Principal

> Moura, B. K. O. de, & Silva, P. H. L. (2024). *Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python*. Departamento de Computação, UFERSA.
🌟 *Fique à vontade para explorar, contribuir ou adaptar este estudo!*

---

Se quiser, posso salvar ou exportar esse conteúdo em `.md`. Deseja isso?
