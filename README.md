# Caminhamento em Grafos — Bellman-Ford

Este projeto implementa o algoritmo de Bellman-Ford em Java para a determinação de caminhos mínimos em grafos direcionados e ponderados com pesos positivos.  
Além de calcular o menor custo entre dois vértices, a implementação considera o critério de desempate baseado no **menor número de arestas** entre caminhos de mesmo custo, garantindo uma análise mais precisa da rota mais eficiente.

## Estrutura do Projeto

```
Caminhamento/
├── Main.java                 # Classe principal, responsável pela execução e interação com o usuário
├── ListaAdjacencia.java      # Estrutura de dados utilizada para representar o grafo
├── BellmanFord.java          # Implementação do algoritmo de Bellman-Ford
├── gerar_resultados.py       # Script em Python para automatizar testes e geração de tabelas/gráficos
├── grafico_denso.png         # Gráfico de desempenho (grafos densos)
├── grafico_esparso.png       # Gráfico de desempenho (grafos esparsos)
├── grafo_denso_*.txt         # Instâncias de grafos densos usadas para testes
├── grafo_esparso_*.txt       # Instâncias de grafos esparsos usadas para testes
└── resultados_completos.xlsx # Tabelas de resultados geradas automaticamente
```

## Requisitos

- **Java 17** ou superior
- **Python 3.8+**
- Bibliotecas Python:
  - `pandas`
  - `matplotlib`
  - `openpyxl`

Instale as dependências Python com:
```bash
pip install pandas matplotlib openpyxl
```

## Como Executar

### 1. Compilar e executar o código Java
Na raiz do projeto:
```bash
javac Main.java
java Main
```

Ao rodar o programa, será solicitado o nome do arquivo contendo o grafo e o par de vértices de origem e destino.  
O programa exibirá:
- O tamanho do caminho mínimo em distância
- O número de arestas no caminho
- O caminho completo
- O tempo de execução

### 2. Executar os testes automáticos
Para gerar as tabelas e gráficos de desempenho:
```bash
python3 gerar_resultados.py
```

Isso executará automaticamente o algoritmo sobre os grafos de teste e criará:
- `tabela_denso.csv`
- `tabela_esparso.csv`
- `resultados_completos.xlsx`
- `grafico_denso.png`
- `grafico_esparso.png`

## Entradas do Programa

Cada arquivo de grafo segue o formato:
```
<número_de_vértices> <número_de_arestas>
<origem> <destino> <peso>
<origem> <destino> <peso>
...
```

Exemplo:
```
5 4
1 2 3
2 3 2
3 4 1
4 5 4
```

## Resultados

Os testes foram realizados com dois tipos de grafos:
- **Grafos Densos:** com grande número de arestas (quase completos)
- **Grafos Esparsos:** com poucas conexões entre os vértices

O desempenho e a precisão dos resultados foram comparados por meio de tabelas e gráficos, permitindo analisar a relação entre tempo de execução, número de vértices e estrutura do grafo.

## Autor

**Matheus Filipe Rocha Viana**  
Pontifícia Universidade Católica de Minas Gerais  
Disciplina: Algoritmos e Estruturas de Dados III
