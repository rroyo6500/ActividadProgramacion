import javax.imageio.ImageIO;
import javax.swing.*;
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
        Jugador.setBounds(0, 260, 250, 240);
        Jugador.setLayout(null);
        Jugador.setVisible(true);
        add(Jugador);

        //---------------------------------
        {
            JLabel Titulo = new JLabel("Jugador");
            Titulo.setFont(new Font("Arial", Font.BOLD, 20));
            Titulo.setBounds(10, 20, 250, 20);
            Jugador.add(Titulo);

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
            anadir.setBounds(10, 150, 200, 40);
            anadir.addActionListener(_ -> {
                int x = Integer.parseInt(posX.getText())-1;
                int y = Integer.parseInt(posY.getText())-1;

                playerPos = new int[]{x, y};

                mapa[y][x] = 'P';
                Jugador.setVisible(false);
                pantalla.repaint();
            });
            Jugador.add(anadir);
        }
        //---------------------------------

        JPanel Tesoro = new JPanel();
        Tesoro.setBounds(250, 260, 250, 240);
        Tesoro.setLayout(null);
        Tesoro.setVisible(true);
        add(Tesoro);

        //---------------------------------
        {
            JLabel Titulo = new JLabel("Tesoro");
            Titulo.setFont(new Font("Arial", Font.BOLD, 20));
            Titulo.setBounds(10, 20, 250, 20);
            Tesoro.add(Titulo);

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
            anadir.setBounds(10, 150, 200, 40);
            anadir.addActionListener(_ -> {
                int x = Integer.parseInt(posX.getText())-1;
                int y = Integer.parseInt(posY.getText())-1;

                tesoroPos = new int[]{x, y};

                mapa[y][x] = 'T';
                Tesoro.setVisible(false);
                pantalla.repaint();
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

                try {
                    if (e.getKeyChar() == 'w') {
                        if (mapa[playerPos[1] - 1][playerPos[0]] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1] - 1][playerPos[0]] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[1] = playerPos[1] - 1;
                    } else if (e.getKeyChar() == 'a') {
                        if (mapa[playerPos[1]][playerPos[0] - 1] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1]][playerPos[0] - 1] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[0] = playerPos[0] - 1;
                    } else if (e.getKeyChar() == 'd') {
                        if (mapa[playerPos[1]][playerPos[0] + 1] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1]][playerPos[0] + 1] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[0] = playerPos[0] + 1;
                    } else if (e.getKeyChar() == 's') {
                        if (mapa[playerPos[1] + 1][playerPos[0]] == 'X')
                            throw new IllegalArgumentException("El jugador no puede traspasar los muros.");
                        mapa[playerPos[1] + 1][playerPos[0]] = 'P';
                        mapa[playerPos[1]][playerPos[0]] = '-';
                        playerPos[1] = playerPos[1] + 1;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("ERROR: El jugador no puede salir fuera del terreno del mapa.");
                } catch (IllegalArgumentException ex) {
                    System.err.println("ERROR: " + ex.getMessage());
                }
                pantalla.repaint();

            }
        });
    }
}
