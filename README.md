# Parallel Digital Image Processing and Analysis - SISMD 2023/2024

- Bruno Santos - 1230170
- David Magalhães - 1201237
- Pedro Pacheco - 1181034
- Vera Pinto - 1180730

## Implementação (explicar o que fizemos por alto ou mostrar métodos importantes tipo a implementação do filtro em cada sequencial)

#### Brightness

1. Sequential
2. Multithreaded
3. Thread-Pool
   1. Executor
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