import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * GUI principal da aplicação com MovAleatorios e EvitarObstaculos.
 * Tem uma checkbox "Gravar" que abre a GUIGravador.
 */
public class GUIApp extends JFrame implements ConsolaPrinter {

    private static final long serialVersionUID = 1L;
    private DadosApp dados;
    private GUIGravador guiGravador;

    private JPanel contentPane;
    private JTextField raioField;
    private JTextField anguloField;
    private JTextField distanciaField;
    private JTextField robotField;
    private JTextArea consolaTextArea;

    private JButton frenteBtn;
    private JButton trasBtn;
    private JButton pararBtn;
    private JButton esquerdaBtn;
    private JButton direitaBtn;
    private JSpinner numSpinner;
    private JRadioButton movAleatoriosRadioBtn;
    private JCheckBox tipoRobotChkBox;
    private JButton limparBtn;
    private JCheckBox imprimirChkBox;
    private JCheckBox gravarChkBox;

    /**
     * Create the frame.
     */
    public GUIApp() {

        // Iniciar Janela
        setTitle("Aplicação Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 470, 560);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(254, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel raioLabel = new JLabel("Raio");
        raioLabel.setBounds(6, 6, 27, 16);
        contentPane.add(raioLabel);

        raioField = new JTextField();
        raioField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dados.setRaio(Integer.parseInt(raioField.getText()));
            }
        });
        raioField.setBounds(45, 1, 33, 26);
        contentPane.add(raioField);
        raioField.setColumns(10);

        JLabel anguloLabel = new JLabel("Ângulo");
        anguloLabel.setBounds(90, 6, 45, 16);
        contentPane.add(anguloLabel);

        anguloField = new JTextField();
        anguloField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dados.setAngulo(Integer.parseInt(anguloField.getText()));
            }
        });
        anguloField.setBounds(147, 1, 33, 26);
        contentPane.add(anguloField);
        anguloField.setColumns(10);

        JLabel distanciaLabel = new JLabel("Distância");
        distanciaLabel.setBounds(190, 6, 61, 16);
        contentPane.add(distanciaLabel);

        distanciaField = new JTextField();
        distanciaField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dados.setDistancia(Integer.parseInt(distanciaField.getText()));
            }
        });
        distanciaField.setColumns(10);
        distanciaField.setBounds(263, 1, 33, 26);
        contentPane.add(distanciaField);

        JLabel robotLabel = new JLabel("Robot");
        robotLabel.setBounds(339, 6, 45, 16);
        contentPane.add(robotLabel);

        robotField = new JTextField();
        robotField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dados.setNomeRobot(robotField.getText());
            }
        });
        robotField.setColumns(10);
        robotField.setBounds(387, 1, 33, 26);
        contentPane.add(robotField);

        JCheckBox ligarChkBox = new JCheckBox("Ligar");
        ligarChkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (ligarChkBox.isSelected()) {
                    dados.getRobot().OpenEV3(dados.getNomeRobot());
                    dados.setOnOff(true);
                    dados.getRobot().start();
                    print("Sucesso na abertura das Streams");
                } else {
                    // se desligar, parar a tarefa de mov aleatorios se estiver ativa
                    if (movAleatoriosRadioBtn.isSelected()) {
                        movAleatoriosRadioBtn.setSelected(false);
                        if (dados.getMovAleatorios() != null) {
                            dados.getMovAleatorios().desativar();
                        }
                    }
                    dados.getRobot().CloseEV3();
                    dados.setOnOff(false);
                    dados.getRobot().stop();
                    ligarChkBox.setSelected(false);
                    print("Conexão encerrada");
                }
            }
        });
        ligarChkBox.setBounds(322, 34, 73, 23);
        contentPane.add(ligarChkBox);

        frenteBtn = new JButton("FRENTE");
        frenteBtn.setBounds(156, 55, 117, 29);
        frenteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Runnable frenteTarefa = new Runnable() {
                    @Override
                    public void run() {
                        frenteAction();
                    }
                };
                new Thread(frenteTarefa).start();
            }
        });
        contentPane.add(frenteBtn);

        pararBtn = new JButton("PARAR");
        pararBtn.setBounds(156, 84, 117, 29);
        pararBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dados.getRobot().Parar(true);
                print("Comando enviado: Parar (imediato)");
            }
        });
        contentPane.add(pararBtn);

        trasBtn = new JButton("TRÁS");
        trasBtn.setBounds(156, 114, 117, 29);
        trasBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Runnable trasTarefa = new Runnable() {
                    @Override
                    public void run() {
                        trasAction();
                    }
                };
                new Thread(trasTarefa).start();
            }
        });
        contentPane.add(trasBtn);

        direitaBtn = new JButton("DIREITA");
        direitaBtn.setBounds(267, 84, 117, 29);
        direitaBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Runnable direitaTarefa = new Runnable() {
                    @Override
                    public void run() {
                        direitaAction();
                    }
                };
                new Thread(direitaTarefa).start();
            }
        });
        contentPane.add(direitaBtn);

        esquerdaBtn = new JButton("ESQUERDA");
        esquerdaBtn.setBounds(45, 84, 117, 29);
        esquerdaBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Runnable esquerdaTarefa = new Runnable() {
                    @Override
                    public void run() {
                        esquerdaAction();
                    }
                };
                new Thread(esquerdaTarefa).start();
            }
        });
        contentPane.add(esquerdaBtn);

        tipoRobotChkBox = new JCheckBox("Simulado/Real");
        tipoRobotChkBox.setBounds(10, 155, 115, 23);
        tipoRobotChkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tipoRobotChkBox.isSelected()) {
                    print("Modo Selecionado: Robot Simulado");
                    dados.getRobot().setRobot(new RobotSimulado());
                } else {
                    print("Modo Selecionado: Robot Real");
                    dados.getRobot().setRobot(new CRobotLegoEV3());
                }
            }
        });
        contentPane.add(tipoRobotChkBox);

        numSpinner = new JSpinner();
        numSpinner.setBounds(191, 155, 34, 26);
        contentPane.add(numSpinner);

        JLabel numeroLabel = new JLabel("Número");
        numeroLabel.setBounds(135, 160, 61, 16);
        contentPane.add(numeroLabel);

        movAleatoriosRadioBtn = new JRadioButton("Movimentos Aleatórios");
        movAleatoriosRadioBtn.setBounds(277, 134, 182, 16);
        movAleatoriosRadioBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (movAleatoriosRadioBtn.isSelected()) {
                    if (!dados.isOnOff()) {
                        print("Ligar robot primeiro!");
                        movAleatoriosRadioBtn.setSelected(false);
                    }
                    print("Movimentos Aleatórios Selecionado");
                    dados.getMovAleatorios().ativar();
                } else {
                    print("Movimentos Aleatórios Desativado");
                    dados.getMovAleatorios().desativar();
                }
            }
        });
        contentPane.add(movAleatoriosRadioBtn);

        JRadioButton evitarRadioBtn = new JRadioButton("Evitar Obstáculos");
        evitarRadioBtn.setBounds(277, 162, 182, 16);
        evitarRadioBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (evitarRadioBtn.isSelected()) {
                    if (!dados.isOnOff()) {
                        print("Ligar robot primeiro!");
                        evitarRadioBtn.setSelected(false);
                    }
                    print("Evitar Objetos Selecionado");
                    dados.getEvitarObstaculos().ativar();
                } else {
                    print("Evitar Objetos Desativado");
                    dados.getEvitarObstaculos().desativar();
                }
            }
        });
        contentPane.add(evitarRadioBtn);

        gravarChkBox = new JCheckBox("Gravar");
        gravarChkBox.setBounds(10, 190, 100, 23);
        gravarChkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gravarChkBox.isSelected()) {
                    abrirGUIGravador();
                } else {
                    fecharGUIGravador();
                }
            }
        });
        contentPane.add(gravarChkBox);

        JLabel consolaLabel = new JLabel("Consola");
        consolaLabel.setBounds(6, 230, 61, 16);
        contentPane.add(consolaLabel);

        JScrollPane consolaScrollPane = new JScrollPane();
        consolaScrollPane.setBounds(6, 250, 455, 230);
        contentPane.add(consolaScrollPane);
        consolaTextArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret) consolaTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        consolaScrollPane.setViewportView(consolaTextArea);

        limparBtn = new JButton("Limpar");
        limparBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consolaTextArea.setText(null);
            }
        });
        limparBtn.setBounds(120, 490, 117, 29);
        contentPane.add(limparBtn);

        imprimirChkBox = new JCheckBox("Imprimir");
        imprimirChkBox.setSelected(true);
        imprimirChkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (imprimirChkBox.isSelected()) {
                    print("Impressão na consola ATIVADA.");
                }
            }
        });
        imprimirChkBox.setBounds(250, 492, 100, 23);
        contentPane.add(imprimirChkBox);

        // se fecharmos a janela o robot TEM que parar
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public JSpinner getNumSpinner() {
        return numSpinner;
    }

    public void setDados(DadosApp dados) {
        this.dados = dados;
        raioField.setText(Integer.toString(dados.getRaio()));
        anguloField.setText(Integer.toString(dados.getAngulo()));
        distanciaField.setText(Integer.toString(dados.getDistancia()));
        robotField.setText(dados.getNomeRobot());
    }

    @Override
    public void print(String s) {
        if (imprimirChkBox == null) return;
        Runnable runnable = () -> {
            if (imprimirChkBox.isSelected()) {
                consolaTextArea.append(s + "\n");
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    private void frenteAction() {
        try {
            dados.getRobot().entrar();
            dados.getRobot().Reta(dados.getDistancia());
            print("Comando enviado: Frente com distância " + dados.getDistancia());
            print("Espera: " + TempoMovimento.tempoReta(dados.getDistancia()));
            dados.getRobot().Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            dados.getRobot().sair();
        }
    }

    private void trasAction() {
        try {
            dados.getRobot().entrar();
            dados.getRobot().Reta(dados.getDistancia() * -1);
            print("Comando enviado: Trás com distância: " + dados.getDistancia());
            print("Espera: " + TempoMovimento.tempoReta(dados.getDistancia()));
            dados.getRobot().Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            dados.getRobot().sair();
        }
    }

    private void direitaAction() {
        try {
            dados.getRobot().entrar();
            dados.getRobot().CurvarDireita(dados.getRaio(), dados.getAngulo());
            print("Comando enviado: Curvar à Direita com ângulo " + dados.getAngulo() + " e raio " + dados.getRaio());
            print("Espera: " + TempoMovimento.tempoCurvar(dados.getRaio(), dados.getAngulo()) + " ms");
            dados.getRobot().Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            dados.getRobot().sair();
        }
    }

    private void esquerdaAction() {
        try {
            dados.getRobot().entrar();
            dados.getRobot().CurvarEsquerda(dados.getRaio(), dados.getAngulo());
            print("Comando enviado: Curvar à Esquerda com ângulo " + dados.getAngulo() + " e raio " + dados.getRaio());
            print("Espera: " + TempoMovimento.tempoCurvar(dados.getRaio(), dados.getAngulo()) + " ms");
            dados.getRobot().Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            dados.getRobot().sair();
        }
    }

    private void abrirGUIGravador() {
        if (guiGravador == null || !guiGravador.isVisible()) {
            guiGravador = new GUIGravador();
            DadosGravador dadosGravador = new DadosGravador(guiGravador);
            guiGravador.setDados(dadosGravador);
            guiGravador.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    gravarChkBox.setSelected(false);
                }
            });
            guiGravador.setVisible(true);
            print("GUI Gravador aberta");
        }
    }

    private void fecharGUIGravador() {
        if (guiGravador != null && guiGravador.isVisible()) {
            guiGravador.dispose();
            guiGravador = null;
            print("GUI Gravador fechada");
        }
    }

    private void cleanup() {
        try {
            fecharGUIGravador();
            if (dados != null) {
                try {
                    dados.getRobot().Parar(true);
                } catch (Exception ignore) {
                }
                try {
                    dados.getRobot().stop();
                } catch (Exception ignore) {
                }
                try {
                    dados.getRobot().CloseEV3();
                } catch (Exception igore) {

                }
                try {
                    dados.getMovAleatorios().desativar();
                    dados.getMovAleatorios().interrupt();
                } catch (Exception ignore) {
                }
                try {
                    dados.getEvitarObstaculos().desativar();
                    dados.getEvitarObstaculos().interrupt();
                } catch (Exception ignore) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
