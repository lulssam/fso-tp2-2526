import java.util.concurrent.Semaphore;

/**
 * Classe servidor do RobotLegoEV3
 * CONSUMIDOR!!
 */
public class MyRobotLegoEV3 extends Tarefa implements IRobotLegoEV3 {

    private static double VEL_ROBOT = 0.02; // read only, fundamental para esperar o tempo
    private static int TEMPO_COMUNICACAO = 100;
    private BufferCircularComando buffer;
    private IRobotLegoEV3 robot;
    private volatile boolean stopRequested;
    private Semaphore semaphore;


    public MyRobotLegoEV3() {
        super();
        robot = new RobotSimulado();
        buffer = new BufferCircularComando();
        stopRequested = false;

    }

    @Override
    void trabalharAtividade() {
        Comando comando = buffer.removerElemento();

        if (comando == null) return;

        stopRequested = false;
        long tempoEspera = 0;

        switch (comando.getIdentificador()) {
            case Comando.ID_RETA:
                int d = comando.getArg1();
                robot.Reta(d);
                tempoEspera = TempoMovimento.tempoReta(d);
                break;
            case Comando.ID_DIREITA:
                int raioD = comando.getArg1();
                int anguloD = comando.getArg2();
                robot.CurvarDireita(raioD, anguloD);
                tempoEspera = TempoMovimento.tempoCurvar(raioD, anguloD);
                break;
            case Comando.ID_ESQUERDA:
                int raioE = comando.getArg1();
                int anguloE = comando.getArg2();
                robot.CurvarEsquerda(raioE, anguloE);
                tempoEspera = TempoMovimento.tempoCurvar(raioE, anguloE);
                break;
            case Comando.ID_PARAR:
                robot.Parar(true);
                tempoEspera = TEMPO_COMUNICACAO;
                break;

        }

        if (tempoEspera > 0) {
            espera(tempoEspera);
        }
    }

    private void espera(long tempoTotal) {
        long tempoDecorrido = 0;
        int parte = 10; // verificação a cada 10ms

        while (tempoDecorrido < tempoTotal) {
            if (stopRequested || estado != Estados.TRABALHAR) break;
            try {
                Thread.sleep(parte);
            } catch (InterruptedException e) {
                break;
            }
            tempoDecorrido += parte;
        }

    }

    public void Reta(int distancia) {
        Comando c = new Comando(Comando.ID_RETA, distancia);
        buffer.inserirElemento(c);
    }

    public void CurvarDireita(int raio, int angulo) {
        Comando c = new Comando(Comando.ID_DIREITA, raio, angulo);
        buffer.inserirElemento(c);
    }

    public void CurvarEsquerda(int raio, int angulo) {
        Comando c = new Comando(Comando.ID_ESQUERDA, raio, angulo);
        buffer.inserirElemento(c);

    }

    public void Parar(boolean parar) {
        if (parar) {
            stopRequested = true;
            buffer.limparBuffer();
            robot.Parar(true);
        } else {
            Comando c = new Comando(Comando.ID_PARAR, 0);
            buffer.inserirElemento(c);
        }
    }

    @Override
    public int SensorToque(int toque) {
        return robot.SensorToque(toque);
    }

    public boolean OpenEV3(String s) {
        return robot.OpenEV3(s);
    }

    public void CloseEV3() {
        robot.CloseEV3();
    }

    public void setSemaphore(Semaphore s) {
        this.semaphore = s;
    }

    /**
     * Ler do comando, enviar para o robot, e esperar pelo tempo de execução de forma
     * interrompível para permitir PARAR imediato.
     */
    /*public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Comando comando = buffer.removerElemento();
            if (comando == null) {
                pararImediatamente();
                continue;
            }
            try {
                if (semaphore != null) {
                    semaphore.acquire();
                }
                long t;
                try {
                    t = comando.executarComandos(robot);
                } finally {
                    if (semaphore != null) semaphore.release();
                }
                if (t > 0) Thread.sleep(t);
            } catch (InterruptedException e) {
                pararImediatamente();
            }
        }
        try {
            if (semaphore != null) semaphore.acquire();
            robot.Parar(true);
        } catch (InterruptedException e) {

        } finally {
            if (semaphore != null) semaphore.release();
        }
    }
*/
    public IRobotLegoEV3 getRobot() {
        return robot;
    }

    public void setRobot(IRobotLegoEV3 robot) {
        this.robot = robot;
    }
}