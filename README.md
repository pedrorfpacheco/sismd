# Parallel Digital Image Processing and Analysis - SISMD 2023/2024

- Bruno Santos - 1230170
- David Magalhães - 1201237
- Pedro Pacheco - 1181034
- Vera Pinto - 1180730

## Implementação (explicar o que fizemos por alto ou mostrar métodos importantes tipo a implementação do filtro em cada sequencial)

#### Brightness

1. Sequential
   O método BrighterFilter aumenta o brilho de cada pixel da imagem. Este algoritmo percorre cada pixel da imagem, aumenta os valores de vermelho (red), verde (green) e azul (blue) por um valor definido, mas sem exceder o máximo de 255 para cada cor. Se o aumento proposto para qualquer cor ultrapassar 255, essa cor é definida como 255. Depois de ajustar as cores, o pixel é atualizado na imagem temporária.

2. Multithreaded
   

3. Thread-Pool
   1. Executor
   
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

   Para cada pixel da imagem, ele calcula uma média dos valores de vermelho, verde e azul dos pixels vizinhos, incluindo o próprio pixel, numa submatriz de tamanho definido por *matrixSize*. Este tamanho determina quão longe de cada pixel a média deve considerar, criando um efeito de desfoque ao "misturar" os valores de cores dos pixels adjacentes.

   O método percorre todos os pixels da imagem, utilizando um deslocamento (offset) para definir a área da submatriz centrada em cada pixel. Os novos valores médios de cor calculados substituem os originais, resultando numa imagem desfocada.
   

2. Multithreaded
   O método BlurFilterMultiThread aplica um filtro numa imagem utilizando múltiplas *threads* para processar de forma paralela e aumentar a eficiência. Cada thread é responsável por desfocar uma parte específica. A imagem é dividida em segmentos horizontais, e cada segmento é processado por uma *thread* diferente. O número de linhas que cada thread processa é calculado para distribuir as linhas da imagem de maneira igualitária entre as *threads*. Após iniciar todas as *threads*, o método espera que todas terminem sua execução usando o método *join()*.

3. Thread-Pool
   1. Executor

      O método BlurFilterThreadPool usa um pool de threads para aplicar um filtro de desfoque a uma imagem, melhorando a eficiência para imagens grandes ou para sistemas com múltiplos processadores. A imagem é dividida em várias partes, cada uma sendo processada em paralelo por diferentes threads. A quantidade de tarefas é determinada pelo tamanho da imagem e pelo número de threads, ajustando para que cada tarefa tenha um trabalho significativo mas não muito pequeno (mínimo de 10000 pixels por tarefa).

      Cada tarefa é responsável por aplicar o desfoque em uma faixa horizontal específica da imagem, calculada com base na altura total da imagem e no número de tarefas. Depois de iniciar todas as tarefas, o método espera que todas terminem usando awaitTermination.
      

   2. Fork Join Pool

      O método BlurFilterForkJoinPool utiliza a estrutura ForkJoinPool para aplicar um filtro de desfoque a uma imagem de maneira eficiente e paralela. A imagem é processada dividindo-a em sub-regiões menores, que são então atribuídas a diferentes threads gerenciadas pelo ForkJoinPool. Cada thread trabalha em uma seção da imagem, aplicando o filtro de desfoque, e esse processo é feito de maneira recursiva até que as seções atinjam um tamanho de limite (THRESHOLD), momento em que o filtro é aplicado diretamente.

      A classe BlurFilterForkJoinPoolTask, que estende RecursiveAction, é responsável por essa divisão e pelo processamento do filtro. Se a área a ser processada é pequena o suficiente (menor que o THRESHOLD), o filtro é aplicado diretamente. Caso contrário, a tarefa é dividida em quatro sub-tarefas, processando cada quadrante da área de forma recursiva.
      

   3. Completable Futures

      
      

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