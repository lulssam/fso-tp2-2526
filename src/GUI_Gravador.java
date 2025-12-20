import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI_Gravador extends JFrame {

    private static final long serialVersionUID = 1L;
    private Dados dados;

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
    private JPanel gravadorPanel;
    private JTextField ficheiroField;
    private JButton browseBtn;
    private JButton gravarBtn;
    private JButton reproduzirBtn;
    private JButton limparBtn;
    private JCheckBox imprimirChkBox;

    private JCheckBox ligarChkBox;


    private MyRobotSync robotLocal;
    private boolean ligado = false;
    private String nomeAtual;

    /**
     * Create the frame.
     */
    public GUI_Gravador(App app) {

        // Iniciar Janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 470, 610);
        setTitle("GUI Gravador");
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
                // Meter raio
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
                // Meter ângulo
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
                // Meter distância
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
                // Meter nome do Robot
                String novoNome = robotField.getText().trim();

                if (novoNome.equals(nomeAtual)) return;

                if (ligado) {
                    boolean eraPartilhado = isPartilhado();

                    // fechar robot local antigo se existir
                    if (!eraPartilhado && robotLocal != null) {
                        try {
                            robotLocal.Parar(true);
                            robotLocal.CloseEV3();
                            robotLocal.stop();
                        } catch (Exception ignore) {
                            robotLocal = null;
                        }
                        nomeAtual = novoNome;

                        // verificar novo robot
                        if (isPartilhado()) {
                            if (dados.isOnOff()) {
                                print("A usar robot partilhado: " + nomeAtual);
                            } else {
                                print("Robot partilhado não está ligado");
                            }
                        } else {
                            // criar e ligar novo robot local
                            robotLocal = new MyRobotSync();
                            robotLocal.OpenEV3(nomeAtual);
                            robotLocal.start();
                            print("Robot mudado para_ " + nomeAtual);
                        }
                    } else {
                        nomeAtual = novoNome;
                        print("Nome definido: " + nomeAtual + "(ligar para ativar)");
                    }
                }
            }
        });
        robotField.setColumns(10);
        robotField.setBounds(387, 1, 33, 26);
        contentPane.add(robotField);

        ligarChkBox = new JCheckBox("Ligar");
        ligarChkBox.setSelected(false);
        ligarChkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (ligarChkBox.isSelected()) {
                    nomeAtual = robotField.getText().trim();
                    if (isPartilhado()) {
                        if (dados.isOnOff()) {
                            ligado = true;
                            print("A usar robot partilhado" + nomeAtual);
                        } else {
                            print("Ligar o robot na GUI principal primeiro");
                            ligarChkBox.setSelected(false);
                        }
                    } else {
                        // criar robot local se não existe
                        if (robotLocal == null) {
                            robotLocal = new MyRobotSync();
                        }
                        robotLocal.OpenEV3(nomeAtual);
                        robotLocal.start();
                        ligado = true;
                        print("Robot Ligado: " + nomeAtual);
                    }
                } else {
                    // desligar - se for local, fechar
                    // se for partilhado só parar de enviar
                    if (!isPartilhado() && robotLocal != null) {
                        try {
                            robotLocal.Parar(true);
                            robotLocal.CloseEV3();
                            robotLocal.stop();
                        } catch (Exception ignored) {
                            robotLocal = null;
                        }
                        ligado = false;
                        print("Comandos desativados");
                    }
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
                getRobotAtual().Parar(true);
                dados.registarComandos(new Comando(Comando.ID_PARAR, 1));
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

        gravadorPanel = new JPanel();
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
                int result = fileChooser.showOpenDialog(GUI_Gravador.this);
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
                    dados.getRobot().Parar(false);
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
        consolaScrollPane.setBounds(6, 300, 455, 230);
        contentPane.add(consolaScrollPane);
        consolaTextArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret) consolaTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        consolaScrollPane.setViewportView(consolaTextArea);

        limparBtn = new JButton("Limpar");
        limparBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consolaTextArea.setText(null); // Limpa a consola
            }
        });
        limparBtn.setBounds(120, 540, 117, 29);
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
        imprimirChkBox.setBounds(250, 542, 100, 23);
        contentPane.add(imprimirChkBox);
    }

    public void setDados(Dados dados) {
        this.dados = dados;
        // valores pre definidos gui
        raioField.setText(Integer.toString(dados.getRaio()));
        anguloField.setText(Integer.toString(dados.getAngulo()));
        distanciaField.setText(Integer.toString(dados.getDistancia()));
        robotField.setText(dados.getNomeRobot());

        nomeAtual = dados.getNomeRobot();
        if (dados.isOnOff()) {
            ligado = true;
            ligarChkBox.setSelected(true);
            print("Robot partilhado ativo: " + nomeAtual);
        }


    }

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
        if (!ligado) {
            print("Robot não está ligado!");
            return;
        }

        try {
            // TODO confirmar se é preciso tirar estas entradas de semáforo já que fazemos no MyRobotSync
            MyRobotSync robot = getRobotAtual();
            robot.entrar();
            robot.Reta(dados.getDistancia());
            dados.registarComandos(new Comando(Comando.ID_RETA, dados.getDistancia()));
            print("Comando enviado: Frente com distância " + dados.getDistancia());
            print("Espera: " + TempoMovimento.tempoReta(dados.getDistancia()));
            // TODO confirmar com stor
            dados.registarComandos(new Comando(Comando.ID_PARAR, 0));
            robot.Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            getRobotAtual().sair();
        }
    }

    private void trasAction() {
        if (!ligado) {
            print("Robot não está ligado!");
            return;
        }
        try {
            MyRobotSync robot = getRobotAtual();
            robot.entrar();
            robot.Reta(dados.getDistancia() * -1);
            dados.registarComandos(new Comando(Comando.ID_RETA, dados.getDistancia() * -1));
            print("Comando enviado: Trás com distância: " + dados.getDistancia());
            print("Espera: " + TempoMovimento.tempoReta(dados.getDistancia()));
            dados.registarComandos(new Comando(Comando.ID_PARAR, 0));
            robot.Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            getRobotAtual().sair();
        }
    }

    private void direitaAction() {
        if (!ligado) {
            print("Robot não está ligado!");
            return;
        }
        try {
            MyRobotSync robot = getRobotAtual();
            robot.entrar();
            robot.CurvarDireita(dados.getRaio(), dados.getAngulo());
            dados.registarComandos(new Comando(Comando.ID_DIREITA, dados.getRaio(), dados.getAngulo()));
            print("Comando enviado: Curvar à Direita com ângulo " + dados.getAngulo() + " e raio " + dados.getRaio());
            print("Espera: " + TempoMovimento.tempoCurvar(dados.getRaio(), dados.getAngulo()) + " ms");
            dados.registarComandos(new Comando(Comando.ID_PARAR, 0));
            robot.Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            getRobotAtual().sair();
        }
    }

    private void esquerdaAction() {
        if (!ligado) {
            print("Robot não está ligado!");
            return;
        }
        try {
            MyRobotSync robot = getRobotAtual();
            robot.entrar();
            robot.CurvarEsquerda(dados.getRaio(), dados.getAngulo());
            dados.registarComandos(new Comando(Comando.ID_ESQUERDA, dados.getRaio(), dados.getAngulo()));
            print("Comando enviado: Curvar à Esquerda com ângulo " + dados.getAngulo() + " e raio " + dados.getRaio());
            print("Espera: " + TempoMovimento.tempoCurvar(dados.getRaio(), dados.getAngulo()) + " ms");
            dados.registarComandos(new Comando(Comando.ID_PARAR, 0));
            robot.Parar(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            getRobotAtual().sair();
        }
    }

    public MyRobotSync getRobotAtual() {
        if (nomeAtual.equals(dados.getNomeRobot()) && dados.isOnOff()) {
            return dados.getRobot();
        }
        return robotLocal;
    }

    private boolean isPartilhado(){
        return nomeAtual.equals(dados.getNomeRobot());
    }

    public void resetBotaoReprod() {
        SwingUtilities.invokeLater(() -> reproduzirBtn.setText("Reproduzir"));
    }
}