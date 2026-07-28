package RockPaperScissors;



import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(MainFrame frame) {

        setLayout(new GridBagLayout());
        setBackground(MainFrame.BACKGROUND_COLOR);

        // ===== Card Container =====
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));

        // ===== Title =====
        JLabel titleLabel = new JLabel("HAND BATTLE");
        titleLabel.setFont(MainFrame.FONT_TITLE);
        titleLabel.setForeground(MainFrame.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Choose Your Game Mode");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== Divider =====
        JSeparator separator = new JSeparator();
        separator.setForeground(MainFrame.BORDER_COLOR);
        separator.setMaximumSize(new Dimension(300, 1));

        // ===== Symbol Badges =====
        JPanel symbolRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        symbolRow.setOpaque(false);
        symbolRow.add(createSymbolBadge("ROCK", MainFrame.SECONDARY_COLOR));
        symbolRow.add(createSymbolBadge("PAPER", MainFrame.SECONDARY_COLOR));
        symbolRow.add(createSymbolBadge("SCISSORS", MainFrame.SECONDARY_COLOR));

        // ===== Mode Buttons =====
        JButton vsComputerButton = createMenuButton("PLAYER  VS  COMPUTER", MainFrame.ACCENT_GREEN);
        JButton vsPlayerButton = createMenuButton("PLAYER  VS  PLAYER", MainFrame.ACCENT_PURPLE);
        JButton exitButton = createMenuButton("EXIT", MainFrame.ACCENT_RED);

        vsComputerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        vsPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== Assemble =====
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(subtitleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(separator);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(symbolRow);
        card.add(Box.createRigidArea(new Dimension(0, 35)));
        card.add(vsComputerButton);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(vsPlayerButton);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(exitButton);

        add(card);

        // ===== Actions =====
        vsComputerButton.addActionListener(e -> frame.showPanel(MainFrame.GAME_PANEL));
        vsPlayerButton.addActionListener(e -> frame.showPanel(MainFrame.PVP_PANEL));

        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    frame, "Are you sure you want to exit?",
                    "Exit Game", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private JLabel createSymbolBadge(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(color);
        label.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return label;
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(MainFrame.FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(14, 40, 14, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(320, 50));
        return button;
    }
}