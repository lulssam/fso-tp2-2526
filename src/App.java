import javax.swing.*;
public class App implements Runnable {
    private GUI gui;
    private GUI_Gravador gui_gravador;
    private Dados dados;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new App());
    }

    public void run() {
        gui_gravador = new GUI_Gravador(this);
        gui = new GUI(this);

        dados = new Dados(gui, gui_gravador);
        gui_gravador.setDados(dados);
        gui.setDados(dados);
        gui.setVisible(true);
    }

    public GUI getGui() {
        return gui;
    }

    public GUI_Gravador getGui_gravador() {
        return gui_gravador;
    }

    public Dados getDados() {
        return dados;
    }
}

