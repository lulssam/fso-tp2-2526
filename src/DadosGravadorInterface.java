/**
 * Interface base para dados que podem ser usados com o Gravador.
 */
public interface DadosGravadorInterface {
    ConsolaPrinter getConsolaPrinter();
    void print(String mensagem);
    MyRobotSync getRobot();
    boolean isOnOff();
}
