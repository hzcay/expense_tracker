package user.Forget_Page;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class ForgetpassPage extends JFrame {
    public ForgetpassPage() {
        try {
            ImageIcon icon = new ImageIcon(ForgetPassConstants.ICON_PATH);
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        setTitle("Expense Tracker - Forgot Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(ForgetPassConstants.FRAME_WIDTH, ForgetPassConstants.FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            BufferedImage bgImage = ImageIO.read(new File(ForgetPassConstants.BACKGROUND_PATH));
            Image scaledImage = bgImage.getScaledInstance(ForgetPassConstants.FRAME_WIDTH,
                    ForgetPassConstants.FRAME_HEIGHT,
                    Image.SCALE_SMOOTH);
            JLabel bgLabel = new JLabel(new ImageIcon(scaledImage));

            bgLabel.setBounds(0, 0, ForgetPassConstants.FRAME_WIDTH,
                    ForgetPassConstants.FRAME_HEIGHT);

            getLayeredPane().add(bgLabel, Integer.valueOf(Integer.MIN_VALUE));

        } catch (IOException e) {
            e.printStackTrace();
        }

        ForgetpassView view = new ForgetpassView();
        setContentPane(view);
        new ForgetpassController(view, this);
    }
}