import javax.swing.*;

public class App implements Runnable {
    private GUIApp gui;
    private DadosApp dados;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new App());
    }

    public void run() {
        gui = new GUIApp();
        dados = new DadosApp(gui, gui.getNumSpinner());
        gui.setDados(dados);
    }
}


