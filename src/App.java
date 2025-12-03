import javax.swing.*;
// TODO 1. fechar a janela -> fechar threads
// TODO 2. botão para parar reprodução
// TODO 3. meter a funcionar com outro robot

public class App implements Runnable {
    private GUI gui;
    private Dados dados;
    public App() {
        SwingUtilities.invokeLater(this);
        gui = new GUI();
        dados = new Dados(gui);
        gui.setDados(dados);
        gui.setVisible(true);
    }
    public static void main(String[] args) {
        App app = new App();
        new Thread(app).start();
    }
    public void run(){

    }
}

