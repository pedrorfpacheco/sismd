# Parallel Digital Image Processing and Analysis - SISMD 2023/2024

- Bruno Santos - 1230170
- David Magalhães - 1201237
- Pedro Pacheco - 1181034
- Vera Pinto - 1180730

## Implementação (explicar o que fizemos por alto ou mostrar métodos importantes tipo a implementação do filtro em cada sequencial)

#### Brightness

1. **Sequential** - 
   O método BrighterFilter aumenta o brilho de cada pixel da imagem. Este algoritmo percorre cada pixel da imagem, aumenta os valores de vermelho (red), verde (green) e azul (blue) por um valor definido, mas sem exceder o máximo de 255 para cada cor. Se o aumento proposto para qualquer cor ultrapassar 255, essa cor é definida como 255. Depois de ajustar as cores, o pixel é atualizado na imagem temporária.

2. **Multithreaded** - 
   

3. **Thread-Pool**
   1. **Executor** - 
   
      O método BrighterFilterThreadPool aplica um filtro para aumentar o brilho de uma imagem usando uma pool de threads para processamento paralelo, o que melhora a eficiência, especialmente em imagens grandes. Este método divide a imagem em faixas horizontais, onde cada thread em um pool fixo processa uma faixa, aumentando o brilho de cada pixel na faixa designada.
   
      A divisão é feita de modo que todas as threads tenham aproximadamente a mesma quantidade de trabalho, distribuindo as linhas restantes pelas primeiras threads caso a divisão não seja exata. Após submeter todas as tarefas ao executor, o método espera que todas as threads terminem usando *awaitTermination*.
      
   
   2. Fork Join Pool
      
   
   3. Completable Futures
      

#### Grayscale

1. **Sequential** - 
 O filtro de escala de cinza, implementado no método GrayScaleFilter, 
 converte uma imagem colorida em tons de cinza. Isso é feito calculando 
 a média dos valores de vermelho, verde e azul para cada pixel e, em seguida, 
 definindo os componentes de cor do pixel para esse valor médio.


2. **Multithreaded** -
   Determina-se o número de linhas que cada thread deveria processar. Isso foi feito dividindo a altura da imagem pelo número de threads e armazena-se o resultado na variável rowsPerThread. As linhas restantes foram distribuídas entre as threads.  
   Em seguida, cria-se um array de threads e inicia-se cada thread para processar uma secção específica da imagem. Cada thread foi criada com uma instância da classe GrayFilterThread, que foi passada a imagem original, as linhas de início e fim que a thread deveria processar.  
   Depois que todas as threads foram iniciadas, utiliza-se o método join para esperar que todas as threads terminassem de processar as suas respectivas secções da imagem.  
   No fim, escreve-se a imagem processada em um arquivo usando a função Utils.writeImage.


3. **Thread-Pool**
   1. **Executor** -
      Determina-se o número de linhas que cada tarefa deveria processar. Isso foi feito dividindo a altura da imagem pelo número de threads e armazenando o resultado na variável rowsPerTask. As linhas restantes foram distribuídas entre as tarefas.  
      Em seguida, cria-se um ExecutorService com um número fixo de threads.  
      Depois, para cada thread, submete-se uma nova tarefa ao ExecutorService. Cada tarefa foi criada com uma instância da classe GrayFilterTask, que foi passada a imagem original, as linhas de início e fim que a tarefa deveria processar.
      Na classe GrayFilterTask, o método run() aplica o filtro de escala de cinza a cada pixel da imagem à secção de linhas que a tarefa deveria processar.
      Após todas as tarefas terem sido submetidas, desliga-se o ExecutorService e aguarda-se a conclusão de todas as tarefas.
      No fim, escreve-se a imagem processada em um arquivo usando a função Utils.writeImage.

   2. **Fork Join Pool** -
      Cria-se uma nova instância de ForkJoinPool com um número específico de threads.  
      Em seguida, você cria-se uma nova tarefa GrayFilterForkJoinPoolTask, que foi passada a imagem original, a imagem de destino, e as linhas de início e fim que a tarefa deveria processar.  
      Como a classe GrayFilterForkJoinPoolTask estende RecursiveAction, a tarefa é dividida em sub-tarefas menores até que o tamanho da tarefa seja menor que um valor específico.
      Depois, submete-se a tarefa ao ForkJoinPool usando o método invoke.  
      Finalmente, escreve-se a imagem processada em um arquivo usando a função Utils.writeImage.

   3. **Completable Futures** -
      Cria-se um ExecutorService com um número fixo de threads.  
      Em seguida, para cada thread, submete-se uma nova tarefa ao ExecutorService. Cada tarefa foi criada com uma instância da classe GrayCompletableFuturesTask, que foi passada a imagem original, as linhas de início e fim que a tarefa deveria processar.
      Na classe GrayCompletableFuturesTask, o método call() aplica o filtro cinza à secção da imagem que foi atríbuida à tarefa e retorna um CompletableFuture contendo a imagem processada.
      Depois que todas as tarefas foram submetidas, aguarda-se a conclusão de todas as tarefas usando o método get de cada CompletableFuture.  
      Finalmente, escreve-se a imagem processada em um arquivo usando a função Utils.writeImage.

