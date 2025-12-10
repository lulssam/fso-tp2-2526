import java.util.HashMap;
import java.util.Map;

/**
 * Gestor de robots que permite partilhar ou criar robots independentes.
 * Se duas GUIs usarem o mesmo nome de robot, partilham a mesma instância.
 * Se usarem nomes diferentes, cada GUI tem o seu robot independente.
 */
public class RobotManager {
    private static RobotManager instance;
    private Map<String, MyRobotSync> robots;

    private RobotManager() {
        robots = new HashMap<>();
    }

    public static synchronized RobotManager getInstance() {
        if (instance == null) {
            instance = new RobotManager();
        }
        return instance;
    }

    /**
     * Obtém ou cria um robot com o nome especificado.
     * Se já existir um robot com esse nome, retorna a instância existente.
     * Caso contrário, cria um novo robot.
     */
    public synchronized MyRobotSync getRobot(String nomeRobot) {
        if (!robots.containsKey(nomeRobot)) {
            robots.put(nomeRobot, new MyRobotSync());
        }
        return robots.get(nomeRobot);
    }

    /**
     * Remove um robot do registo quando já não é necessário.
     */
    public synchronized void removeRobot(String nomeRobot) {
        robots.remove(nomeRobot);
    }

    /**
     * Verifica se existe um robot com o nome especificado.
     */
    public synchronized boolean hasRobot(String nomeRobot) {
        return robots.containsKey(nomeRobot);
    }
}
