public class RobotSimulado implements IRobotLegoEV3 {
    public RobotSimulado() {
    }
    @Override
    public boolean OpenEV3(String s) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return true;
    }

    @Override
    public void CloseEV3() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void Reta(int d) {
        try {
            long t = TempoMovimento.tempoReta(d);
            Thread.sleep(t);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void CurvarEsquerda(int raio, int angulo) {
        try {
            long t = TempoMovimento.tempoCurvar(raio, angulo);
            Thread.sleep(t);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void CurvarDireita(int raio, int angulo) {
        try {
            long t = TempoMovimento.tempoCurvar(raio, angulo);
            Thread.sleep(t);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void Parar(boolean p) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public int SensorToque(int toque) {
        return 0;
    }


}