#### Swirl

1. **Sequential**</br>
   O código começa determinando as dimensões da imagem, ou seja, sua altura e largura. 
   Em seguida, são calculadas as coordenadas do centro da imagem, que serão usadas como base para o filtro swirl. 
   Além disso, é definido o ângulo máximo de rotação, representado por "maxAngle" que será aplicado com base na distância do pixel ao centro da imagem.
   
   Em seguida, é criada uma matriz para armazenar a imagem filtrada, com as mesmas dimensões da imagem original.
   
   O código itera sobre cada pixel da imagem. Para cada pixel, calcula-se a distância entre o pixel e o centro da imagem usando a fórmula da distância. 
   Com base nessa distância, calcula-se o ângulo de rotação multiplicando a distância pelo ângulo máximo
   
   Usando o ângulo calculado, são calculadas as novas coordenadas do pixel após a aplicação do filtro swirl. 
   Isso é feito usando as fórmulas de transformação mencionadas na documentaçaão do projeto.

3. **Multithreaded**</br>
   O código cria várias threads para processar a imagem em paralelo. 
   Cada thread é responsável por uma parte da imagem, dividida igualmente com base no número de threads especificado.
   Caso nao seja possível dividir igualitariamente, a última thread assume a porção excedente da imagem.
   Para cada parte da imagem atribuída a uma thread, o código itera sobre cada pixel e aplica a transformação descrita anteriormente.
3. **Thread-Pool**
   1. **Executor**</br>
      Muito semelhante aos anteriores onde cada thread é responsável por uma parte específica da imagem, e o filtro de distorção é aplicado. 
      O que diferencia essa implementaçao é que o código utiliza um ExecutorService para gerenciar o pool de threads e aguarda a conclusão do processamento antes de continuar.
      Nesse caso, nao sendo necessário criar e dar start nas threads e nem fazer o join do trabalho.
   2. **Fork Join Pool**</br>
      Nesse caso, tambeém existe divisao das tarefas.
      Cada thread é responsável por processar uma parte específica da imagem.
      A classe `SwirlFilterForkJoinPoolTask` representa uma tarefa recursiva que aplica o filtro de distorção a uma parte da imagem.
      Essa parte foi definica como sendo a linha de pixeis da imagem. 
      Enquanto for possível dividir a imagem, novos empilhamentos de execução são criados e invocados recursivamente.
   3. **Completable Futures** </br>
      Esse algoritimo por sua vez executa de forma assíncrona usando CompletableFuture. 
      Ele começa definindo as dimensões da imagem e os parâmetros do filtro como todos os outros. 
      Para cada parte da imagem atribuída a uma tarefa, é criado um CompletableFuture que executa de maneira assíncrona para processar cada pixel (a mesma divisão aqui foi explicada no tópico de Miltithreads).
      Após a criação de todas as tarefas, a conclusão de todas elas é esperada atraveés do método allOf(). 
      Uma vez que todas as tarefas estejam concluídas, a imagem filtrada é escrita em um arquivo.

#### Glass

1. **Sequential** -
   Cria-se uma cópia da imagem original para armazenar a imagem processada.  
   Em seguida, percorre-se cada pixel da imagem original. Para cada pixel, calcula-se um deslocamento aleatório dentro de um raio especificado (5 pixeis).  
   Substituiu-se o pixel original pelo pixel deslocado na imagem copiada.  
   Finalmente, escreve-se a imagem processada num arquivo.


2. **Multithreaded** -
   Cria-se uma cópia da imagem original para armazenar a imagem processada.  
   Em seguida, dividi-se a imagem em várias secções, cada uma processada por uma thread separada.  
   Para cada thread, percorre cada pixel na secção atribuída a essa thread. Para cada pixel, calcula um deslocamento aleatório dentro de um raio especificado (5 pixeis).  
   Substituiu-se o pixel original pelo pixel deslocado na imagem copiada.  
   Finalmente, espera-se que todas as threads terminem e escreve a imagem processada num arquivo.


