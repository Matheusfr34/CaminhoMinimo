import java.io.*;
import java.util.*;

class Grafo {

    private static class Lista {
        private static class Nodo {
            int valor;
            Nodo prox;
            Nodo(int valor) {
                this.valor = valor;
            }
        }

        private Nodo inicio;
        private Nodo fim;
        private int tamanho;

        public void adicionar(int valor) {
            Nodo novo = new Nodo(valor);
            if (inicio == null) {
                inicio = fim = novo;
            } else {
                fim.prox = novo;
                fim = novo;
            }
            tamanho++;
        }

        public boolean contem(int x) {
            for (Nodo i = inicio; i != null; i = i.prox)
                if (i.valor == x) return true;
            return false;
        }

        public int[] elementos() {
            int[] arr = new int[tamanho];
            int k = 0;
            for (Nodo i = inicio; i != null; i = i.prox)
                arr[k++] = i.valor;
            Arrays.sort(arr);
            return arr;
        }
    }

    private Lista[] adj;
    private Aresta[] listaArestas;
    public int qtdVertices;
    private int contadorArestas = 0;

    public Grafo(int vertices, int arestas) {
        this.qtdVertices = vertices;
        this.listaArestas = new Aresta[arestas];
        this.adj = new Lista[vertices + 1];
        for (int i = 0; i <= vertices; i++)
            adj[i] = new Lista();
    }

    public void inserirAresta(int origem, int destino, int peso) {
        adj[origem].adicionar(destino);
        listaArestas[contadorArestas++] = new Aresta(origem, destino, peso);
    }

    public int grauSaida(int v) {
        return adj[v].tamanho;
    }

    public int grauEntrada(int v) {
        int grau = 0;
        for (int i = 1; i <= qtdVertices; i++)
            if (adj[i].contem(v)) grau++;
        return grau;
    }

    public int[] sucessores(int v) {
        return adj[v].elementos();
    }

    public int[] predecessores(int v) {
        int[] pred = new int[grauEntrada(v)];
        int idx = 0;
        for (int i = 1; i <= qtdVertices; i++) {
            if (i != v && adj[i].contem(v))
                pred[idx++] = i;
        }
        return pred;
    }

    public Aresta[] arestas() {
        return listaArestas;
    }
}

record Aresta(int origem, int destino, int peso) {}

class BellmanFord {

    private int[] distancia;
    private int[] predecessor;
    private int[] arestasUsadas;
    private Grafo grafo;

    public BellmanFord(Grafo g) {
        this.grafo = g;
        int n = g.qtdVertices;
        this.distancia = new int[n + 1];
        this.predecessor = new int[n + 1];
        this.arestasUsadas = new int[n + 1];
    }

    public void calcular(int origem) {
        Arrays.fill(distancia, Integer.MAX_VALUE);
        Arrays.fill(predecessor, -1);
        Arrays.fill(arestasUsadas, Integer.MAX_VALUE);

        distancia[origem] = 0;
        arestasUsadas[origem] = 0;

        for (int i = 1; i < grafo.qtdVertices; i++) {
            boolean alterou = false;

            for (Aresta e : grafo.arestas()) {
                int u = e.origem();
                int v = e.destino();
                int peso = e.peso();

                if (distancia[u] != Integer.MAX_VALUE) {
                    int novaDist = distancia[u] + peso;
                    int novasArestas = arestasUsadas[u] + 1;

                    // Menor distância OU mesma distância mas menos arestas
                    if (novaDist < distancia[v] || (novaDist == distancia[v] && novasArestas < arestasUsadas[v])) {
                        distancia[v] = novaDist;
                        predecessor[v] = u;
                        arestasUsadas[v] = novasArestas;
                        alterou = true;
                    }
                }
            }

            if (!alterou) break;
        }
    }

    public Caminho reconstruirCaminho(int origem, int destino) {
        Caminho c = new Caminho();
        if (distancia[destino] == Integer.MAX_VALUE) return c;

        LinkedList<Integer> lista = new LinkedList<>();
        for (int v = destino; v != -1; v = predecessor[v])
            lista.addFirst(v);

        c.caminho = lista;
        c.distanciaTotal = distancia[destino];
        c.numArestas = arestasUsadas[destino];
        return c;
    }
}

class Caminho {
    public LinkedList<Integer> caminho = new LinkedList<>();
    public int distanciaTotal = Integer.MAX_VALUE;
    public int numArestas = 0;
}

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner entrada = new Scanner(System.in);

        System.out.print("Arquivo de entrada: ");
        String nomeArquivo = entrada.nextLine();
        Scanner file = new Scanner(new File(nomeArquivo));

        int nVertices = file.nextInt();
        int nArestas = file.nextInt();
        Grafo g = new Grafo(nVertices, nArestas);

        for (int i = 0; i < nArestas; i++) {
            int origem = file.nextInt();
            int destino = file.nextInt();
            int peso = file.nextInt();
            g.inserirAresta(origem, destino, peso);
        }

        System.out.print("Informe origem e destino: ");
        int origem = entrada.nextInt();
        int destino = entrada.nextInt();

        long inicio = System.currentTimeMillis();
        BellmanFord bf = new BellmanFord(g);
        bf.calcular(origem);
        long fim = System.currentTimeMillis();

        Caminho c = bf.reconstruirCaminho(origem, destino);

        System.out.println("\n=== RESULTADOS ===");
        if (c.caminho.isEmpty()) {
            System.out.println("Não existe caminho entre os vértices informados.");
        } else {
            System.out.println("Distância total: " + c.distanciaTotal);
            System.out.println("Número de arestas: " + c.numArestas);
            System.out.println("Caminho: " + c.caminho);            
        }
        System.out.println("Tempo (ms): " + (fim - inicio));

        entrada.close();
        file.close();
    }
}
