import javax.swing.*;

/**
 * Dados legacy - mantido para compatibilidade mas não usado nas novas GUIs.
 * Use DadosApp ou DadosGravador ao invés.
 */
public class Dados implements DadosAppInterface, DadosGravadorInterface {
    private String nomeRobot;
    private ConsolaPrinter consolaPrinter;
    private int raio, angulo, distancia;
    private boolean onOff;
    private MyRobotSync robot;
    private JSpinner numSpinner;

    private EvitarObstaculos evitarObstaculos;
    private MovAleatorios movAleatorios;
    private Gravador gravador;

    public Dados(ConsolaPrinter consolaPrinter) {
        nomeRobot = "EV8";
        this.consolaPrinter = consolaPrinter;
        raio = 20;
        angulo = 90;
        distancia = 33;
        onOff = false;
        robot = new MyRobotSync();

        // tarefa mov aleatorios
        movAleatorios = new MovAleatorios(this);
        movAleatorios.start();

        // tarefa evitar obstaculos
        evitarObstaculos = new EvitarObstaculos(this);
        evitarObstaculos.start();

        // tarefa gravador
        gravador = new Gravador(this);
        gravador.start();
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

    public void setRobot(MyRobotSync robot) {
        this.robot = robot;
    }

    public Gravador getGravador() {
        return gravador;
    }

    public void setGravador(Gravador gravador) {
        this.gravador = gravador;
    }

    public JSpinner getNumSpinner() {
        return numSpinner;
    }

    public void setNumSpinner(JSpinner numSpinner) {
        this.numSpinner = numSpinner;
    }

    /**
     * Método para encaminhar os comandos para o gravdor quando acionarmos o modo*/
    public void registrarComandos(Comando c) {
        if (gravador != null) {
            gravador.gravar(c);
        }
    }
}