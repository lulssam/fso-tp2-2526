# FSO 2025/26 — 2º Trabalho Prático

Controlo concorrente de um robot **Lego EV3** em Java, com várias tarefas (*threads*) a partilhar o mesmo robot: movimentos aleatórios, evitar obstáculos e um gravador/reprodutor de comandos. A aplicação funciona com o robot real (via Bluetooth) ou com um robot simulado.

Trabalho da unidade curricular de **Fundamentos de Sistemas Operativos** da Licenciatura em Engenharia Informática e Multimédia (LEIM) do ISEL.

- Mª Luísa Melo e Sampaio, nº 50799
- Carlos Ceita, nº 50950

Grupo 08. Docente: Prof. Eng.º Jorge Pais.

O relatório completo está em [`TP2_A50950_A50799.pdf`](TP2_A50950_A50799.pdf).

## Funcionalidades

**GUI Principal**
- Ligar/desligar o robot pelo nome (por omissão, `EV8`).
- Comandos manuais: `FRENTE`, `TRÁS`, `ESQUERDA`, `DIREITA` e `PARAR`, com raio, ângulo e distância configuráveis.
- Alternar entre **robot real** (`CRobotLegoEV3`) e **simulado** (`RobotSimulado`).
- **Movimentos Aleatórios**: executa um lote de *N* comandos aleatórios (retas de 10–50 cm; curvas de raio 10–30 cm e ângulo 20–90°).
- **Evitar Obstáculos**: lê o sensor de toque de 50 em 50 ms. Quando deteta um obstáculo, para o robot, recua 20 cm e roda 90° para um lado escolhido aleatoriamente. Durante esta manobra, nem o utilizador nem as outras tarefas conseguem mexer o robot.
- Consola de *log*.

**GUI Gravador**
- **Gravar**: guarda num ficheiro de texto todos os comandos enviados ao robot.
- **Reproduzir**: lê o ficheiro e reexecuta os comandos.
- Pode usar o mesmo robot da GUI principal (partilhado) ou ligar-se a outro robot.

Formato do ficheiro de gravação (ver [`comandos.txt`](comandos.txt)):

```
RETA <distância>
DIREITA <raio> <ângulo>
ESQUERDA <raio> <ângulo>
PARAR <0|1>
```

## Arquitetura

```
GUI / GUI_Gravador ──► MyRobotSync ──► MyRobotLegoEV3 ──► BufferCircularComando ──► IRobotLegoEV3
(produtores)           (exclusão       (consumidor,       (16 posições,              ├─ CRobotLegoEV3 (real)
MovAleatorios          mútua)           thread própria)    semáforos)                └─ RobotSimulado
EvitarObstaculos
Gravador
```

| Classe | Papel |
|---|---|
| `App` | Ponto de entrada. Cria as duas GUIs e o objeto `Dados`. |
| `Dados` | Estado partilhado (robot, parâmetros, referências às tarefas e GUIs). Arranca as tarefas. |
| `Tarefa` | Classe abstrata (`extends Thread`) com a máquina de estados `DORMIR` / `TRABALHAR` / `TERMINAR`. A tarefa adormece num semáforo e acorda com `ativar()`. |
| `MovAleatorios`, `EvitarObstaculos`, `Gravador` | Tarefas concretas: cada uma implementa `trabalharAtividade()`. |
| `MyRobotSync` | Envolve o robot com um semáforo de acesso (`entrar()` / `sair()`) para garantir exclusão mútua entre tarefas. |
| `MyRobotLegoEV3` | Servidor do robot. Os métodos de movimento só inserem comandos no buffer, e a thread consumidora executa-os e espera o tempo de cada movimento. `Parar(true)` limpa o buffer e interrompe a espera. |
| `BufferCircularComando` | Buffer circular produtor/consumidor com os semáforos `livres`, `ocupados` e `acesso`. |
| `Comando` | Representa um comando (`RETA`, `DIREITA`, `ESQUERDA`, `PARAR`) e os seus argumentos. |
| `TempoMovimento` | Estima a duração de cada movimento (velocidade de 0,02 cm/ms + 100 ms de comunicação). |
| `IRobotLegoEV3` | Interface comum aos robots real e simulado (polimorfismo). |
| `CRobotLegoEV3` | Adaptador para a classe `RobotLegoEV3` da biblioteca fornecida. |
| `RobotSimulado` | Robot falso que só espera o tempo de cada movimento. O sensor de toque devolve sempre 0. |

## Requisitos

- JDK 23. Foi o usado no projeto; versões mais recentes também devem funcionar.
- As bibliotecas fornecidas na cadeira, que **não estão no repositório**:
  - `RobotLegoEV3.jar`
  - `bluecove-2.1.1-SNAPSHOT.jar` (Bluetooth)

O projeto do IntelliJ (`.idea/libraries/RobotLegoEV3.xml`) espera encontrá-las em `../../../bibliotecas/`, relativamente à pasta do projeto. Põe-nas lá, ou corrige o caminho em *Project Structure → Libraries*.

## Como correr

**IntelliJ IDEA:** abre a pasta do projeto, confirma as bibliotecas e corre `App.main()`.

**Linha de comandos:**

```bash
# Linux/macOS (no Windows, troca ':' por ';')
LIBS=caminho/para/RobotLegoEV3.jar:caminho/para/bluecove-2.1.1-SNAPSHOT.jar
javac -cp "$LIBS" -d out src/*.java
java  -cp "out:$LIBS" App
```

Para testar sem robot, marca a caixa **Simulado/Real** antes de ligar. Quando está marcada, usa o robot simulado.

Mesmo para usar só o simulador, o `RobotLegoEV3.jar` tem de estar no *classpath* para compilar, porque `CRobotLegoEV3` e `EvitarObstaculos` dependem dele.
