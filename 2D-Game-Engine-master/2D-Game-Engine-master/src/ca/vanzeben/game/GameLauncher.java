package ca.vanzeben.game;
/*
Author: David J Bowen
Rework From Vanzeben
File:  GameLauncher
Descr: Starts the application
        A networking game
 */
import java.applet.Applet;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameLauncher extends Applet {

    // Constants
    public static final SimpleDateFormat DD_MMM_YYYY_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
    private static Game game = new Game();
    public static final boolean DEBUG = false;

    @Override
    public void init() {
        // *** Construct GUI ***
        setLayout(new BorderLayout());
        // Create main panel and add it to CENTER region of user JFrame:
        add(game, BorderLayout.CENTER);
        setMaximumSize(Game.DIMENSIONS);
        setMinimumSize(Game.DIMENSIONS);
        setPreferredSize(Game.DIMENSIONS);
        game.debug = DEBUG;
        game.isApplet = true;
    }

    @Override
    public void start() {
        game.start();
    }

    @Override
    public void stop() {
        game.stop();
    }

    public static void main(String[] args) {
        game.setMinimumSize(Game.DIMENSIONS);
        game.setMaximumSize(Game.DIMENSIONS);
        game.setPreferredSize(Game.DIMENSIONS);

        game.frame = new JFrame(Game.NAME);

        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLayout(new BorderLayout());

        game.frame.add(game, BorderLayout.CENTER);
        game.frame.pack();

        game.frame.setResizable(false);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        game.windowHandler = new WindowHandler(game);
        game.debug = DEBUG;

        game.start();
    }
}
