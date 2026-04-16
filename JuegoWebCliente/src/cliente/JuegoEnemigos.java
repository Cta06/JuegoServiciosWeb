package cliente;

import javax.swing.*;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class JuegoEnemigos extends JFrame {

    private int puntos = 0;
    private Timer timerTiempo;
    private boolean juegoTerminado = false;
    private boolean activo = true;

    private JLabel lblPuntos;
    private JLabel lblTiempo;
    private JProgressBar barra;

    private long inicio;
    private ArrayList<Enemigo> enemigos = new ArrayList<>();

    private String servidorURL = "http://localhost:8081/juego";

    public JuegoEnemigos() {

        setTitle("Juego Web");
        setSize(600, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        lblPuntos = new JLabel("Puntos: 0");
        lblPuntos.setBounds(10, 10, 200, 20);
        add(lblPuntos);

        lblTiempo = new JLabel("Tiempo: 0 s");
        lblTiempo.setBounds(10, 30, 200, 20);
        add(lblTiempo);

        barra = new JProgressBar(0, 20);
        barra.setBounds(10, 60, 200, 20);
        barra.setStringPainted(true);
        add(barra);

        inicio = System.currentTimeMillis();

        escucharServidor();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                activo = false;
            }
        });

        new Thread(() -> {
            while (puntos < 20) {
                crearEnemigo();
                try { Thread.sleep(300); } catch (Exception e) {}
            }
        }).start();

        timerTiempo = new Timer(100, e -> actualizarTiempo());
        timerTiempo.start();
    }

    private void enviarEvento(String mensaje) {

        try {
            URL url = new URL(servidorURL + "/evento");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = "{\"mensaje\":\"" + mensaje + "\"}";

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            conn.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void escucharServidor() {

        new Thread(() -> {

            while (activo) {

                try {

                    URL url = new URL(servidorURL + "/eventos");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String inputLine;
                    StringBuilder content = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }

                    in.close();

                    String eventos = content.toString();

                    if (!eventos.equals("[]")) {
                        System.out.println("EVENTOS: " + eventos);
                    }

                    Thread.sleep(2000);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }).start();
    }

    private void crearEnemigo() {

        Enemigo e = new Enemigo(this);

        enemigos.add(e);
        add(e.getBoton());
        repaint();

        e.start();
    }

    public void eliminarEnemigo(Enemigo e) {

        puntos++;
        lblPuntos.setText("Puntos: " + puntos);

        barra.setValue(puntos);
        barra.setString(puntos + " / 20");

        enviarEvento("Jugador elimino enemigo. Puntos=" + puntos);

        remove(e.getBoton());
        enemigos.remove(e);
        repaint();

        if (puntos >= 20) {
            terminarJuego();
        }
    }

    public void desaparecerEnemigo(Enemigo e) {

        if (enemigos.contains(e)) {
            remove(e.getBoton());
            enemigos.remove(e);
            repaint();
        }
    }

    private void actualizarTiempo() {

        if (juegoTerminado) return;

        long ahora = System.currentTimeMillis();
        long segundos = (ahora - inicio) / 1000;

        lblTiempo.setText("Tiempo: " + segundos + " s");
    }

    private void terminarJuego() {

        juegoTerminado = true;
        activo = false;

        timerTiempo.stop();

        long fin = System.currentTimeMillis();
        long tiempo = (fin - inicio) / 1000;

        JOptionPane.showMessageDialog(this,
                "Juego terminado en " + tiempo + " segundos");

        System.exit(0);
    }

    public static void main(String[] args) {
        new JuegoEnemigos().setVisible(true);
    }
}