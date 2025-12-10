import javax.swing.*;

/**
 * Interface base para dados que podem ser usados com MovAleatorios e EvitarObstaculos.
 */
public interface DadosAppInterface {
    ConsolaPrinter getConsolaPrinter();
    void print(String mensagem);
    MyRobotSync getRobot();
    boolean isOnOff();
    JSpinner getNumSpinner();
    MovAleatorios getMovAleatorios();
    void registrarComandos(Comando c);
}
