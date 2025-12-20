import java.util.Random;

public class MovAleatorios extends Tarefa {

    private final Dados dados;
    private final Random random = new Random();

    public MovAleatorios(Dados dados) {
        this.dados = dados;
    }

    @Override
    void trabalharAtividade() throws InterruptedException {
        dados.getGui().print("Movimentos Aleatorios: Iniciado.");
        int num;
        try {
            num = (int) dados.getGui().getNumSpinner().getValue();
        } catch (Exception e) {
            num = 0;
        }

        // tratamento de erros
        if (num <= 0) {
            dados.getGui().print("Movimentos Aleatorios: número inválido (" + num + ")");
            estado = Estados.DORMIR;
            return;
        }

        dados.getGui().print("Movimentos Aleatorios: novo lote de " + num + " comandos.");
        boolean interrompido = false;

        for (int i = 0; i < num && estado == Estados.TRABALHAR; i++) {
            // verificação antes de cada comando
            if (Thread.interrupted()) {
                dados.getGui().print("Movimentos Aleatorios: interrupção antes do comando " + (i + 1));
                interrompido = true;
                estado = Estados.DORMIR;
                break;
            }

            int movimento = random.nextInt(3); // reta = 0, dir = 1, esq = 2
            int distancia, raio, angulo;
            long tempo;

            switch (movimento) {
                case 0: // RETA
                    distancia = randoms(10, 50);
                    dados.getGui().print((i + 1) + ": Comando Aleatório: RETA " + distancia + " cm");
                    // exclusão mútua por comando
                    dados.getRobot().entrar();
                    try {
                        dados.getRobot().Reta(distancia);
                        dados.registarComandos(new Comando(Comando.ID_DIREITA, distancia));
                    } finally {
                        dados.getRobot().sair();
                    }
                    tempo = TempoMovimento.tempoReta(distancia);
                    break;

                case 1: // DIREITA
                    raio = randoms(10, 30);
                    angulo = randoms(20, 90);
                    dados.getGui().print((i + 1) + ": Comando Aleatório DIREITA raio=" + raio + " ang=" + angulo);
                    dados.getRobot().entrar();
                    try {
                        dados.getRobot().CurvarDireita(raio, angulo);
                        dados.registarComandos(new Comando(Comando.ID_DIREITA, raio, angulo));
                    } finally {
                        dados.getRobot().sair();
                    }
                    tempo = TempoMovimento.tempoCurvar(raio, angulo);
                    break;

                case 2: // ESQUERDA
                    raio = randoms(10, 30);
                    angulo = randoms(20, 90);
                    dados.getGui().print((i + 1) + ": Comando Aleatório ESQUERDA raio=" + raio + " ang=" + angulo);
                    dados.getRobot().entrar();
                    try {
                        dados.getRobot().CurvarEsquerda(raio, angulo);
                        dados.registarComandos(new Comando(Comando.ID_ESQUERDA, raio, angulo));
                    } finally {
                        dados.getRobot().sair();
                    }
                    tempo = TempoMovimento.tempoCurvar(raio, angulo);
                    break;

                default:
                    tempo = 0;
            }

            dados.getGui().print("Espera: " + tempo + " ms");
            try {
                Thread.sleep(tempo);
            } catch (InterruptedException e) {
                dados.getGui().print("Movimentos Aleatorios: comando interrompido durante espera");
                interrompido = true;
                break;
            }
        }

        if (!interrompido) {
        //dados.getRobot().Parar(false);
        dados.getGui().print("Movimentos Aleatorios: lote concluído.");
        dados.getRobot().Parar(false);
        Thread.sleep(3000);

        } else {
            dados.getGui().print("Movimentos Aleatorios: lote interrompido");
        }
        if (interrompido) {
            Thread.interrupted();
        }

        dados.getGui().print("Movimentos Aleatorios: desativado.");

    }


    private int randoms(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}