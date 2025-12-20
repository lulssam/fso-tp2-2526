import java.io.*;

/**
 * Esta tarefa tem a possibilidade de
 * trabalhar com o mesmo robot ou com um robot diferente das outras tarefas.
 * A manipulação do ficheiro deve obrigatoriamente ser efetuada com as classes de
 * InputStream, Reader, OutputStream e Writer do java.io.
 */
public class Gravador extends Tarefa {
    private Dados dados;

    // gravação
    private volatile boolean gravar;
    private String caminhoGravar;
    private OutputStream os;
    private Writer writer;

    // reprodução
    private volatile boolean reproduzir = false;
    private String caminhoReproduzir;

    public Gravador(Dados dados) {
        this.dados = dados;
        gravar = false;
    }

    public synchronized boolean isGravar() {
        return gravar;
    }

    public synchronized void iniciarGravar(String caminhoFicheiro) {
        // se estiver a gravar
        if (gravar) {
            dados.getGuiGravador().print("Gravador: Gravação ativa");
            return;
        }
        // tratamento de erros
        if (caminhoFicheiro == null || caminhoFicheiro.trim().isEmpty()) {
            dados.getGuiGravador().print("Gravador: Caminho do ficheiro inválido");
            return;
        }

        try {
            caminhoGravar = caminhoFicheiro;

            os = new FileOutputStream(caminhoGravar, false);
            writer = new OutputStreamWriter(os, "UTF-8");
            gravar = true;
            dados.getGuiGravador().print("Gravador: gravação iniciada em " + caminhoGravar);
        } catch (IOException e) {
            dados.getGuiGravador().print("Gravador: erro a iniciar gravação: " + e.getMessage());
            fecharWritter();
        }
    }

    public synchronized void pararGravacao() {
        if (!gravar) {
            dados.getGuiGravador().print("Gravador: não está a gravar");
            return;
        }
        fecharWritter();
        gravar = false;
        dados.getGuiGravador().print("Gravador: gravação acabada");


    }

    public synchronized void pararReproducao() {
        reproduzir = false;
        estado = Estados.DORMIR;
        this.interrupt();
        try {
            dados.getRobot().Parar(false);
        } catch (Exception ignore) {
        }
        dados.getGuiGravador().print("Gravador: reproduzir parado");
        dados.getGuiGravador().resetBotaoReprod();
    }

    public synchronized void gravar(Comando comando) {
        if (!gravar || writer == null || comando == null) return;
        try {
            String linha = encode(comando);
            writer.write(linha);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            dados.getGuiGravador().print("Gravador: erro a escrever comando: " + e.getMessage());
        }
    }

    public synchronized void reproduzir(String caminhoFicheiro) {
        if (reproduzir) {
            dados.getGuiGravador().print("Gravador: já está a reproduzir");
            return;
        }

        if (caminhoFicheiro == null || caminhoFicheiro.trim().isEmpty()) {
            dados.getGuiGravador().print("Gravador: caminho do ficheiro inválido");
            return;
        }

        // ativar tarefa para trabalhar
        this.caminhoReproduzir = caminhoFicheiro;
        this.reproduzir = true;
        ativar();
    }

    public boolean isReproduzir() {
        return reproduzir;
    }

    /**
     * Método auxiliar para de comando para texto
     */
    private String encode(Comando comando) {
        switch (comando.getIdentificador()) {
            case Comando.ID_RETA:
                return "RETA " + comando.getArg1();
            case Comando.ID_DIREITA:
                return "DIREITA " + comando.getArg1() + " " + comando.getArg2();
            case Comando.ID_ESQUERDA:
                return "ESQUERDA " + comando.getArg1() + " " + comando.getArg2();
            case Comando.ID_PARAR:
                return "PARAR " + (comando.getArg1() != 0 ? 1 : 0);
            default:
                return "desconhecido " + comando.toString();
        }
    }

    private Comando decode(String linha) {
        try {
            String[] comando = linha.split("\\s+"); // vamos separar um array de strings
            // Ex: RETA 10 -> ["RETA", "10"]
            String op = comando[0].toUpperCase();
            switch (op) {
                case "RETA":
                    return new Comando(Comando.ID_RETA, Integer.parseInt(comando[1]));
                case "DIREITA":
                    return new Comando(Comando.ID_DIREITA, Integer.parseInt(comando[1]), Integer.parseInt(comando[2]));
                case "ESQUERDA":
                    return new Comando(Comando.ID_ESQUERDA, Integer.parseInt(comando[1]), Integer.parseInt(comando[2]));
                case "PARAR":
                    return new Comando(Comando.ID_PARAR, Integer.parseInt(comando[1]));
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    void trabalharAtividade(){
        // executar SÓ se estiver em modo de reprodução
        if (!reproduzir) {
            estado = Estados.DORMIR;
            return;
        }

        // tratamento de erros
        if (!dados.isOnOff()) {
            dados.getGuiGravador().print("Gravador: ligue o robot primeiro");
            reproduzir = false;
            estado = Estados.DORMIR;
            return;
        }

        dados.getGuiGravador().print("Gravador: iniciar reprodução de " + caminhoReproduzir);

        try (InputStream is = new FileInputStream(caminhoReproduzir);
             Reader ir = new InputStreamReader(is, "UTF-8");
             BufferedReader br = new BufferedReader(ir)) {

            dados.getGuiGravador().print("Gravador: Semáforo adquirido");

            String linha;
            while ((linha = br.readLine()) != null && estado == Estados.TRABALHAR && reproduzir) {

                if (Thread.interrupted()) break;

                linha = linha.trim();
                if (linha.isEmpty()) continue;

                Comando comando = decode(linha);
                if (comando == null) {
                    dados.getGuiGravador().print("Gravador: linha inválida: " + linha);
                    continue;
                }
                executarComando(comando);
            }

            // no fim parar de forma não imediata para acabar a sequência
            dados.getGuiGravador().print("Gravador: Parar(false)");
            dados.getRobot().Parar(false);
            dados.getGuiGravador().print("Gravador: reprodução concluída");
        } catch (IOException e) {
            dados.getGuiGravador().print("Gravador: erro ao ler ficheiro: " + e.getMessage());
        } finally {
            reproduzir = false;
            estado = Estados.DORMIR;
            dados.getGuiGravador().resetBotaoReprod();
        }
    }

    private void executarComando(Comando comando) {
        long espera = comando.executarComandos(dados.getGuiGravador().getRobotAtual());

        dados.getGuiGravador().print("Gravador: " + comando.toString());
        if (espera > 0) {
            dados.getGuiGravador().print("Gravador: espera " + espera + " ms");
            try {
                Thread.sleep(espera);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void fecharWritter() {
        try {
            if (writer != null) writer.close();
        } catch (IOException e) {

        } finally {
            writer = null;
        }

        try {
            if (os != null) os.close();
        } catch (IOException e) {
        } finally {
            os = null;
        }
    }
}
