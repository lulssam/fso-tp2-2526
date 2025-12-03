public class CRobotLegoEV3 implements IRobotLegoEV3{

    private RobotLegoEV3 robot;

    public CRobotLegoEV3() {
        this.robot = new RobotLegoEV3();
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
        return  robot.SensorToque(toque);
    }


}