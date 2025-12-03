/**
 * Classe servidor do RobotLegoEV3
 * CONSUMIDOR!!
 */
public class MyRobotLegoEV3 extends Thread implements IRobotLegoEV3 {

    private static double VEL_ROBOT = 0.02; // read only, fundamental para esperar o tempo
    private static int TEMPO_COMUNICACAO = 100;
    private BufferCircularComando buffer;
    private IRobotLegoEV3 robot;
    private volatile boolean stopRequested = false;

    public MyRobotLegoEV3() {
        robot = new CRobotLegoEV3();
        buffer = new BufferCircularComando();
    }

    public static double getVelRobot() {
        return VEL_ROBOT;
    }

    public static int getTempoComunicacao() {
        return TEMPO_COMUNICACAO;
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
            this.interrupt();
            buffer.limparBuffer();
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

    private void pararImediatamente() {
        if (stopRequested || Thread.currentThread().isInterrupted()) {
            robot.Parar(true);
            buffer.limparBuffer();
            stopRequested = false;
            Thread.interrupted();
        }
    }

    /**
     * Ler do comando, enviar para o robot, e esperar pelo tempo de execução de forma
     * interrompível para permitir PARAR imediato.
     */
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Comando comando = buffer.removerElemento();
            if (comando == null) {
                pararImediatamente();
                continue;
            }
            try {
                long t = comando.executarComandos(robot);
                if (t > 0) Thread.sleep(t);
            } catch (InterruptedException e) {
                pararImediatamente();
            }
        }
        robot.Parar(true);
    }

    public IRobotLegoEV3 getRobot() {
        return robot;
    }

    public void setRobot(IRobotLegoEV3 robot) {
        this.robot = robot;
    }
}