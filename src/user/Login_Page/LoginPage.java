package user.Login_Page;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class LoginPage extends JFrame {
    public LoginPage() {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(this);
        setTitle("Expense Tracker - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LoginConstants.FRAME_WIDTH, LoginConstants.FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            ImageIcon icon = new ImageIcon(LoginConstants.ICON_PATH);
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedImage bgImage = ImageIO.read(new File(LoginConstants.BACKGROUND_PATH));
            Image scaledImage = bgImage.getScaledInstance(LoginConstants.FRAME_WIDTH, LoginConstants.FRAME_HEIGHT,
                    Image.SCALE_SMOOTH);
            JLabel bgLabel = new JLabel(new ImageIcon(scaledImage));

            bgLabel.setBounds(0, 0, LoginConstants.FRAME_WIDTH, LoginConstants.FRAME_HEIGHT);

            getLayeredPane().add(bgLabel, Integer.valueOf(Integer.MIN_VALUE));

        } catch (IOException e) {
            e.printStackTrace();
        }

        LoginView view = new LoginView();
        setContentPane(view);
        new LoginController(view, this);
    }
}