import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Random;

public class JuegoProgramacion extends JFrame{
    public static void main(String[] args) {
        new JuegoProgramacion();
    }

    static Path currentDirectoryPath = FileSystems.getDefault().getPath("");
    public static String path = currentDirectoryPath.toAbsolutePath().toString();

    final Random R = new Random();

    final char[][] mapa = new char[12][12];
    final int[] controlKeys = {87, 65, 83, 68};
    boolean infDungeon = false;

    int[] doorPos;
    int[] enemyPos;
    int[] playerPos;
    int[] tesoroPos;

    public JuegoProgramacion() {
        JFrame juego = this;
        setLayout(null);

        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                if (i == 0 || i == mapa.length - 1 || j == 0 || j == mapa[i].length - 1) {
                    mapa[i][j] = 'X';
                    continue;
                }
                int r = R.nextInt(0, (int) mapa.length / 2);
                if (r == 0) {
                    mapa[i][j] = 'X';
                } else {
                    mapa[i][j] = '-';
                }
            }
        }

        JPanel pantalla = new JPanel(){
            @Override
            public void paint(Graphics g){
                super.paint(g);

                if (playerPos != null && doorPos != null) {
                    if (playerPos[0] == doorPos[0] && playerPos[1] == doorPos[1]) {
                        infDungeon = true;
                    }
                }
                if (infDungeon) {
                    boolean Tesoro = false;
                    tesoroPos = null;
                    for (int i = 0; i < mapa.length; i++) {
                        for (int j = 0; j < mapa[i].length; j++) {
                            if (i == 0 || i == mapa.length - 1 || j == 0 || j == mapa[i].length - 1) {
                                mapa[i][j] = 'X';
                                continue;
                            }
                            int r = R.nextInt(0, (int) mapa.length / 2);
                            if (r == 0) {
                                mapa[i][j] = 'X';
                            } else if (R.nextInt(0, 1000) == 0 && !Tesoro) {
                                mapa[i][j] = 'T';
                                tesoroPos = new int[]{j, i};
                                Tesoro = true;
                            } else {
                                mapa[i][j] = '-';
                            }
                        }
                        infDungeon = false;
                    }
                    int rX = R.nextInt(1, mapa[0].length - 2);
                    int rY = R.nextInt(1, mapa.length - 2);

                    mapa[rY][rX] = 'D';
                    doorPos = new int[]{rX, rY};
                    if (playerPos != null) mapa[playerPos[1]][playerPos[0]] = 'P';
                    mapa[enemyPos[1]][enemyPos[0]] = 'E';
                }
                int rX = R.nextInt(0, mapa[0].length);
                int rY = R.nextInt(0, mapa.length);

                try {
                    while (mapa[rY][rX] == 'X' ||
                            mapa[rY][rX] == 'T' ||
                            mapa[rY][rX] == 'D'
                    ) {
                        rX = R.nextInt(0, mapa[0].length);
                        rY = R.nextInt(0, mapa.length);
                    }
                } catch (Exception _) {}

                if (enemyPos != null) mapa[enemyPos[1]][enemyPos[0]] = '-';
                enemyPos = new int[]{rX, rY};
                mapa[rY][rX] = 'E';

                int width = getWidth() / mapa[0].length;
                int height = getHeight() / mapa.length;
                int x = 0, y = 0;

                for (int i = 0; i < mapa.length; i++) {
                    for (int j = 0; j < mapa[i].length; j++) {
                        switch (mapa[i][j]) {
                            case 'X' -> g.setColor(Color.GRAY);
                            case 'P' -> g.setColor(Color.GREEN);
                            case 'T' -> g.setColor(Color.YELLOW);
                            case 'E' -> g.setColor(Color.RED);
                            case 'D' -> g.setColor(Color.CYAN);
                            default -> g.setColor(Color.BLACK);
                        }
                        g.fillRect(x, y, width, height);
                        x += width;
                    }
                    x = 0;
                    y += height;
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
                    } else if (enemyPos[0] == playerPos[0] && enemyPos[1] == playerPos[1]) {
                        juego.setVisible(false);

                        JFrame dead = new JFrame();
                        dead.setLayout(null);
                        dead.setBounds(0, 0, 250, 125);
                        dead.setLocationRelativeTo(null);
                        dead.setTitle("Has Ganado!");
                        dead.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        dead.setVisible(true);
                        dead.setFocusable(true);
                        dead.requestFocus();
                        dead.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyPressed(KeyEvent e) {
                                super.keyPressed(e);

                                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                    System.exit(0);
                                }
                            }
                        });

                        JLabel text = new JLabel("¡GAME OVER!");
                        text.setBounds(0, 20, 250, 25);
                        text.setHorizontalAlignment(JLabel.CENTER);
                        dead.add(text);

                        JLabel text2 = new JLabel("¡El Monstruo te ha cazado!");
                        text2.setBounds(0, 50, 250, 25);
                        text2.setHorizontalAlignment(JLabel.CENTER);
                        dead.add(text2);
                    }
                } catch (Exception _) {}

            }
        };
        pantalla.setBounds(0, 0, 500, 500);
        add(pantalla);

        JPanel Jugador = new JPanel();
        Jugador.setBounds(10, 525, 225, 175);
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
            JLabel LposX = new JLabel("Coordenada X (1 - " + mapa.length + ")");
            LposX.setBounds(10, 50, 150, 20);
            Jugador.add(LposX);

            JTextField posX = new JTextField();
            posX.setBounds(140, 50, 50, 20);
            Jugador.add(posX);

            JLabel LposY = new JLabel("Coordenada Y (1 - " + mapa.length + ")");
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
                    int x = Integer.parseInt(posX.getText());
                    int y = Integer.parseInt(posY.getText());

                    if (mapa[y][x] == 'X' || mapa[y][x] == 'T') {
                        new JError("No puedes colocar al Jugador en la misma posicion que una pared");
                        return;
                    }

                    playerPos = new int[]{x, y};
                    mapa[y][x] = 'P';
                    Jugador.setVisible(false);
                    pantalla.repaint();
                } catch (NumberFormatException _) {
                    new JError("Debes introducir numeros enteros en las coordenadas");
                } catch (ArrayIndexOutOfBoundsException _){
                    new JError("Debes introducir coordenadas validas (0-" + mapa.length + ")");
                }
            });
            Jugador.add(anadir);
        }
        //---------------------------------

        JPanel Tesoro = new JPanel();
        Tesoro.setBounds(250, 525, 225, 175);
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
            JLabel infD = new JLabel("Mazmorra Aleatoria");
            infD.setBounds(10, 25, 200, 20);
            Tesoro.add(infD);

            JCheckBox autoD = new JCheckBox();
            autoD.setBounds(170, 25, 20, 20);
            Tesoro.add(autoD);

            JLabel LposX = new JLabel("Coordenada X (1 - " + mapa.length + ")");
            LposX.setBounds(10, 50, 150, 20);
            Tesoro.add(LposX);

            JTextField posX = new JTextField();
            posX.setBounds(140, 50, 50, 20);
            Tesoro.add(posX);

            JLabel LposY = new JLabel("Coordenada Y (1 - " + mapa.length + ")");
            LposY.setBounds(10, 75, 150, 20);
            Tesoro.add(LposY);

            JTextField posY = new JTextField();
            posY.setBounds(140, 75, 50, 20);
            Tesoro.add(posY);

            JLabel Lauto = new JLabel("Generar automaticamente");
            Lauto.setBounds(10, 100, 150, 20);
            Tesoro.add(Lauto);

            JCheckBox auto = new JCheckBox();
            auto.setBounds(170, 100, 20, 20);
            Tesoro.add(auto);

            JButton anadir = new JButton("Añadir Tesoro");
            anadir.setBackground(Color.GREEN);
            anadir.setBounds(12, 125, 200, 40);
            anadir.addActionListener(_ -> {

                if (autoD.isSelected()) {
                    infDungeon = true;
                    Tesoro.setVisible(false);
                    pantalla.repaint();
                    return;
                }

                try {
                    int x = 0, y = 0;
                    if (auto.isSelected()) {
                        if (playerPos != null) {
                            x = mapa[0].length - 1 - playerPos[0];
                            y = mapa.length - 1 - playerPos[1];
                            if (mapa[y][x] == 'X') {
                                while (mapa[y][x] == 'X'){
                                    x = R.nextInt(0, mapa[0].length);
                                    y = R.nextInt(0, mapa.length);
                                }
                            }
                        } else {
                            while (mapa[y][x] == 'X'){
                                x = R.nextInt(0, mapa[0].length);
                                y = R.nextInt(0, mapa.length);
                            }
                        }
                    } else {
                        x = Integer.parseInt(posX.getText());
                        y = Integer.parseInt(posY.getText());

                        if (mapa[y][x] == 'X') {
                            new JError("No puedes colocar el Tesoro en la misma posicion que una pared");
                            return;
                        } else if (mapa[y][x] == 'P') {
                            new JError("No puedes colocar el Tesoro en la misma posicion que el jugador");
                            return;
                        }
                    }

                    tesoroPos = new int[]{x, y};

                    mapa[y][x] = 'T';
                    Tesoro.setVisible(false);
                    pantalla.repaint();
                } catch (NumberFormatException _) {
                    new JError("Debes introducir numeros enteros en las coordenadas");
                } catch (ArrayIndexOutOfBoundsException _){
                    new JError("Debes introducir coordenadas validas (0-" + mapa.length + ")");
                }
            });
            Tesoro.add(anadir);
        }
        //---------------------------------

        setTitle("Encuentra el tesoro");
        setBounds(0, 0, 515, 750);
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
                    if (e.getKeyCode() == controlKeys[0]) {
                        if (mapa[playerPos[1] - 1][playerPos[0]] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1] - 1][playerPos[0]] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[1] = playerPos[1] - 1;
                    } else if (e.getKeyCode() == controlKeys[1]) {
                        if (mapa[playerPos[1]][playerPos[0] - 1] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1]][playerPos[0] - 1] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[0] = playerPos[0] - 1;
                    } else if (e.getKeyCode() == controlKeys[2]) {
                        if (mapa[playerPos[1] + 1][playerPos[0]] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1] + 1][playerPos[0]] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[1] = playerPos[1] + 1;
                    } else if (e.getKeyCode() == controlKeys[3]) {
                        if (mapa[playerPos[1]][playerPos[0] + 1] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1]][playerPos[0] + 1] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[0] = playerPos[0] + 1;
                    } else if (e.getKeyChar() == 'i') {
                        help = new JFrame("Instrucciones");
                        help.setLayout(new BoxLayout(help.getContentPane(), BoxLayout.X_AXIS));

                        JPanel informacionBasica = new JPanel();
                        informacionBasica.setLayout(null);
                        informacionBasica.setMinimumSize(new Dimension(500, 800));
                        informacionBasica.setMaximumSize(new Dimension(500, 800));
                        informacionBasica.setPreferredSize(new Dimension(500, 800));
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
                        movimiento.setMinimumSize(new Dimension(480, 150));
                        movimiento.setMaximumSize(new Dimension(480, 150));
                        movimiento.setPreferredSize(new Dimension(480, 150));
                        movimiento.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(Color.BLACK),
                                "Movimiento", 0, TitledBorder.DEFAULT_POSITION,
                                new Font("Arial", Font.BOLD, 15), Color.BLACK)
                        );
                        rightPart.add(movimiento);
                        //---------------------------------
                        JLabel up = new JLabel("Moverse hacia arriba:");
                        up.setBounds(25, 25, 200, 20);
                        movimiento.add(up);
                        JLabel upKey = new JLabel(String.valueOf((char) controlKeys[0]));
                        upKey.setBounds(225, 25, 200, 20);
                        movimiento.add(upKey);

                        JLabel down = new JLabel("Moverse hacia abajo:");
                        down.setBounds(25, 50, 200, 20);
                        movimiento.add(down);
                        JLabel downKey = new JLabel(String.valueOf((char) controlKeys[2]));
                        downKey.setBounds(225, 50, 200, 20);
                        movimiento.add(downKey);

                        JLabel left = new JLabel("Moverse hacia la izquierda:");
                        left.setBounds(25, 75, 200, 20);
                        movimiento.add(left);
                        JLabel leftKey = new JLabel(String.valueOf((char) controlKeys[1]));
                        leftKey.setBounds(225, 75, 200, 20);
                        movimiento.add(leftKey);

                        JLabel right = new JLabel("Moverse hacia la derecha:");
                        right.setBounds(25, 100, 200, 20);
                        movimiento.add(right);
                        JLabel rightKey = new JLabel(String.valueOf((char) controlKeys[3]));
                        rightKey.setBounds(225, 100, 200, 20);
                        movimiento.add(rightKey);

                        //---------------------------------

                        JPanel controles = new JPanel();
                        controles.setLayout(new BoxLayout(controles, BoxLayout.Y_AXIS));
                        controles.setMinimumSize(new Dimension(480, 610));
                        controles.setMaximumSize(new Dimension(480, 610));
                        controles.setPreferredSize(new Dimension(480, 610));
                        controles.setBorder(BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(Color.BLACK),
                                "Controles", 0, TitledBorder.DEFAULT_POSITION,
                                new Font("Arial", Font.BOLD, 15), Color.BLACK)
                        );
                        rightPart.add(controles);

                        //---------------------------------
                        {
                            JPanel[] controlKeyPanels = new JPanel[4];
                            for (int i = 0; i < controlKeyPanels.length; i++) {
                                String direccion = switch (i) {
                                    case 0 -> "Up";
                                    case 1 -> "Left";
                                    case 2 -> "Down";
                                    case 3 -> "Right";
                                    default -> throw new IllegalStateException("Unexpected value: " + i);
                                };

                                controlKeyPanels[i] = new JPanel();
                                controlKeyPanels[i].setLayout(new GridBagLayout());
                                controlKeyPanels[i].setBorder(BorderFactory.createTitledBorder(
                                        BorderFactory.createLineBorder(Color.GRAY),
                                        direccion, 0, TitledBorder.DEFAULT_POSITION,
                                        new Font("Arial", Font.BOLD, 15), Color.BLACK)
                                );
                                controles.add(controlKeyPanels[i]);

                                int finalI = i;
                                JButton changeControls = new JButton("Cambiar tecla de movimiento '" + direccion + "'");
                                changeControls.setBackground(Color.GREEN);
                                changeControls.addActionListener(_ -> {
                                    JFrame changeKeyFrame = new JFrame("Cambiar tecla de movimiento '" + direccion + "'");
                                    changeKeyFrame.setBounds(0, 0, 250, 125);
                                    changeKeyFrame.setResizable(false);
                                    changeKeyFrame.setLocationRelativeTo(null);
                                    changeKeyFrame.setVisible(true);
                                    changeKeyFrame.setFocusable(true);
                                    changeKeyFrame.requestFocus();
                                    changeKeyFrame.addKeyListener(new KeyAdapter() {
                                        @Override
                                        public void keyPressed(KeyEvent e) {
                                            super.keyPressed(e);
                                            controlKeys[finalI] = e.getKeyCode();
                                            switch (direccion) {
                                                case "Up" -> upKey.setText(String.valueOf((char) controlKeys[0]));
                                                case "Left" -> leftKey.setText(String.valueOf((char) controlKeys[1]));
                                                case "Down" -> downKey.setText(String.valueOf((char) controlKeys[2]));
                                                case "Right" -> rightKey.setText(String.valueOf((char) controlKeys[3]));
                                            }
                                            changeKeyFrame.setVisible(false);
                                            changeKeyFrame.dispose();
                                        }
                                    });

                                    JLabel text = new JLabel("Presiona la tecla que desees asignar a '" + direccion + "'.");
                                    text.setBounds(10, 10, 230, 25);
                                    changeKeyFrame.add(text);
                                });
                                controlKeyPanels[i].add(changeControls);

                            }
                        }
                        //---------------------------------

                        help.setBounds(0, 0, 1000, 800);
                        help.setResizable(false);
                        help.setLocationRelativeTo(null);
                        help.setVisible(true);
                        help.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    }
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

// Clase unicamente para poder crear ventanas de error. No la cuento como necesaria para el juego.
class JError extends JFrame {
    public JError(String mensaje) {
        setLayout(new GridBagLayout());
        setTitle("Error");
        setBounds(0, 0, mensaje.length() * 10, 100);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setFocusable(true);
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                setVisible(false);
                dispose();
            }
        });

        JLabel mensajeLabel = new JLabel(mensaje);
        mensajeLabel.setBounds(0, 0, mensaje.length() * 10, 100);
        mensajeLabel.setFont(new Font("Arial", Font.BOLD, 15));
        add(mensajeLabel);
    }
}