3. **Thread-Pool**
   1. **Executor** -
      Cria um ExecutorService com um número fixo de threads igual ao número de núcleos de CPU disponíveis.  
      Divide a imagem em várias secções, cada uma processada por uma tarefa separada. Cada tarefa foi responsável por aplicar o filtro de vidro a uma secção específica da imagem.  
      Submete todas as tarefas ao ExecutorService. Cada tarefa foi executada por uma thread do pool de threads.  
      Usa o método shutdown do ExecutorService para iniciar um desligamento ordenado, no qual as tarefas previamente submetidas são executadas, mas não são aceites novas tarefas. Em seguida, chama o método awaitTermination para bloquear até que todas as tarefas tenham concluído a execução após um pedido de desligar.  
      Escreve a imagem processada em um arquivo.
   
   2. **Fork Join Pool** -
      Cria um ForkJoinPool com um número fixo de threads igual ao número de núcleos de CPU disponíveis.  
      Cria uma tarefa GlassFilterForkJoinPoolTask que processa toda a imagem. Se a imagem era grande demais para ser processada eficientemente por uma única tarefa, divide a tarefa em duas tarefas menores para processaram metade da imagem cada uma.  
      Submete a tarefa ao ForkJoinPool. A tarefa foi executada por uma thread do pool de threads.  
      Usa o método invoke do ForkJoinPool para iniciar a execução da tarefa e esperar até que ela fosse concluída.  
      Por fim, escreve a imagem processada em um arquivo.
   
   3. **Completable Futures** -
      Cria um ExecutorService com um número fixo de threads igual ao número de núcleos de CPU disponíveis.  
      Divide a imagem em várias seções, cada uma processada por uma tarefa separada. Cada tarefa foi responsável por aplicar o filtro de vidro a uma seção específica da imagem.  
      Submete todas as tarefas ao ExecutorService. Cada tarefa foi executada por uma thread do pool de threads.  
      Usa o método allOf do CompletableFuture para criar um CompletableFuture que é concluído quando todas as tarefas são concluídas. Em seguida, você chamou o método join para bloquear até que todas as tarefas tenham concluído a execução.  
      Por fim, escreve a imagem processada em um arquivo.

#### Blur

1. **Sequential** - 

   Para cada pixel da imagem, ele calcula uma média dos valores de vermelho, verde e azul dos pixels vizinhos, incluindo o próprio pixel, numa submatriz de tamanho definido por *matrixSize*. Este tamanho determina quão longe de cada pixel a média deve considerar, criando um efeito de desfoque ao "misturar" os valores de cores dos pixels adjacentes.

   O método percorre todos os pixels da imagem, utilizando um deslocamento (offset) para definir a área da submatriz centrada em cada pixel. Os novos valores médios de cor calculados substituem os originais, resultando numa imagem desfocada.
   

2. **Multithreaded** - 
   O método BlurFilterMultiThread aplica um filtro numa imagem utilizando múltiplas *threads* para processar de forma paralela e aumentar a eficiência. Cada thread é responsável por desfocar uma parte específica. A imagem é dividida em segmentos horizontais, e cada segmento é processado por uma *thread* diferente. O número de linhas que cada thread processa é calculado para distribuir as linhas da imagem de maneira igualitária entre as *threads*. Após iniciar todas as *threads*, o método espera que todas terminem sua execução usando o método *join()*.

3. **Thread-Pool**
   1. **Executor** - 

      O método BlurFilterThreadPool usa um pool de threads para aplicar um filtro de desfoque a uma imagem, melhorando a eficiência para imagens grandes ou para sistemas com múltiplos processadores. A imagem é dividida em várias partes, cada uma sendo processada em paralelo por diferentes threads. A quantidade de tarefas é determinada pelo tamanho da imagem e pelo número de threads, ajustando para que cada tarefa tenha um trabalho significativo mas não muito pequeno (mínimo de 10000 pixels por tarefa).

      Cada tarefa é responsável por aplicar o desfoque em uma faixa horizontal específica da imagem, calculada com base na altura total da imagem e no número de tarefas. Depois de iniciar todas as tarefas, o método espera que todas terminem usando awaitTermination.
      

   2. **Fork Join Pool** - 

      O método BlurFilterForkJoinPool utiliza a estrutura ForkJoinPool para aplicar um filtro de desfoque a uma imagem de maneira eficiente e paralela. A imagem é processada dividindo-a em sub-regiões menores, que são então atribuídas a diferentes threads gerenciadas pelo ForkJoinPool. Cada thread trabalha em uma seção da imagem, aplicando o filtro de desfoque, e esse processo é feito de maneira recursiva até que as seções atinjam um tamanho de limite (THRESHOLD), momento em que o filtro é aplicado diretamente.

      A classe BlurFilterForkJoinPoolTask, que estende RecursiveAction, é responsável por essa divisão e pelo processamento do filtro. Se a área a ser processada é pequena o suficiente (menor que o THRESHOLD), o filtro é aplicado diretamente. Caso contrário, a tarefa é dividida em quatro sub-tarefas, processando cada quadrante da área de forma recursiva.
      

   3. **Completable Futures** - 

      
      

