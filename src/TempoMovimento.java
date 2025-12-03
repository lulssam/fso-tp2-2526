public final class TempoMovimento {
    private static double VEL_ROBOT_CM_POR_MS = 0.02;
    private static int TEMPO_COMUNICACAO_MS = 100;

    private TempoMovimento() {}

    public static long tempoReta(int distanciaCm) {
        return (long) (Math.abs(distanciaCm) / VEL_ROBOT_CM_POR_MS + TEMPO_COMUNICACAO_MS);
    }

    public static long tempoCurvar(int raioCm, int anguloGraus) {
        double comprimentoArco = raioCm * Math.toRadians(anguloGraus);
        return (long) (comprimentoArco / VEL_ROBOT_CM_POR_MS + TEMPO_COMUNICACAO_MS);
    }

    public static double getVelRobot() { return VEL_ROBOT_CM_POR_MS; }
    public static void setVelRobot(double velRobotCmPorMs) { VEL_ROBOT_CM_POR_MS = velRobotCmPorMs; }

    public static int getTempoComunicacao() { return TEMPO_COMUNICACAO_MS; }
    public static void setTempoComunicacao(int tempoMs) { TEMPO_COMUNICACAO_MS = tempoMs; }
}