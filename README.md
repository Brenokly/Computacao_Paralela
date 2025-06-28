# 🐍 Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python 🚀

### Curso: Ciência da Computação 💻
### Universidade: UFERSA - Universidade Federal Rural do Semi-Árido 🌱
### Status: Pesquisa Concluída ✅

## 👥 Autores

* **Discente:** Breno Klywer Olegario de Moura
* **Orientador:** Paulo Henrique Lopes Silva

---

## 📚 Introdução

[cite_start]A **computação paralela** é fundamental para extrair o máximo de desempenho de processadores multicore modernos[cite: 2]. [cite_start]No entanto, a escolha da linguagem e do paradigma de programação impacta diretamente a eficiência, a escalabilidade e a complexidade do desenvolvimento[cite: 3].

[cite_start]Este estudo apresenta uma **análise comparativa de desempenho** da paralelização dos algoritmos **Quicksort** e **Multiplicação de Matrizes** utilizando **C++, Kotlin e Python**[cite: 4]. [cite_start]Analisamos os trade-offs de cada ecossistema, desde o gerenciamento de threads nativas em C++ [cite: 5][cite_start], passando pelas corrotinas de alta eficiência da JVM em Kotlin [cite: 5][cite_start], até os desafios impostos pelo *Global Interpreter Lock* (GIL) em Python[cite: 6].

---

## 🎯 Objetivos

* **Geral:** Avaliar comparativamente a eficiência de C++, Kotlin e Python em tarefas que exigem alto grau de paralelismo de CPU.
* **Específicos:**
    * [cite_start]Medir o desempenho e a escalabilidade das implementações em hardware de commodity com múltiplos núcleos[cite: 21].
    * [cite_start]Analisar gargalos específicos de cada ecossistema, como o impacto do GIL, o custo de comunicação entre processos (IPC) e o overhead de criação de threads[cite: 23].
    * [cite_start]Identificar pontos críticos de desempenho, como o `maxDepth` ótimo para recursão paralela e a robustez de cada abordagem contra deadlocks[cite: 8, 22].

---

## 🛠️ Pré-requisitos

Para replicar o ambiente de teste e executar os benchmarks, você precisará do seguinte software instalado e configurado no seu sistema:

* [cite_start]**Sistema Operacional:** Os testes foram realizados no **Windows 11 Pro 24H2**[cite: 37].
* **Compilador C++:** Um compilador com suporte a C++17. [cite_start]O estudo utilizou o **MSVC v19.38**[cite: 37], que acompanha o Visual Studio ou o Visual Studio Build Tools.
* **Ambiente Kotlin/Java:**
    * [cite_start]**JDK (Java Development Kit):** Versão 23 ou superior[cite: 37].
    * [cite_start]**Kotlin:** Versão 2.0.0 ou superior[cite: 37].
* [cite_start]**Python:** **Python 3.13.3** ou superior[cite: 37].

---

## 🚀 Como Executar os Testes

[cite_start]Os testes foram automatizados por um script que executa cada cenário, coleta os tempos e calcula as médias, garantindo a consistência dos resultados[cite: 38]. Para executar manualmente cada implementação:

### C++
1.  Navegue até o diretório do código C++.
2.  Compile o arquivo-fonte utilizando o compilador MSVC (ou outro de sua preferência).
    ```bash
    # Exemplo de compilação com o compilador cl.exe do Visual Studio
    cl /EHsc /O2 /std:c++17 seu_codigo.cpp -o quicksort_cpp.exe
    ```
3.  Execute o binário gerado.
    ```bash
    ./quicksort_cpp.exe
    ```

### Kotlin
1.  Navegue até o diretório do código Kotlin.
2.  Compile o arquivo `.kt` para um arquivo `.jar`.
    ```bash
    kotlinc seu_codigo.kt -include-runtime -d seu_codigo.jar
    ```
3.  Execute o arquivo `.jar` utilizando a JVM.
    ```bash
    java -jar seu_codigo.jar
    ```

### Python
1.  Navegue até o diretório do código Python.
2.  Certifique-se de que não há dependências externas além da biblioteca padrão.
3.  Execute o script diretamente com o interpretador Python.
    ```bash
    python seu_script.py
    ```

