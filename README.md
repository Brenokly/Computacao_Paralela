Claro, peço desculpas por qualquer problema na formatação anterior.

Aqui está o conteúdo completo e atualizado para o seu arquivo `README.md`. Ele está no formato Markdown, pronto para ser copiado e colado diretamente no GitHub.

```markdown
# 🐍 Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python 🚀

**Curso:** Ciência da Computação 💻  
**Universidade:** UFERSA - Universidade Federal Rural do Semi-Árido 🌱  
**Status:** Pesquisa Concluída ✅

**Autores:**
* **Discente:** Breno Klywer Olegario de Moura
* **Orientador:** Paulo Henrique Lopes Silva

[cite_start]Este repositório documenta a pesquisa e os resultados do artigo **"Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python"** [cite: 1][cite_start], que realiza uma análise comparativa de desempenho entre as três linguagens na paralelização de algoritmos clássicos[cite: 4].

## 📚 Resumo da Pesquisa

[cite_start]A computação paralela é fundamental para extrair o máximo de desempenho de processadores multicore[cite: 2]. [cite_start]A escolha da linguagem e do paradigma de programação, no entanto, impacta diretamente a eficiência, a escalabilidade e a complexidade do desenvolvimento[cite: 3].

[cite_start]Este estudo apresenta uma análise de desempenho comparativa da paralelização do **Quicksort** e da **Multiplicação de Matrizes** utilizando C++, Kotlin e Python[cite: 4].

* [cite_start]Em **C++**, exploramos o paralelismo nativo com `std::async`[cite: 5].
* [cite_start]Em **Kotlin**, utilizamos corrotinas sobre a JVM para obter paralelismo gerenciado[cite: 5].
* [cite_start]Em **Python**, avaliamos o `ThreadPoolExecutor` (limitado pelo I/O) e a biblioteca `multiprocessing` para contornar o Global Interpreter Lock (GIL) em tarefas de uso intensivo da CPU[cite: 6].

## 🎯 Objetivos Atingidos

* [cite_start]**Avaliação Empírica Rigorosa:** Medimos o desempenho e a escalabilidade das implementações em hardware de commodity (AMD Ryzen 5 5600G, 6 núcleos/12 threads)[cite: 21, 37].
* [cite_start]**Análise de Gargalos:** Investigamos o impacto do GIL em Python, os custos de comunicação entre processos (IPC) e a eficiência dos modelos de agendamento de tarefas de cada ecossistema[cite: 23].
* [cite_start]**Identificação de Pontos Críticos:** Demonstramos quantitativamente o "ponto de inflexão" (`maxDepth` ótimo) onde o overhead de criação de threads em C++ supera os ganhos do paralelismo e o risco de deadlock em implementações recursivas com `ThreadPoolExecutor` em Python[cite: 8, 22].

## 🛠️ Metodologia

A avaliação foi conduzida sob uma metodologia de benchmark rigorosa para garantir a validade estatística.

1.  **Ambiente de Teste:**
    * [cite_start]**CPU:** AMD Ryzen 5 5600G (6 Núcleos, 12 Threads) @ 3.9GHz [cite: 37]
    * [cite_start]**RAM:** 16 GB DDR4 [cite: 37]
    * [cite_start]**SO:** Windows 11 Pro 24H2 [cite: 37]
    * [cite_start]**Versões:** C++17 (MSVC v19.38), Kotlin 2.0.0 (JVM 23), Python 3.13.3[cite: 37].
2.  [cite_start]**Processo de Benchmark:** Para cada teste, realizamos 3 execuções de aquecimento (descartadas) seguidas de 7 medições independentes[cite: 39, 40]. [cite_start]Os resultados apresentados são a média e o desvio padrão dessas medições[cite: 41].
3.  **Métricas:**
    * [cite_start]**Tempo de Execução (s):** Média das execuções[cite: 50].
    * [cite_start]**Speedup:** Razão entre o tempo de execução sequencial e o paralelo ($T_{sequencial}/T_{paralelo}$)[cite: 51].

## 📊 Resultados e Análise

### Quicksort Paralelo

[cite_start]O paralelismo foi aplicado de forma recursiva, com a profundidade controlada pelo parâmetro `maxDepth`[cite: 43].

* [cite_start]**C++:** Foi a linguagem mais performática, atingindo um **speedup de até 3.39x**[cite: 65, 66]. [cite_start]O desempenho é sensível ao `maxDepth`, com o ponto ótimo variando conforme o tamanho da entrada, revelando que o overhead de criação de threads se torna um gargalo após certa profundidade[cite: 57].
* [cite_start]**Kotlin:** Apresentou um *slowdown* (speedup < 1.0x), indicando que o overhead do gerenciamento de tarefas superou os ganhos do paralelismo[cite: 67]. [cite_start]No entanto, seu scheduler se mostrou robusto e estável, sem travar com o aumento da recursão[cite: 58].
* [cite_start]**Python (`ThreadPoolExecutor`):** Além do *slowdown* devido ao GIL [cite: 68][cite_start], a implementação **travou (deadlock) a partir de `maxDepth=5`** [cite: 56, 59][cite_start], confirmando que `ThreadPoolExecutor` é vulnerável e inadequado para algoritmos recursivos com paralelismo[cite: 59, 82].

| Linguagem | Tamanho (N) | Tempo Sequencial (s) | Melhor Tempo Paralelo (s) | Speedup |
| :--- | :--- | :--- | :--- | :--- |
| **C++** | 5.000.000 | 0.3589 | **0.1057** (d=8) | **3.39x** |
| **Kotlin**| 5.000.000 | 0.4808 | 0.5151 (d=4) | 0.93x |
| **Python**| 5.000.000 | 20.5430 | 21.2038 (d=2)| 0.97x |

[cite_start]*(Tabela adaptada da Tabela 2 do artigo [cite: 63, 65])*

### Multiplicação de Matrizes Paralela

[cite_start]Nesta tarefa, onde o cálculo de cada linha da matriz resultante foi distribuído[cite: 48], os resultados foram distintos.

* [cite_start]**Kotlin:** Demonstrou **excelente escalabilidade**, com o speedup crescendo com o tamanho da matriz, atingindo um pico de **5.82x**[cite: 71, 72]. [cite_start]Isso confirma a eficiência do modelo de corrotinas e da JVM para tarefas "embaraçosamente paralelas"[cite: 69, 72].
* [cite_start]**Python (`multiprocessing`):** Apresentou um *slowdown* drástico e consistente[cite: 73]. [cite_start]O custo de serialização e comunicação entre processos (IPC) para transferir as sub-matrizes foi tão alto que anulou completamente os ganhos obtidos com o paralelismo[cite: 73].

| Linguagem | Tamanho da Matriz (NxN) | Tempo Sequencial (s) | Tempo Paralelo (s) | Speedup |
| :--- | :--- | :--- | :--- | :--- |
| **Kotlin**| 1473 | 10.6651 | **1.8342** | **5.82x** |
| **Python**| 1473 | 0.0666 | 0.6225 | 0.11x |

[cite_start]*(Tabela adaptada da Tabela 3 do artigo [cite: 70, 71])*

## 🎓 Conclusões

1.  [cite_start]**C++:** Oferece o **melhor desempenho absoluto**, mas exige gerenciamento manual e sintonia fina (`maxDepth`) para evitar que o overhead de criação de threads degrade a performance[cite: 77, 78, 86].
2.  [cite_start]**Kotlin:** Provou ser uma **alternativa robusta e de alta produtividade**[cite: 9]. [cite_start]Seu modelo de corrotinas com um scheduler inteligente se mostrou resiliente, escalável e eficiente, destacando-se como uma excelente opção para paralelismo na JVM[cite: 79, 80, 86].
3.  [cite_start]**Python:** As bibliotecas padrão (`ThreadPoolExecutor`, `multiprocessing`) são **inadequadas para paralelismo de CPU de alta performance**[cite: 9, 87]. [cite_start]O `ThreadPoolExecutor` é ineficaz pelo GIL e arriscado em cenários recursivos[cite: 82]. [cite_start]O `multiprocessing` sofre com um overhead de IPC que o torna inviável para tarefas com comunicação de dados frequente[cite: 83].

## 🔮 Trabalhos Futuros

* [cite_start]Comparar os resultados com bibliotecas padrão da indústria, como **OpenMP** (para C++) e **Numba** (para Python)[cite: 88].
* [cite_start]Desenvolver um modelo heurístico para prever o `maxDepth` ótimo para o Quicksort com base em parâmetros de hardware e tamanho da entrada[cite: 89].

## 📑 Referência Principal

> Moura, B. K. O. de, & Silva, P. H. L. (2024). *Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python*. [cite_start]Departamento de Computação, Universidade Federal Rural do Semi-Árido (UFERSA). [cite: 1]

```
