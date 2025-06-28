# 🐍 Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python 🚀

### Curso: Ciência da Computação 💻

### Universidade: UFERSA - Universidade Federal Rural do Semi-Árido 🌱

### Status: Pesquisa Concluída ✅

---

## 👥 Autores

* **Discente:** Breno Klywer Olegario de Moura
* **Orientador:** Paulo Henrique Lopes Silva

---

## 📚 Introdução

A **computação paralela** é fundamental para extrair o máximo de desempenho de processadores multicore modernos. No entanto, a escolha da linguagem e do paradigma de programação impacta diretamente a eficiência, a escalabilidade e a complexidade do desenvolvimento.

Este estudo apresenta uma **análise comparativa de desempenho** da paralelização dos algoritmos **Quicksort** e **Multiplicação de Matrizes** utilizando **C++, Kotlin e Python**. Analisamos os trade-offs de cada ecossistema: desde o gerenciamento de threads nativas em C++, passando pelas corrotinas de alta eficiência da JVM em Kotlin, até os desafios impostos pelo *Global Interpreter Lock* (GIL) em Python.

---

## 🎯 Objetivos

* **Geral:** Avaliar comparativamente a eficiência de C++, Kotlin e Python em tarefas que exigem alto grau de paralelismo de CPU.
* **Específicos:**

  * Medir o desempenho e a escalabilidade das implementações em hardware de múltiplos núcleos.
  * Analisar gargalos específicos de cada ecossistema, como o impacto do GIL, o custo de comunicação entre processos (IPC) e o overhead de criação de threads.
  * Identificar pontos críticos de desempenho, como o `maxDepth` ótimo para recursão paralela e a robustez de cada abordagem contra deadlocks.

---

## 🛠️ Pré-requisitos

Para replicar o ambiente de teste e executar os benchmarks, você precisará do seguinte:

* **Sistema Operacional:** Windows 11 Pro 24H2
* **Compilador C++:** Compatível com C++17 (ex.: MSVC v19.38)
* **Ambiente Kotlin/Java:**

  * JDK 23 ou superior
  * Kotlin 2.0.0 ou superior
* **Python:** Python 3.13.3 ou superior

---

## 🚀 Como Executar os Testes

### C++

1. Navegue até o diretório do código C++.
2. Compile utilizando o compilador:

   ```bash
   cl /EHsc /O2 /std:c++17 seu_codigo.cpp -o quicksort_cpp.exe
   ```
3. Execute:

   ```bash
   ./quicksort_cpp.exe
   ```

### Kotlin

1. Compile o arquivo `.kt`:

   ```bash
   kotlinc seu_codigo.kt -include-runtime -d seu_codigo.jar
   ```
2. Execute:

   ```bash
   java -jar seu_codigo.jar
   ```

### Python

1. Execute diretamente:

   ```bash
   python seu_script.py
   ```

---

## 📈 Resultados e Análise

### Quicksort Paralelo

| Linguagem | Tamanho (N) | Tempo Seq. (s) | Paralelo (s) | Speedup   |
| --------- | ----------- | -------------- | ------------ | --------- |
| C++       | 5.000.000   | 0.3589         | **0.1057**   | **3.39x** |
| Kotlin    | 5.000.000   | 0.4808         | 0.5151       | 0.93x     |
| Python    | 5.000.000   | 20.5430        | 21.2038      | 0.97x     |

**Análise:**

* **C++** teve o melhor desempenho, mas exige ajuste fino do `maxDepth` para evitar overhead excessivo de threads.
* **Kotlin** foi estável, mas seu modelo de tarefas pequenas gerou um leve *slowdown*.
* **Python** travou com `ThreadPoolExecutor` em `maxDepth > 5`, revelando vulnerabilidade a deadlocks.

---

### Multiplicação de Matrizes

| Linguagem | Tamanho (N x N) | Tempo Seq. (s) | Paralelo (s) | Speedup   |
| --------- | --------------- | -------------- | ------------ | --------- |
| Kotlin    | 1473            | 10.6651        | **1.8342**   | **5.82x** |
| Python    | 1473            | 0.0666         | 0.6225       | 0.11x     |

**Análise:**

* **Kotlin** apresentou excelente escalabilidade, atingindo 5.82x de speedup.
* **Python**, mesmo com `multiprocessing`, sofreu com overhead de IPC que eliminou os ganhos do paralelismo.

---

## 🎓 Conclusões

* **C++:** Melhor desempenho absoluto. Exige atenção à profundidade de paralelismo para evitar perda de performance.
* **Kotlin:** Altamente produtivo e escalável, com corrotinas bem gerenciadas pela JVM.
* **Python:** Fácil de usar, mas limitado para CPU-bound devido ao GIL e IPC.

---

## 🔮 Trabalhos Futuros

* Comparar com bibliotecas especializadas como **OpenMP** (C++) e **Numba** (Python).
* Criar uma heurística para prever o `maxDepth` ideal com base no hardware e tamanho de entrada.

---

## 📑 Referência Principal

> Moura, B. K. O. de, & Silva, P. H. L. (2024). *Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python*. Departamento de Computação, UFERSA.

---

🌟 *Fique à vontade para explorar, adaptar ou contribuir com este estudo!*
