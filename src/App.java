import javax.swing.*;
// TODO 1. fechar a janela -> fechar threads
// TODO 2. botão para parar reprodução
// TODO 3. meter a funcionar com outro robot

public class App implements Runnable {
    private GUI gui;
    private Dados dados;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new App());
    }

    public void run() {
        gui = new GUI();
        dados = new Dados(gui);
        gui.setDados(dados);
        gui.setVisible(true);
    }
}

