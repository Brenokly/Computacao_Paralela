🐍 Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python 🚀

Curso: Ciência da Computação 💻

Universidade: UFERSA - Universidade Federal Rural do Semi-Árido 🌱

Status: Pesquisa Concluída ✅

Autores:

    Discente: Breno Klywer Olegario de Moura

    Orientador: Paulo Henrique Lopes Silva

Este repositório documenta a pesquisa e os resultados do artigo "Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python", que realiza uma análise comparativa de desempenho entre as três linguagens na paralelização de algoritmos clássicos.

📚 Resumo da Pesquisa

A computação paralela é fundamental para extrair o máximo de desempenho de processadores multicore. A escolha da linguagem e do paradigma de programação, no entanto, impacta diretamente a eficiência, a escalabilidade e a complexidade do desenvolvimento. 

Este estudo apresenta uma análise de desempenho comparativa da paralelização do 

Quicksort e da Multiplicação de Matrizes utilizando C++, Kotlin e Python. 

    Em 

    C++, exploramos o paralelismo nativo com std::async. 

Em 

Kotlin, utilizamos corrotinas sobre a JVM para obter paralelismo gerenciado. 

Em 

Python, avaliamos o ThreadPoolExecutor (limitado pelo GIL) e a biblioteca multiprocessing para contornar o Global Interpreter Lock (GIL) em tarefas de uso intensivo da CPU. 

🎯 Objetivos Atingidos

    Avaliação Empírica Rigorosa: Medimos o desempenho e a escalabilidade das implementações em hardware de commodity (AMD Ryzen 5 5600G, 6 núcleos/12 threads). 

Análise de Gargalos: Investigamos o impacto do GIL em Python, os custos de comunicação entre processos (IPC) e a eficiência dos modelos de agendamento de tarefas de cada ecossistema. 

Identificação de Pontos Críticos: Demonstramos quantitativamente o "ponto de inflexão" (maxDepth ótimo) onde o overhead de criação de threads em C++ supera os ganhos do paralelismo e o risco de deadlock em implementações recursivas com ThreadPoolExecutor em Python. 

🛠️ Metodologia

A avaliação foi conduzida sob uma metodologia de benchmark rigorosa para garantir a validade estatística. 

    Ambiente de Teste:

        CPU: AMD Ryzen 5 5600G (6 Núcleos, 12 Threads) @ 3.9GHz 

RAM: 16 GB DDR4 

SO: Windows 11 Pro 24H2 

Versões: C++17 (MSVC v19.38), Kotlin 2.0.0 (JVM 23), Python 3.13.3 

Processo de Benchmark: Para cada teste, realizamos 3 execuções de aquecimento (descartadas) seguidas de 7 medições independentes. Os resultados apresentados são a média e o desvio padrão dessas medições. 

Métricas:

    Tempo de Execução (s): Média das execuções. 

Speedup: Razão entre o tempo de execução sequencial e o paralelo (Tsequencial​/Tparalelo​). 

📊 Resultados e Análise

Quicksort Paralelo

O paralelismo foi aplicado de forma recursiva, com a profundidade controlada pelo parâmetro 

maxDepth. 

    C++: Foi a linguagem mais performática, atingindo um speedup de até 3.39x. O desempenho é sensível ao 

maxDepth, com o ponto ótimo variando conforme o tamanho da entrada, revelando que o overhead de criação de threads se torna um gargalo após certa profundidade. 

Kotlin: Apresentou um slowdown (speedup < 1.0x), indicando que o overhead do gerenciamento de corrotinas, para as muitas e pequenas tarefas geradas pelo Quicksort, superou os ganhos do paralelismo. No entanto, seu scheduler se mostrou robusto e estável, sem travar com o aumento da recursão. 

Python (ThreadPoolExecutor): Além do slowdown devido ao GIL , a implementação 

travou (deadlock) a partir de maxDepth=5, confirmando que ThreadPoolExecutor é vulnerável e inadequado para algoritmos recursivos com paralelismo. 

Linguagem
	

Tamanho (N)
	

Tempo Sequencial (s)
	

Melhor Tempo Paralelo (s)
	

Speedup

C++
	

5.000.000
	

0.3589
	

0.1057 (d=8)
	

3.39x

Kotlin
	

5.000.000
	

0.4808
	

0.5151 (d=4)
	

0.93x

Python
	

5.000.000
	

20.5430
	

21.2038 (d=2)
	

0.97x

Multiplicação de Matrizes Paralela

Nesta tarefa, onde o cálculo de cada linha da matriz resultante foi distribuído, os resultados foram distintos. 

    Kotlin: Demonstrou excelente escalabilidade, com o speedup crescendo linearmente com o tamanho da matriz, atingindo um pico de 5.82x. Isso confirma a eficiência do modelo de corrotinas e da JVM para tarefas "embaraçosamente paralelas". 

Python (multiprocessing): Apresentou um slowdown drástico e consistente (speedup de ~0.04x a 0.11x). O custo de serialização e comunicação entre processos (IPC) para transferir as sub-matrizes foi tão alto que anulou completamente os ganhos obtidos com o paralelismo. 

Linguagem
	

Tamanho da Matriz (NxN)
	

Tempo Sequencial (s)
	

Tempo Paralelo (s)
	

Speedup

Kotlin
	

1473
	

10.6651
	

1.8342
	

5.82x

Python
	

1473
	

0.0666
	

0.6225
	

0.11x

🎓 Conclusões

    C++: Oferece o melhor desempenho absoluto, mas exige gerenciamento manual e sintonia fina (maxDepth) para evitar que o overhead de criação de threads degrade a performance. 

Kotlin: Provou ser uma alternativa robusta e de alta produtividade. Seu modelo de corrotinas com um scheduler inteligente se mostrou resiliente, escalável e eficiente, destacando-se como uma excelente opção para paralelismo na JVM. 

Python: As bibliotecas padrão (ThreadPoolExecutor, multiprocessing) são inadequadas para paralelismo de CPU de alta performance. O 

ThreadPoolExecutor é ineficaz pelo GIL e arriscado em cenários recursivos , enquanto o 

multiprocessing sofre com um overhead de IPC que o torna inviável para tarefas com comunicação de dados frequente. O real poder do Python para computação numérica vem de bibliotecas especializadas (ex: NumPy) que delegam o trabalho para código C/Fortran otimizado. 

🔮 Trabalhos Futuros

    Comparar os resultados com bibliotecas padrão da indústria, como 

    OpenMP (para C++) e Numba (para Python). 

Desenvolver um modelo heurístico para prever o 

maxDepth ótimo para o Quicksort com base em parâmetros de hardware (núcleo, etc.) e tamanho da entrada. 

📑 Referência Principal

    Moura, B. K. O. de, & Silva, P. H. L. (2024). Implementação e Avaliação de Programas Paralelos usando C++, Kotlin e Python. Departamento de Computação, Universidade Federal Rural do Semi-Árido (UFERSA).
