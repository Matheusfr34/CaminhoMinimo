import subprocess
import time
import pandas as pd
import matplotlib.pyplot as plt

instancias_densas = [
    ("grafo_denso_5.txt", 5, 10, (1, 5)),
    ("grafo_denso_10.txt", 10, 45, (1, 10)),
    ("grafo_denso_15.txt", 15, 105, (1, 15)),
    ("grafo_denso_20.txt", 20, 190, (1, 20)),
]

instancias_esparsas = [
    ("grafo_esparso_5.txt", 5, 4, (1, 5)),
    ("grafo_esparso_10.txt", 10, 9, (1, 10)),
    ("grafo_esparso_15.txt", 15, 14, (1, 15)),
    ("grafo_esparso_20.txt", 20, 19, (1, 20)),
]


def testar_instancias(tipo, instancias):
    resultados = []

    for nome, v, a, (origem, destino) in instancias:
        print(f"\n🔹 Testando {tipo}: {nome}")

        tempos = []
        distancia = None
        arestas = None

        for _ in range(3):  # roda 3x para média
            start = time.time()

            proc = subprocess.run(
                ["java", "Main"],
                input=f"{nome}\n{origem}\n{destino}\n",
                text=True,
                capture_output=True
            )

            end = time.time()
            tempos.append((end - start) * 1000)

            saida = proc.stdout
            for linha in saida.splitlines():
                if "Distância total" in linha:
                    distancia = int(linha.split(":")[1].strip())
                if "Número de arestas" in linha:
                    arestas = int(linha.split(":")[1].strip())

        media_tempo = sum(tempos) / len(tempos)
        resultados.append({
            "Arquivo": nome,
            "Vértices": v,
            "Arestas": a,
            "Origem-Destino": f"{origem}->{destino}",
            "Distância Mínima": distancia,
            "Nº de Arestas no Caminho": arestas,
            "Tempo Médio (ms)": round(media_tempo, 2)
        })

    df = pd.DataFrame(resultados)
    print(f"\nTabela {tipo}:\n", df, "\n")

    # Salva tabela em CSV
    df.to_csv(f"tabela_{tipo.lower()}.csv", index=False)

    # Gera gráfico
    plt.plot(df["Vértices"], df["Tempo Médio (ms)"], marker='o')
    plt.title(f"Tempo de Execução x Nº de Vértices ({tipo})")
    plt.xlabel("Número de Vértices")
    plt.ylabel("Tempo Médio (ms)")
    plt.grid(True)
    plt.savefig(f"grafico_{tipo.lower()}.png")
    plt.close()

    return df


if __name__ == "__main__":
    df_denso = testar_instancias("Denso", instancias_densas)
    df_esparso = testar_instancias("Esparso", instancias_esparsas)

    # Gera resumo em Excel com ambas as tabelas
    with pd.ExcelWriter("resultados_completos.xlsx") as writer:
        df_denso.to_excel(writer, sheet_name="Grafo Denso", index=False)
        df_esparso.to_excel(writer, sheet_name="Grafo Esparso", index=False)

    print("====Resultados gerados====")
    print(" - tabela_denso.csv / tabela_esparso.csv")
    print(" - grafico_denso.png / grafico_esparso.png")
    print(" - resultados_completos.xlsx")
