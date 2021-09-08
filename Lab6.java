//Classe que contem a variável em comum entre as threads
class Contador {

  private int r; //Recurso compartilhado
  
  //Construtor
  public Contador() { 
     this.r = 0;
  }

  public synchronized void inc() { 
     this.r++; 
  }

  public synchronized int get() { 
      return this.r; 
  }
  
}

//Classe que estende Thread e varre o vator da classe que contem a main, verificando se o elemento analisado é par, aumentando o contador compartilhado se sim 
class T extends Thread {
   
   private int id; //Identificador da thread
   
   Contador contador; //Objeto compartilhado entre threads
  
   //Construtor
   public T(int tid, Contador contador) { 
      this.id = tid; 
      this.contador = contador;
   }

   //Metodo principal da thread
   public void run() {
      for (int i = this.id; i < Lab6.tam; i += Lab6.N){
         if (Lab6.vetor[i] % 2 == 0){
            this.contador.inc();
         }
      }
   }
}

//Classe da aplicação, onde se contem a main
class Lab6 {
   static final int N = 8; //Número de threads
   static final int tam = 1000; //Tamanho do vetor
   static int[] vetor = new int[tam]; //Declarando o vetor que armazenará os valores a serem analisados

   public static void main (String[] args) {
      
      Thread[] threads = new Thread[N]; //Reserva espaço para um vetor de threads

      int contadorSeq = 0; //Contador sequencial para comparação
      
      //Preenchendo vetor (esperando resultado = tam/2 aproximadamente)
      for (int i=0; i < tam; i++){
         vetor[i] = i;
      }

      //Cria uma instancia do recurso compartilhado entre as threads
      Contador contador = new Contador();

      //Cria as threads da aplicacao
      for (int i=0; i < N; i++) {
         threads[i] = new T(i, contador);
      }

      //Inicia as threads
      for (int i=0; i < N; i++) {
         threads[i].start();
      }

      //Espera pelo termino de todas as threads
      for (int i=0; i < N; i++) {
         try { threads[i].join(); } catch (InterruptedException e) { return; }
      }

      //Realizando a função de forma sequencial
      for (int i = 0; i < tam; i ++){
         if (vetor[i] % 2 == 0){
            contadorSeq++;
         }
      }

      //Check de corretude comparando a função concorrente com a sequencial 
      if (contador.get() != contadorSeq){
         System.out.println("Erro na execução do programa!");
      } else {
         System.out.println("Programa executado com sucesso!");
         System.out.println("Numero de elementos pares: " + contadorSeq);
      }     
   }
}
