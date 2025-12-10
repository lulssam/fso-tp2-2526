import java.util.Random;

/**
 * A tarefa “Evitar Obstáculo” deve de estar a testar o sensor de toque de 50 em 50 ms, e
 * quando este ficar ativo deve imediatamente parar o robot, depois fazer recuar o robot de 20
 * cm, e depois fazer uma curva à esquerda ou à direita (com escolha aleatória) de 90 graus.
 * Durante estas ações nem o utilizador nem as outras tarefas devem poder movimentar o robot.
 */
public class EvitarObstaculos extends Tarefa {
    private final int ESPERA_SENSOR = 50;
    private DadosAppInterface dados;
    private MyRobotSync robot;
    private Random random;

    public EvitarObstaculos(DadosAppInterface dados) {
        this.dados = dados;
        random = new Random();
        robot = dados.getRobot();
    }

    @Override
    void trabalharAtividade() throws InterruptedException {
        int toque;
        try {
            dados.getRobot().entrar();
            toque = robot.SensorToque(RobotLegoEV3.S_1);
        } finally {
            dados.getRobot().sair();
        }
        switch (toque) {
            case 0:
                Thread.sleep(ESPERA_SENSOR);
                break;
            case 1:

                dados.print("Evitar Obstáculos: Obstáculo Detectado");

                // se os mov aleatorios estiverem ATIVOS e a FUNCIONAR
                if (dados.getMovAleatorios() != null && dados.getMovAleatorios().estado == Estados.TRABALHAR) {
                    dados.print("Evitar Obstáculos: Interromper Movimentos Aleatórios");
                    dados.getMovAleatorios().interrupt(); // interromper
                }
                try {
                    dados.getRobot().entrar();
                    dados.print("Evitar Obstáculos: Semáforo adquirido");

                    dados.getRobot().Parar(true); // parar imediatamente
                    dados.print("Evitar Obstaculos: Parar.");

                    Thread.sleep(TempoMovimento.getTempoComunicacao());

                    // reta
                    int distancia = -20;
                    dados.getRobot().Reta(distancia);
                    dados.registrarComandos(new Comando(Comando.ID_RETA, distancia));
                    dados.print("Evitar Obstáculos: Reta com distância: " + distancia);
                    Thread.sleep(TempoMovimento.tempoReta(distancia));

                    int escolha = random();

                    switch (escolha) {
                        // esquerda
                        case 0:
                            dados.getRobot().CurvarEsquerda(0, 90);
                            dados.registrarComandos(new Comando(Comando.ID_ESQUERDA, 0, 90));
                            dados.print("Evitar Obstáculos: Curva Esquerda com raio: 0, e angulo: 90º. ");
                            Thread.sleep(TempoMovimento.tempoCurvar(0, 90));

                            break;
                        case 1:
                            dados.getRobot().CurvarDireita(0, 90);
                            dados.registrarComandos(new Comando(Comando.ID_DIREITA, 0, 90));
                            dados.print("Evitar Obstáculos: Curva Direita com raio: 0, e angulo: 90º. ");
                            Thread.sleep(TempoMovimento.tempoCurvar(0, 90));

                            break;

                    }
                    dados.getRobot().Parar(false);
                    break;

                } finally {
                    dados.getRobot().sair();
                    dados.print("Evitar Obstáculos: Semaforo Liberado");
                }


        }

        Thread.sleep(ESPERA_SENSOR);
    }

    private int random() {
        return random.nextInt(2);
    }

}