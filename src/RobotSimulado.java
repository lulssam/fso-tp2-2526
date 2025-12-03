public class RobotSimulado implements IRobotLegoEV3 {
    public RobotSimulado() {
    }

    @Override
    public boolean OpenEV3(String s) {
        System.out.println(s);
        try {
            Thread.sleep(100);
            System.out.println("WAIT 100ms");
        } catch (InterruptedException e) {
        }
        return true;
    }

    @Override
    public void CloseEV3() {
        System.out.println("Close EV3");
        try {
            Thread.sleep(100);
            System.out.println("WAIT 100ms");
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void Reta(int d) {
        System.out.println("Reta (" + d + ")");
        try {
            long t = TempoMovimento.tempoReta(d);
            Thread.sleep(t);
            System.out.println("WAIT " + t + " ms");
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void CurvarEsquerda(int raio, int angulo) {
        System.out.println("Curvar Esquerda (raio: " + raio + " angulo: " + angulo + ")");
        try {
            long t = TempoMovimento.tempoCurvar(raio, angulo);
            Thread.sleep(t);
            System.out.println("WAIT " + t + " ms");
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void CurvarDireita(int raio, int angulo) {
        System.out.println("Curvar Direita (raio: " + raio + " angulo: " + angulo + ")");
        try {
            long t = TempoMovimento.tempoCurvar(raio, angulo);
            Thread.sleep(t);
            System.out.println("WAIT " + t + " ms");
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void Parar(boolean p) {
        System.out.println("Parar (" + p + ")");
        try {
            Thread.sleep(100);
            System.out.println("WAIT 100ms");
        } catch (InterruptedException e) {
        }
    }

    @Override
    public int SensorToque(int toque) {
        System.out.println("SensorToque (" + toque + ")");
        return 0;
    }


}
