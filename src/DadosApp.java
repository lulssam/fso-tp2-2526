import javax.swing.*;

/**
 * Dados específicos da GUI principal (com tarefas MovAleatorios e EvitarObstaculos).
 */
public class DadosApp implements DadosAppInterface {
    private String nomeRobot;
    private ConsolaPrinter consolaPrinter;
    private int raio, angulo, distancia;
    private boolean onOff;
    private MyRobotSync robot;
    private JSpinner numSpinner;

    private EvitarObstaculos evitarObstaculos;
    private MovAleatorios movAleatorios;

    public DadosApp(ConsolaPrinter consolaPrinter, JSpinner numSpinner) {
        nomeRobot = "EV8";
        this.consolaPrinter = consolaPrinter;
        this.numSpinner = numSpinner;
        raio = 20;
        angulo = 90;
        distancia = 33;
        onOff = false;
        
        // Obter robot do gestor
        robot = RobotManager.getInstance().getRobot(nomeRobot);

        // tarefa mov aleatorios
        movAleatorios = new MovAleatorios(this);
        movAleatorios.start();

        // tarefa evitar obstaculos
        evitarObstaculos = new EvitarObstaculos(this);
        evitarObstaculos.start();
    }

    public MovAleatorios getMovAleatorios() {
        return movAleatorios;
    }

    public EvitarObstaculos getEvitarObstaculos() {
        return evitarObstaculos;
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

    public JSpinner getNumSpinner() {
        return numSpinner;
    }

    /**
     * Método para encaminhar os comandos (não usado na GUIApp, mas necessário para a interface)
     */
    public void registrarComandos(Comando c) {
        // Na GUI principal, não gravamos comandos aqui
        // Isso é feito no GUIGravador
    }
}
