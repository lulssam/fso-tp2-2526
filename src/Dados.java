import java.util.ArrayList;

public class Dados {
    private String nomeRobot;
    private GUI gui;
    private GUI_Gravador guiGravador;
    private ArrayList<String> texto;
    private int raio, angulo, distancia;
    private boolean onOff;
    private MyRobotSync robot; // MUDAR PARA ROBOTSYNC

    private EvitarObstaculos evitarObstaculos;
    private MovAleatorios movAleatorios;
    private Gravador gravador;

    public Dados(GUI gui, GUI_Gravador guiGravador) {
        nomeRobot = "EV8";
        this.gui = gui;
        this.guiGravador = guiGravador;
        texto = new ArrayList<String>();
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

    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public ArrayList<String> getTexto() {
        return texto;
    }

    public void setTexto(ArrayList<String> texto) {
        this.texto = texto;
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

    public GUI_Gravador getGuiGravador() {
        return guiGravador;
    }

    public void setGravador(Gravador gravador) {
        this.gravador = gravador;
    }

    /**
     * Método para encaminhar os comandos para o gravdor quando acionarmos o modo*/
    public void registarComandos(Comando c) {
        if (gravador != null) {
            gravador.gravar(c);
        }
    }
}