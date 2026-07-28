package RockPaperScissors;


import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContainer;

    public static final String MENU_PANEL = "MENU";
    public static final String GAME_PANEL = "GAME";        // vs Computer
    public static final String PVP_PANEL = "PVP";          // vs Human

    // ===== Shared Color Theme (Classic Palette) =====
    public static final Color PRIMARY_COLOR = new Color(41, 65, 121);
    public static final Color SECONDARY_COLOR = new Color(70, 100, 170);
    public static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    public static final Color ACCENT_GREEN = new Color(46, 139, 87);
    public static final Color ACCENT_RED = new Color(178, 34, 34);
    public static final Color ACCENT_GOLD = new Color(184, 134, 11);
    public static final Color ACCENT_PURPLE = new Color(106, 76, 156);
    public static final Color TEXT_DARK = new Color(33, 33, 33);
    public static final Color BORDER_COLOR = new Color(200, 200, 205);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 34);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 16);

    private GamePanel gamePanel;
    private PvPGamePanel pvpGamePanel;

    public MainFrame() {
        setTitle("HAND BATTLE");
        setSize(700, 580);
        setMinimumSize(new Dimension(650, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        MainMenuPanel menuPanel = new MainMenuPanel(this);
        gamePanel = new GamePanel(this);
        pvpGamePanel = new PvPGamePanel(this);

        mainContainer.add(menuPanel, MENU_PANEL);
        mainContainer.add(gamePanel, GAME_PANEL);
        mainContainer.add(pvpGamePanel, PVP_PANEL);

        add(mainContainer);
        cardLayout.show(mainContainer, MENU_PANEL);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainContainer, panelName);
    }
}