---

## 📈 Resultados e Análise

### Quicksort Paralelo

| Linguagem | Tamanho (N) | Seq. (s) | Paralelo (s) | Speedup   |
| :-------- | :---------- | :------- | :----------- | :-------- |
| **C++** | 5.000.000   | 0.3589   | **0.1057** | **3.39x** |
| **Kotlin**| 5.000.000   | 0.4808   | 0.5151       | 0.93x     |
| **Python**| 5.000.000   | 20.5430  | 21.2038      | 0.97x     |

**Destaques e Análise Detalhada:**
* [cite_start]**C++** apresentou o melhor desempenho, mas o resultado depende da sintonia do `maxDepth`[cite: 66]. [cite_start]Após um ponto ótimo, o overhead da criação excessiva de threads nativas degrada a performance, criando uma curva em "U"[cite: 57].
* **Kotlin** sofreu um leve *slowdown*. [cite_start]Seu scheduler de corrotinas se mostrou extremamente estável e resiliente, mas o overhead de gerenciamento de muitas tarefas pequenas superou o ganho do paralelismo neste algoritmo específico[cite: 58, 67].
* [cite_start]**Python** travou com `ThreadPoolExecutor` quando `maxDepth` foi maior que 5[cite: 59]. [cite_start]Isso confirma que a biblioteca é vulnerável a deadlocks em cenários de paralelismo recursivo, além de ser ineficaz devido ao GIL[cite: 68, 82].

### Multiplicação de Matrizes

| Linguagem | Tamanho (N x N) | Seq. (s) | Paralelo (s) | Speedup   |
| :-------- | :-------------- | :------- | :----------- | :-------- |
| **Kotlin**| 1473            | 10.6651  | **1.8342** | **5.82x** |
| **Python**| 1473            | 0.0666   | 0.6225       | 0.11x     |

**Destaques e Análise Detalhada:**
* [cite_start]**Kotlin** demonstrou excelente escalabilidade em uma tarefa "embaraçosamente paralela"[cite: 69]. [cite_start]O speedup cresceu consistentemente com o tamanho da matriz, atingindo **5.82x** e provando a eficiência da JVM e das corrotinas para esta carga de trabalho[cite: 72].
* **Python**, mesmo usando `multiprocessing` para contornar o GIL, sofreu um *slowdown* drástico. [cite_start]O custo de serializar e transferir as sub-matrizes entre os processos (overhead de IPC) foi tão alto que anulou completamente qualquer ganho com o paralelismo[cite: 73].

---

## 🎓 Conclusões

* **C++:** Oferece o **melhor desempenho absoluto**, mas exige responsabilidade. [cite_start]O desenvolvedor precisa realizar uma sintonia fina (como o `maxDepth`) para gerenciar o overhead e evitar degradação de performance[cite: 77, 78].
* **Kotlin:** Provou ser uma **alternativa robusta, produtiva e escalável**. [cite_start]Seu modelo de corrotinas com um scheduler inteligente (work-stealing) o torna uma opção resiliente e atraente para concorrência na JVM[cite: 79, 86].
* [cite_start]**Python:** As bibliotecas padrão (`ThreadPoolExecutor`, `multiprocessing`) são **inadequadas para paralelismo de CPU de alta performance**[cite: 87]. [cite_start]Seu poder em computação numérica vem de bibliotecas como NumPy, que delegam o trabalho pesado para código otimizado em C/Fortran[cite: 84].

---

## 🔮 Trabalhos Futuros

* [cite_start]Realizar uma comparação direta com bibliotecas otimizadas padrão da indústria, como **OpenMP (C++)** e **Numba (Python)**, para avaliar o desempenho contra soluções mais especializadas[cite: 88].
* [cite_start]Desenvolver um modelo heurístico para prever o `maxDepth` ótimo para o Quicksort com base no número de núcleos da máquina e no tamanho da entrada de dados[cite: 89].

---

## 📑 Referência Principal

> Moura, B. K. O. de, & Silva, P. H. L. (2024). *Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python*. [cite_start]Departamento de Computação, UFERSA[cite: 1].

🌟 *Fique à vontade para explorar, contribuir ou adaptar este estudo!*
