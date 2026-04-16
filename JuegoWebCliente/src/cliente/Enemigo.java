package cliente;

import javax.swing.*;

public class Enemigo extends Thread {

    private JButton boton;
    private JuegoEnemigos juego;

    public Enemigo(JuegoEnemigos juego) {

        this.juego = juego;

        boton = new JButton("X");

        boton.setBounds(
                (int)(Math.random()*500),
                (int)(Math.random()*300),
                50,
                50
        );

        boton.addActionListener(e -> juego.eliminarEnemigo(this));
    }

    public JButton getBoton() {
        return boton;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {}

        juego.desaparecerEnemigo(this);
    }
}