#### Conditional Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

## Resultados(colocar tabelas com os tempos, explicar as tabelas e colocar imagens de cada resultado)

### City.jpg

#### Brightness

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Grayscale

1. **Sequential** - 
2. **Multithreaded** - 
3. **Thread-Pool** - 
   1. **Executor** - 
   2. **Fork Join Pool** - 
   3. **Completable Futures** - 

#### Swirl

![city-swirl.jpg](StarterCode/assets/swirl/city-swirl.jpg)
1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures
   
#### Glass

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Conditional Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

### Tree.jpg

#### Brightness

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Grayscale

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Swirl

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Glass

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Conditional Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

### Turtle.jpg

#### Brightness

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Grayscale

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Swirl

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Glass

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Conditional Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

### Eye.jpg - Não fazer até decidirmos se é preciso

#### Brightness

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Grayscale

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Swirl

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Glass

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

#### Conditional Blur

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
   2. Fork Join Pool
   3. Completable Futures

### Garbage Collector

In order to improve performance, we looked into garbage collection tuning.
We analyzed the following garbage collectors: Serial, Parallel, G1, Shenandoah and Z.
The following criteria was used to compare the different garbage collectors:
- Throughput: Average time spent running code vs running GC;
- Latency: amount of time code pauses for GC to run;
- Memory Usage: the size of the heap;

For this project, we are particularly interested in throughput and latency, as image processing is a CPU-bound task, but we are also concerned with the time the user has to wait for the response with the resulting image.
Memory, however, isn't a big concern here since this is a small application.

#### Serial Garbage Collector

The Serial Garbage Collector works on a single thread.
As such it is best suited for single-processor machines.
It has the advantage of requiring a small amount of memory.

#### Parallel Garbage Collector

The Parallel Garbage Collector runs on multiple threads.
As such, it is recommended for multicore systems.
It is designed to reduce CPU time spent on garbage collection, being ideal for applications with little user interaction.

#### G1 Garbage Collector

The G1 (or Garbage First) Garbage Collector also uses multiple threads, but differs from the Parallel GB in that some work is done concurrently with the application.
The collector tries to achieve high throughput along with short pause times.
It is particularly useful for applications that require predictable garbage collection pause times.

#### Shenandoah Garbage Collector

The Shenandoah Garbage Collector is a low-pause-time garbage collector, having low latency.
It also works concurrently with the application, and attempts to avoid stop-the-world pauses.
It tries to keep pause times constant, regardless of heap size.

#### Z Garbage Collector

The Z Garbage Collector is a scalable garbage collector, designed for large heaps.
It is also low-latency, aiming not to exceed a pause time of 10ms, and works concurrently with the application.

#### Results

The following tests were conducted with the following conditions:
   * processor - AMD Ryzen 7 3700U with Radeon Vega Mobile Gfx, 2.30 GHz.
   * image size - 1920x1195 pixels.
   * number of threads - 9.
   * filter - Glass with fork join pool implementation.

| Garbage Collector | Time 1 (ms) | Time 2 (ms) | Time 3 (ms) | Average Time (ms) |
| ------------------ | ----------- | ----------- | ----------- |-------------------|
| Serial             |  361      |  322      |  329      | 337.33            |
| Parallel           |  358      |  321      |  291      | 323.33            |
| G1                 |  313      |  331      |  321      | 321.66            |
| Z                  |  437      |  412      |  425      | 424.66            |

The Shenandoah Garbage Collector was not tested as it is not supported on the used JDK distribution (Oracle).

As we can see, the G1 Garbage Collector had the best average time, closely followed by the Parallel Garbage Collector.
This was expected, as the G1 Garbage Collector has a good balance of throughput and latency, which is adequate for this application.

#### Tuning

