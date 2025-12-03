import java.util.Random;

/**
 * MovAleatorios: gera ciclos contínuos de N comandos aleatórios
 * enquanto estiver ativado (estado == TRABALHAR).
 * Preempção por EvitarObstaculos: interrupt() faz terminar o comando atual
 * e o ciclo retoma automaticamente.
 */
public class MovAleatorios extends Tarefa {

    private final Dados dados;
    private final Random random = new Random();

    public MovAleatorios(Dados dados) {
        this.dados = dados;
    }

    @Override
    void trabalharAtividade() throws InterruptedException {
        dados.getGui().print("Movimentos Aleatorios: ativo (modo contínuo).");

        // Loop contínuo enquanto a tarefa estiver ativa
        while (estado == Estados.TRABALHAR) {

            // Ler valor atual do spinner (pode mudar dinamicamente)
            int num;
            try {
                num = (int) dados.getGui().getNumSpinner().getValue();
            } catch (Exception e) {
                num = 0;
            }

            if (num <= 0) {
                dados.getGui().print("Movimentos Aleatorios: número inválido (" + num + ")");
                // Esperar um pouco para não consumir CPU
                Thread.sleep(250);
                // Continua o while para tentar novamente (se o utilizador mudar)
                continue;
            }

            dados.getGui().print("Movimentos Aleatorios: novo lote de " + num + " comandos.");

            for (int i = 0; i < num && estado == Estados.TRABALHAR; i++) {

                // Verificação rápida antes de iniciar comando
                if (Thread.interrupted()) {
                    dados.getGui().print("Movimentos Aleatorios: interrupção antes do comando " + (i + 1));
                    // Limpa sinal e sai do lote (volta ao while para retomar)
                    Thread.interrupted(); // limpa flag
                    break;
                }

                int movimento = random.nextInt(3); // 0=RETA 1=DIREITA 2=ESQUERDA
                int distancia, raio, angulo;
                long tempoExec;

                switch (movimento) {
                    case 0: // RETA
                        distancia = randomIntervalo(10, 50);
                        dados.getGui().print((i + 1) + ": Aleatório RETA " + distancia + " cm");
                        // Exclusão mútua por comando
                        dados.getRobot().entrar();
                        try {
                            dados.getRobot().Reta(distancia);
                        } finally {
                            dados.getRobot().sair();
                        }
                        tempoExec = TempoMovimento.tempoReta(distancia);
                        break;

                    case 1: // DIREITA
                        raio = randomIntervalo(10, 30);
                        angulo = randomIntervalo(20, 90);
                        dados.getGui().print((i + 1) + ": Aleatório DIREITA raio=" + raio + " ang=" + angulo);
                        dados.getRobot().entrar();
                        try {
                            dados.getRobot().CurvarDireita(raio, angulo);
                        } finally {
                            dados.getRobot().sair();
                        }
                        tempoExec = TempoMovimento.tempoCurvar(raio, angulo);
                        break;

                    case 2: // ESQUERDA
                        raio = randomIntervalo(10, 30);
                        angulo = randomIntervalo(20, 90);
                        dados.getGui().print((i + 1) + ": Aleatório ESQUERDA raio=" + raio + " ang=" + angulo);
                        dados.getRobot().entrar();
                        try {
                            dados.getRobot().CurvarEsquerda(raio, angulo);
                        } finally {
                            dados.getRobot().sair();
                        }
                        tempoExec = TempoMovimento.tempoCurvar(raio, angulo);
                        break;

                    default:
                        tempoExec = 0;
                }

                dados.getGui().print("Espera: " + tempoExec + " ms");
                try {
                    Thread.sleep(tempoExec);
                } catch (InterruptedException e) {
                    dados.getGui().print("Movimentos Aleatorios: comando interrompido durante espera");
                    // Limpar flag de interrupção para retomar próximo ciclo
                    Thread.interrupted();
                    // Deixa o for terminar, volta ao while para iniciar novo lote
                    break;
                }
            }

            // Paragem suave entre lotes: enfileirar Parar(false) se quiseres
            dados.getRobot().Parar(false);
            dados.getGui().print("Movimentos Aleatorios: lote concluído. Próximo lote irá iniciar se ainda ativo.");

            // Pequena pausa entre lotes para não monopolizar CPU
            Thread.sleep(150);

            // Se for interrompido aqui, retoma no topo do while
            if (Thread.interrupted()) {
                dados.getGui().print("Movimentos Aleatorios: interrupção pós-lote");
                Thread.interrupted(); // limpa flag
            }
        }

        dados.getGui().print("Movimentos Aleatorios: desativado.");
    }

    private int randomIntervalo(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}