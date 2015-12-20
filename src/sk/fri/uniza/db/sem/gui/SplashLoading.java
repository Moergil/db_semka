package sk.fri.uniza.db.sem.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SplashLoading {

    private static final String SPLASH_IMAGE_PATH = "splash-image.jpg";
    private static final int PADDING = 30;

    private final JDialog dialog;

    public SplashLoading() {
        dialog = new JDialog((Frame) null, "Initializing...");

        dialog.setLayout(new GridBagLayout());

        InputStream splashImageStream = getClass().getResourceAsStream(SPLASH_IMAGE_PATH);
        BufferedImage splashImage;

        try {
            splashImage = ImageIO.read(splashImageStream);
        } catch (IOException e) {
            throw new RuntimeException("Splash image is missing");
        }

        ImageIcon splashImageIcon = new ImageIcon(splashImage);
        JLabel splashImageLabel = new JLabel(splashImageIcon);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        dialog.add(splashImageLabel, c);

        c.gridy++;
        c.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        dialog.add(progressBar, c);

        dialog.pack();
        dialog.revalidate();

        dialog.setLocationRelativeTo(null);
    }

    public void show() {
        dialog.setVisible(true);
    }

    public void hide() {
        dialog.setVisible(false);
        dialog.dispose();
    }
}
