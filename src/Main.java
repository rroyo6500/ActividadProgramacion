import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        new Interfaz();
    }
}

class Interfaz extends JFrame {

    static Path currentDirectoryPath = FileSystems.getDefault().getPath("");
    public static String path = currentDirectoryPath.toAbsolutePath().toString();

    char[][] mapa = {
            {'-', '-', '-', '-', '-'},
            {'-', '-', '-', 'X', 'X'},
            {'-', 'X', '-', '-', '-',},
            {'X', '-', '-', '-', '-',},
            {'-', '-', 'X', 'X', '-',},
    };
    int[] playerPos;
    int[] tesoroPos;

    char[] controlKeys = {'w', 'a', 's', 'd'};

    public Interfaz() {
        JFrame juego = this;
        setLayout(null);

        BufferedImage imgTesoro;
        try {
            imgTesoro = ImageIO.read(new File(path + "\\Imagenes\\tesoro.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JPanel pantalla = new JPanel(){
            @Override
            public void paint(Graphics g){
                super.paint(g);

                int x = 0, y = 0;

                for (int i = 0; i < mapa.length; i++) {
                    for (int j = 0; j < mapa[i].length; j++) {
                        switch (mapa[i][j]) {
                            case 'X' -> g.setColor(Color.GRAY);
                            case 'P' -> g.setColor(Color.GREEN);
                            default -> g.setColor(Color.BLACK);
                        }
                        g.fillRect(x, y, 50, 50);
                        if (mapa[i][j] == 'T') g.drawImage(imgTesoro, x, y, 50, 50, null);
                        x += 50;
                    }
                    x = 0;
                    y += 50;
                }

                try {
                    if (playerPos[0] == tesoroPos[0] && playerPos[1] == tesoroPos[1]) {
                        juego.setVisible(false);

                        JFrame win = new JFrame();
                        win.setLayout(null);
                        win.setBounds(0, 0, 250, 125);
                        win.setLocationRelativeTo(null);
                        win.setTitle("Has Ganado!");
                        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        win.setVisible(true);
                        win.setFocusable(true);
                        win.requestFocus();
                        win.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                super.keyPressed(e);

                                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                    System.exit(0);
                                }
                            }
                        });

                        JLabel text = new JLabel("¡FELICIDADES!");
                        text.setBounds(0, 20, 250, 25);
                        text.setHorizontalAlignment(JLabel.CENTER);
                        win.add(text);

                        JLabel text2 = new JLabel("¡Has encontrado el tesoro!");
                        text2.setBounds(0, 50, 250, 25);
                        text2.setHorizontalAlignment(JLabel.CENTER);
                        win.add(text2);
                    }
                } catch (Exception _) {}

            }
        };
        pantalla.setBounds(10, 10, 250, 250);
        add(pantalla);

        JPanel Jugador = new JPanel();
        Jugador.setBounds(10, 275, 225, 175);
        Jugador.setLayout(null);
        Jugador.setVisible(true);
        Jugador.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Jugador", 0, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 20), Color.BLACK)
        );
        add(Jugador);

        //---------------------------------
        {
            JLabel LposX = new JLabel("Coordenada X (1 - 5)");
            LposX.setBounds(10, 50, 150, 20);
            Jugador.add(LposX);

            JTextField posX = new JTextField();
            posX.setBounds(140, 50, 50, 20);
            Jugador.add(posX);

            JLabel LposY = new JLabel("Coordenada Y (1 - 5)");
            LposY.setBounds(10, 75, 150, 20);
            Jugador.add(LposY);

            JTextField posY = new JTextField();
            posY.setBounds(140, 75, 50, 20);
            Jugador.add(posY);

            JButton anadir = new JButton("Añadir Jugador");
            anadir.setBackground(Color.GREEN);
            anadir.setBounds(12, 125, 200, 40);
            anadir.addActionListener(_ -> {
                try {
                    int x = Integer.parseInt(posX.getText()) - 1;
                    int y = Integer.parseInt(posY.getText()) - 1;

                    playerPos = new int[]{x, y};

                    mapa[y][x] = 'P';
                    Jugador.setVisible(false);
                    pantalla.repaint();
                } catch (Exception _) {
                    System.err.println("ERROR: Se ha intentado añadir un caracter no numerico en las coordenadas");
                }
            });
            Jugador.add(anadir);
        }
        //---------------------------------

        JPanel Tesoro = new JPanel();
        Tesoro.setBounds(250, 275, 225, 175);
        Tesoro.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Tesoro", 0, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 20), Color.BLACK)
        );
        Tesoro.setLayout(null);
        Tesoro.setVisible(true);
        add(Tesoro);

        //---------------------------------
        {
            JLabel LposX = new JLabel("Coordenada X (1 - 5)");
            LposX.setBounds(10, 50, 150, 20);
            Tesoro.add(LposX);

            JTextField posX = new JTextField();
            posX.setBounds(140, 50, 50, 20);
            Tesoro.add(posX);

            JLabel LposY = new JLabel("Coordenada Y (1 - 5)");
            LposY.setBounds(10, 75, 150, 20);
            Tesoro.add(LposY);

            JTextField posY = new JTextField();
            posY.setBounds(140, 75, 50, 20);
            Tesoro.add(posY);

            JButton anadir = new JButton("Añadir Tesoro");
            anadir.setBackground(Color.GREEN);
            anadir.setBounds(12, 125, 200, 40);
            anadir.addActionListener(_ -> {
                try {
                    int x = Integer.parseInt(posX.getText()) - 1;
                    int y = Integer.parseInt(posY.getText()) - 1;

                    tesoroPos = new int[]{x, y};

                    mapa[y][x] = 'T';
                    Tesoro.setVisible(false);
                    pantalla.repaint();
                } catch (Exception _) {
                    System.err.println("ERROR: Se ha intentado añadir un caracter no numerico en las coordenadas");
                }
            });
            Tesoro.add(anadir);
        }
        //---------------------------------

        setTitle("Encuentra el tesoro");
        setBounds(0, 0, 500, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setFocusable(true);
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                JFrame help;

                try {
                    if (e.getKeyChar() == controlKeys[0]) {
                        if (mapa[playerPos[1] - 1][playerPos[0]] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1] - 1][playerPos[0]] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[1] = playerPos[1] - 1;
                    } else if (e.getKeyChar() == controlKeys[1]) {
                        if (mapa[playerPos[1]][playerPos[0] - 1] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1]][playerPos[0] - 1] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[0] = playerPos[0] - 1;
                    } else if (e.getKeyChar() == controlKeys[2]) {
                        if (mapa[playerPos[1] + 1][playerPos[0]] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1] + 1][playerPos[0]] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[1] = playerPos[1] + 1;
                    } else if (e.getKeyChar() == controlKeys[3]) {
                        if (mapa[playerPos[1]][playerPos[0] + 1] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1]][playerPos[0] + 1] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[0] = playerPos[0] + 1;
                    } else if (e.getKeyChar() == 'i') {

                        help = new JFrame("Instrucciones");
                        help.setLayout(new BoxLayout(help.getContentPane(), BoxLayout.X_AXIS));
                        help.setBounds(0, 0, 1000, 800);
                        help.setResizable(false);
                        help.setLocationRelativeTo(null);
                        help.setVisible(true);
                        help.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JPanel informacionBasica = new JPanel();
                        informacionBasica.setLayout(null);
                        informacionBasica.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(Color.BLACK),
                                "Informacion Basica", 0, TitledBorder.DEFAULT_POSITION,
                                new Font("Arial", Font.BOLD, 15), Color.BLACK)
                        );
                        help.add(informacionBasica);
                        //---------------------------------
                        {
                            JLabel pTitle = new JLabel("Como añadir al Jugador:");
                            pTitle.setBounds(8, 20, 480, 25);
                            pTitle.setFont(new Font("Arial", Font.BOLD, 15));
                            informacionBasica.add(pTitle);

                            JLabel[] pText = {
                                    new JLabel("1. Añade la posicion X"),
                                    new JLabel("2. Añade la posicion Y"),
                                    new JLabel("3. Pulsa el boton de 'Añadir Jugador'")
                            };
                            for (int i = 0; i < pText.length; i++) {
                                pText[i].setBounds(25, (65 + (i * 20)), 250, 20);
                                informacionBasica.add(pText[i]);
                            }
                            ImageIcon instPlayer = new ImageIcon(path + "\\Imagenes\\InstPlayer.png");
                            Image instPlayerImg = instPlayer.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
                            JLabel pImg = new JLabel(new ImageIcon(instPlayerImg));
                            pImg.setBounds(300, 20, 160, 120);
                            informacionBasica.add(pImg);
                        }
                        {
                            JLabel pTesoro = new JLabel("Como añadir el Tesoro:");
                            pTesoro.setBounds(8, 150, 480, 25);
                            pTesoro.setFont(new Font("Arial", Font.BOLD, 15));
                            informacionBasica.add(pTesoro);

                            JLabel[] pText = {
                                    new JLabel("1. Añade la posicion X"),
                                    new JLabel("2. Añade la posicion Y"),
                                    new JLabel("3. Pulsa el boton de 'Añadir Tesoro'")
                            };
                            for (int i = 0; i < pText.length; i++) {
                                pText[i].setBounds(25, (195 + (i * 20)), 250, 20);
                                informacionBasica.add(pText[i]);
                            }
                            ImageIcon instTesoro = new ImageIcon(path + "\\Imagenes\\InstTesoro.png");
                            Image instTesoroImg = instTesoro.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
                            JLabel pImg = new JLabel(new ImageIcon(instTesoroImg));
                            pImg.setBounds(300, 150, 160, 120);
                            informacionBasica.add(pImg);
                        }
                        {
                            JLabel coordenadas = new JLabel("Coordenadas:");
                            coordenadas.setBounds(8, 275, 480, 25);
                            coordenadas.setFont(new Font("Arial", Font.BOLD, 15));
                            informacionBasica.add(coordenadas);

                            ImageIcon imgCoordenadas = new ImageIcon(path + "\\Imagenes\\Pantalla.png");
                            JLabel pImg = new JLabel(imgCoordenadas);
                            pImg.setBounds(100, 350, 300, 300);
                            informacionBasica.add(pImg);
                        }
                        //---------------------------------
                        JPanel rightPart = new JPanel();
                        rightPart.setLayout(new BoxLayout(rightPart, BoxLayout.Y_AXIS));
                        help.add(rightPart);

                        JPanel movimiento = new JPanel();
                        movimiento.setLayout(null);
                        movimiento.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(Color.BLACK),
                                "Movimiento", 0, TitledBorder.DEFAULT_POSITION,
                                new Font("Arial", Font.BOLD, 15), Color.BLACK)
                        );
                        rightPart.add(movimiento);
                        //---------------------------------
                        JLabel up = new JLabel("Moverse hacia arriba:");
                        up.setBounds(25, 20, 200, 20);
                        movimiento.add(up);
                        JLabel upKey = new JLabel(String.valueOf(controlKeys[0]));
                        upKey.setBounds(225, 20, 200, 20);
                        movimiento.add(upKey);

                        JLabel down = new JLabel("Moverse hacia abajo:");
                        down.setBounds(25, 40, 200, 20);
                        movimiento.add(down);
                        JLabel downKey = new JLabel(String.valueOf(controlKeys[2]));
                        downKey.setBounds(225, 40, 200, 20);
                        movimiento.add(downKey);

                        JLabel left = new JLabel("Moverse hacia la izquierda:");
                        left.setBounds(25, 60, 200, 20);
                        movimiento.add(left);
                        JLabel leftKey = new JLabel(String.valueOf(controlKeys[1]));
                        leftKey.setBounds(225, 60, 200, 20);
                        movimiento.add(leftKey);

                        JLabel right = new JLabel("Moverse hacia la derecha:");
                        right.setBounds(25, 80, 200, 20);
                        movimiento.add(right);
                        JLabel rightKey = new JLabel(String.valueOf(controlKeys[3]));
                        rightKey.setBounds(225, 80, 200, 20);
                        movimiento.add(rightKey);
                        //---------------------------------

                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("ERROR: El jugador no puede salir fuera del terreno del mapa.");
                } catch (IllegalArgumentException ex) {
                    System.err.println("ERROR: " + ex.getMessage());
                }
                pantalla.repaint();
            }
        });

        KeyEvent keyEvent = new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_I, 'i');
        getKeyListeners()[0].keyPressed(keyEvent);

    }
}
