/**
 * Dados específicos do gravador (sem tarefas MovAleatorios e EvitarObstaculos).
 */
public class DadosGravador implements DadosGravadorInterface {
    private String nomeRobot;
    private ConsolaPrinter consolaPrinter;
    private int raio, angulo, distancia;
    private boolean onOff;
    private MyRobotSync robot;

    private Gravador gravador;

    public DadosGravador(ConsolaPrinter consolaPrinter) {
        nomeRobot = "EV8";
        this.consolaPrinter = consolaPrinter;
        raio = 20;
        angulo = 90;
        distancia = 33;
        onOff = false;
        
        // Obter robot do gestor
        robot = RobotManager.getInstance().getRobot(nomeRobot);

        // tarefa gravador
        gravador = new Gravador(this);
        gravador.start();
    }

    public String getNomeRobot() {
        return nomeRobot;
    }

    public void setNomeRobot(String nomeRobot) {
        this.nomeRobot = nomeRobot;
        // Atualizar robot quando o nome muda
        robot = RobotManager.getInstance().getRobot(nomeRobot);
    }

    public ConsolaPrinter getConsolaPrinter() {
        return consolaPrinter;
    }

    public void print(String mensagem) {
        if (consolaPrinter != null) {
            consolaPrinter.print(mensagem);
        }
    }

    public int getRaio() {
        return raio;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }

    public int getAngulo() {
        return angulo;
    }

    public void setAngulo(int angulo) {
        this.angulo = angulo;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }

    public MyRobotSync getRobot() {
        return robot;
    }

    public Gravador getGravador() {
        return gravador;
    }

    /**
     * Método para encaminhar os comandos para o gravador quando acionarmos o modo
     */
    public void registrarComandos(Comando c) {
        if (gravador != null) {
            gravador.gravar(c);
        }
    }
}
