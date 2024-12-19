package user.Signup_Page;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;

public class SignupPage extends JFrame {

    public SignupPage() {
        try {
            ImageIcon icon = new ImageIcon(SignupConstants.ICON_PATH);
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.updateComponentTreeUI(this);
        setTitle("Expense Tracker - Sign up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SignupConstants.FRAME_WIDTH, SignupConstants.FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        try {
            BufferedImage bgImage = ImageIO.read(new File(SignupConstants.BACKGROUND_PATH));
            Image scaledImage = bgImage.getScaledInstance(SignupConstants.FRAME_WIDTH, SignupConstants.FRAME_HEIGHT,
                    Image.SCALE_SMOOTH);
            JLabel bgLabel = new JLabel(new ImageIcon(scaledImage));

            bgLabel.setBounds(0, 0, SignupConstants.FRAME_WIDTH, SignupConstants.FRAME_HEIGHT);

            getLayeredPane().add(bgLabel, Integer.valueOf(Integer.MIN_VALUE));

        } catch (IOException e) {
            e.printStackTrace();
        }

        SignupView view = new SignupView();
        setContentPane(view);
        new SignupController(view, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignupPage signupPage = new SignupPage();
            signupPage.setVisible(true);
        });
    }
}
