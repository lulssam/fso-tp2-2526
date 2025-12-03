public interface IRobotLegoEV3 {
    boolean OpenEV3(String s);
    void CloseEV3();
    void Reta(int d);
    void CurvarEsquerda(int raio, int angulo);
    void CurvarDireita(int raio, int angulo);
    void Parar(boolean p);
    int SensorToque(int toque);
}
