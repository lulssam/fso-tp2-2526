public class Comando {
    public final static int ID_RETA = 0;
    public final static int ID_DIREITA = 1;
    public final static int ID_ESQUERDA = 2;
    public final static int ID_PARAR = 3;
    private int identificador; // identificador dos comandos
    private int arg1, arg2;
    private String descricao;

    public Comando(int identificador, int arg1, int arg2) {
        this.identificador = identificador;
        this.arg1 = arg1;
        this.arg2 = arg2;
        setDescricaoById();
    }

    public Comando(int identificador, int arg1) {
        this.identificador = identificador;
        this.arg1 = arg1;
        setDescricaoById();
    }

    public Comando(Comando c) { // construtor por cópia
        this.identificador = c.identificador;
        this.arg1 = c.arg1;
        this.arg2 = c.arg2;
        this.descricao = c.descricao;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    @Override
    public String toString() {
        return "Comando: " + descricao;
    }

    private void setDescricaoById() {
        switch (this.identificador) {
            case ID_RETA:
                this.descricao = "RETA(" + arg1 + ")";
                break;
            case ID_DIREITA:
                this.descricao = "DIREITA(raio=" + arg1 + ", angulo=" + arg2 + ")";
                break;
            case ID_ESQUERDA:
                this.descricao = "ESQUERDA(raio=" + arg1 + ", angulo=" + arg2 + ")";
                break;
            case ID_PARAR:
                this.descricao = "PARAR(" + (arg1 == 1 ? "true" : "false") + ")";
                break;
            default:
                this.descricao = "DESCONHECIDO";
                break;

        }
    }

    public long executarComandos(IRobotLegoEV3 robot) {
        switch (identificador) {
            case Comando.ID_RETA: {
                int d = arg1;
                robot.Reta(d);
                return TempoMovimento.tempoReta(d);
            }
            case Comando.ID_DIREITA: {
                int raioD = arg1;
                int anguloD = arg2;
                robot.CurvarDireita(raioD, anguloD);
                return TempoMovimento.tempoCurvar(raioD, anguloD);
            }
            case Comando.ID_ESQUERDA: {
                int raioE = arg1;
                int anguloE = arg2;
                robot.CurvarEsquerda(raioE, anguloE);
                return TempoMovimento.tempoCurvar(raioE, anguloE);
            }
            case Comando.ID_PARAR: {
                boolean imediato = (arg1 != 0);
                robot.Parar(imediato);
                return TempoMovimento.getTempoComunicacao();
            }
            default:
                return 0;
        }
    }
}