
public class App extends Thread {
    private GUI gui;
    private Dados dados;
    public App() {
        gui = new GUI();
        dados = new Dados(gui);
        gui.setDados(dados);
        gui.setVisible(true);
    }
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
    public void run(){

    }
}


/*import javax.swing.*;

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
}*/

