import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * GUI do Gravador - Janela separada para gravar e reproduzir comandos.
 * Não tem as tarefas MovAleatorios e EvitarObstaculos.
 */
public class GUIGravador extends JFrame implements ConsolaPrinter {

    private static final long serialVersionUID = 1L;
    private DadosGravador dados;

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
    private JCheckBox tipoRobotChkBox;
    private JTextField ficheiroField;
    private JButton browseBtn;
    private JButton gravarBtn;
    private JButton reproduzirBtn;
    private JButton limparBtn;
    private JCheckBox imprimirChkBox;

    /**
     * Create the frame.
     */
    public GUIGravador() {
        // Iniciar Janela
        setTitle("Gravador de Comandos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(500, 100, 470, 510);
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
                dados.registrarComandos(new Comando(Comando.ID_PARAR, 1));
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

        JPanel gravadorPanel = new JPanel();
        gravadorPanel.setOpaque(false);
        gravadorPanel.setBorder(BorderFactory.createTitledBorder("Gravador"));
        gravadorPanel.setBounds(6, 190, 455, 80);
        gravadorPanel.setLayout(null);
        contentPane.add(gravadorPanel);

        JLabel ficheiroLabel = new JLabel("Ficheiro");
        ficheiroLabel.setBounds(10, 30, 50, 16);
        gravadorPanel.add(ficheiroLabel);

        ficheiroField = new JTextField();
        ficheiroField.setBounds(65, 25, 300, 26);
        gravadorPanel.add(ficheiroField);
        ficheiroField.setColumns(10);

        browseBtn = new JButton("...");
        browseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(GUIGravador.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    ficheiroField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    print("Ficheiro selecionado: " + ficheiroField.getText());
                }
            }
        });
        browseBtn.setBounds(370, 24, 45, 29);
        gravadorPanel.add(browseBtn);

        gravarBtn = new JButton("Gravar");
        gravarBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!dados.isOnOff()) {
                    print("Ligar o robot primeiro");
                    return;
                }
                String caminho = ficheiroField.getText() != null ? ficheiroField.getText().trim() : "";
                if (caminho.isEmpty()) {
                    print("Selecione um ficheiro para gravar primeiro");
                    return;
                }

                if (!dados.getGravador().isGravar()) {
                    dados.getGravador().iniciarGravar(caminho);
                    gravarBtn.setText("Parar");
                } else {
                    dados.getGravador().pararGravacao();
                    gravarBtn.setText("Gravar");
                }
            }
        });
        gravarBtn.setBounds(100, 50, 117, 29);
        gravadorPanel.add(gravarBtn);

        reproduzirBtn = new JButton("Reproduzir");
        reproduzirBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!dados.isOnOff()) {
                    print("Ligar o robot");
                }

                String caminho = ficheiroField.getText() != null ? ficheiroField.getText().trim() : "";
                if (caminho.isEmpty()) {
                    print("Selecione um ficheiro para reproduzir primeiro");
                    return;
                }

                // se estiver a gravar, para primeiro
                if (dados.getGravador().isGravar()) {
                    dados.getGravador().pararGravacao();
                    gravarBtn.setText("Gravar");
                }

                if (!dados.getGravador().isReproduzir()) {
                    dados.getGravador().reproduzir(caminho);
                    reproduzirBtn.setText("Parar");
                    print("Reproduzir");
                } else {
                    dados.getGravador().pararReproducao();
                    reproduzirBtn.setText("Reproduzir");
                }
            }
        });
        reproduzirBtn.setBounds(230, 50, 117, 29);
        gravadorPanel.add(reproduzirBtn);

        JLabel consolaLabel = new JLabel("Consola");
        consolaLabel.setBounds(6, 280, 61, 16);
        contentPane.add(consolaLabel);

        JScrollPane consolaScrollPane = new JScrollPane();
        consolaScrollPane.setBounds(6, 300, 455, 130);
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
        limparBtn.setBounds(120, 440, 117, 29);
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
        imprimirChkBox.setBounds(250, 442, 100, 23);
        contentPane.add(imprimirChkBox);

        // se fecharmos a janela o robot TEM que parar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
        });
    }

    public void setDados(DadosGravador dados) {
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
            dados.registrarComandos(new Comando(Comando.ID_RETA, dados.getDistancia()));
            print("Comando enviado: Frente com distância " + dados.getDistancia());
            print("Espera: " + TempoMovimento.tempoReta(dados.getDistancia()));
            dados.registrarComandos(new Comando(Comando.ID_PARAR, 0));
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
            dados.registrarComandos(new Comando(Comando.ID_RETA, dados.getDistancia() * -1));
            print("Comando enviado: Trás com distância: " + dados.getDistancia());
            print("Espera: " + TempoMovimento.tempoReta(dados.getDistancia()));
            dados.registrarComandos(new Comando(Comando.ID_PARAR, 0));
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
            dados.registrarComandos(new Comando(Comando.ID_DIREITA, dados.getRaio(), dados.getAngulo()));
            print("Comando enviado: Curvar à Direita com ângulo " + dados.getAngulo() + " e raio " + dados.getRaio());
            print("Espera: " + TempoMovimento.tempoCurvar(dados.getRaio(), dados.getAngulo()) + " ms");
            dados.registrarComandos(new Comando(Comando.ID_PARAR, 0));
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
            dados.registrarComandos(new Comando(Comando.ID_ESQUERDA, dados.getRaio(), dados.getAngulo()));
            print("Comando enviado: Curvar à Esquerda com ângulo " + dados.getAngulo() + " e raio " + dados.getRaio());
            print("Espera: " + TempoMovimento.tempoCurvar(dados.getRaio(), dados.getAngulo()) + " ms");
            dados.registrarComandos(new Comando(Comando.ID_PARAR, 0));
            dados.getRobot().Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            dados.getRobot().sair();
        }
    }

    private void cleanup() {
        try {
            if (dados != null) {
                try {
                    dados.getRobot().Parar(true);
                } catch (Exception ignore) {
                }
                try {
                    dados.getGravador().desativar();
                    dados.getGravador().interrupt();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
