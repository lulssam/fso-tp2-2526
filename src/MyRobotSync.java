import java.util.concurrent.Semaphore;

public class MyRobotSync implements IRobotLegoEV3 {
    private Semaphore acesso;
    private MyRobotLegoEV3 robot;

    public MyRobotSync() {
        acesso = new Semaphore(1);
        robot = new MyRobotLegoEV3();
        robot.setSemaphore(acesso);
    }

    @Override
    public boolean OpenEV3(String s) {
        return robot.OpenEV3(s);
    }

    @Override
    public void CloseEV3() {
        robot.CloseEV3();
    }

    @Override
    public void Reta(int d) {
        robot.Reta(d);
    }

    @Override
    public void CurvarEsquerda(int raio, int angulo) {
        robot.CurvarEsquerda(raio, angulo);
    }

    @Override
    public void CurvarDireita(int raio, int angulo) {
        robot.CurvarDireita(raio, angulo);
    }

    @Override
    public void Parar(boolean p) {
        robot.Parar(p);
    }

    @Override
    public int SensorToque(int toque) {
        return robot.SensorToque(toque);
    }

    public IRobotLegoEV3 getRobot() {
        return robot;
    }

    public void setRobot(IRobotLegoEV3 robot) {
        this.robot.setRobot(robot);
    }

    public void entrar() throws InterruptedException {
        acesso.acquire();
    }

    public void sair() {
        acesso.release();
    }

    public void start() {
        robot.start();
    }

    public void stop() {
        robot.interrupt();
    }
}