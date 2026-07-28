package RockPaperScissors;



import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel {

    private int playerScore = 0;
    private int computerScore = 0;
    private int tieCount = 0;

    private JLabel scoreLabel;
    private JLabel resultLabel;
    private JLabel playerChoiceLabel;
    private JLabel computerChoiceLabel;

    private final String[] choices = {"ROCK ✊", "PAPER 🖐️", "SCISSORS ✌️"};

    public GamePanel(MainFrame frame) {

        setLayout(new BorderLayout());
        setBackground(MainFrame.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ================= TOP: Score Bar =================
        JPanel scoreBar = new JPanel();
        scoreBar.setBackground(MainFrame.PRIMARY_COLOR);
        scoreBar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel modeLabel = new JLabel("VS COMPUTER MODE");
        modeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        modeLabel.setForeground(new Color(200, 210, 230));

        scoreLabel = new JLabel("PLAYER: 0     COMPUTER: 0     TIES: 0");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);

        JPanel scoreBarInner = new JPanel();
        scoreBarInner.setOpaque(false);
        scoreBarInner.setLayout(new BoxLayout(scoreBarInner, BoxLayout.Y_AXIS));
        modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreBarInner.add(modeLabel);
        scoreBarInner.add(Box.createRigidArea(new Dimension(0, 5)));
        scoreBarInner.add(scoreLabel);
        scoreBar.add(scoreBarInner);

        add(scoreBar, BorderLayout.NORTH);

        // ================= CENTER: Game Board =================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel choiceDisplayPanel = new JPanel(new GridBagLayout());
        choiceDisplayPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 0, 20);

        playerChoiceLabel = createChoiceCard();
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        vsLabel.setForeground(MainFrame.TEXT_DARK);
        computerChoiceLabel = createChoiceCard();

        gbc.gridx = 0;
        choiceDisplayPanel.add(wrapWithTitle(playerChoiceLabel, "YOU"), gbc);
        gbc.gridx = 1;
        choiceDisplayPanel.add(vsLabel, gbc);
        gbc.gridx = 2;
        choiceDisplayPanel.add(wrapWithTitle(computerChoiceLabel, "COMPUTER"), gbc);

        resultLabel = new JLabel("Make your move!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        resultLabel.setForeground(MainFrame.TEXT_DARK);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        JPanel choiceButtonsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        choiceButtonsPanel.setOpaque(false);
        choiceButtonsPanel.setMaximumSize(new Dimension(500, 70));

        JButton rockButton = createChoiceButton("ROCK ✊");
        JButton paperButton = createChoiceButton("PAPER 🖐️");
        JButton scissorsButton = createChoiceButton("SCISSORS ✌️");

        choiceButtonsPanel.add(rockButton);
        choiceButtonsPanel.add(paperButton);
        choiceButtonsPanel.add(scissorsButton);

        choiceDisplayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        choiceButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(choiceDisplayPanel);
        centerPanel.add(resultLabel);
        centerPanel.add(choiceButtonsPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ================= BOTTOM: Control Buttons =================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton playAgainButton = createControlButton("Play Again");
        JButton mainMenuButton = createControlButton("Main Menu");
        JButton exitButton = createControlButton("Exit");

        bottomPanel.add(playAgainButton);
        bottomPanel.add(mainMenuButton);
        bottomPanel.add(exitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // ================= Action Listeners =================
        rockButton.addActionListener(e -> playRound(0));
        paperButton.addActionListener(e -> playRound(1));
        scissorsButton.addActionListener(e -> playRound(2));

        playAgainButton.addActionListener(e -> resetRound());

        mainMenuButton.addActionListener(e -> {
            resetGame();
            frame.showPanel(MainFrame.MENU_PANEL);
        });

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

    private void playRound(int playerChoiceIndex) {
        Random random = new Random();
        int computerChoiceIndex = random.nextInt(3);

        playerChoiceLabel.setText(choices[playerChoiceIndex]);
        computerChoiceLabel.setText(choices[computerChoiceIndex]);

        String playerChoice = choices[playerChoiceIndex];
        String computerChoice = choices[computerChoiceIndex];
        String result = determineWinner(playerChoiceIndex, computerChoiceIndex);

        switch (result) {
            case "WIN":
                playerScore++;
                resultLabel.setText("YOU WIN!   (" + playerChoice + " beats " + computerChoice + ")");
                resultLabel.setForeground(MainFrame.ACCENT_GREEN);
                playerChoiceLabel.setBackground(new Color(220, 245, 225));
                playerChoiceLabel.setBorder(BorderFactory.createLineBorder(MainFrame.ACCENT_GREEN, 2));
                computerChoiceLabel.setBackground(new Color(250, 225, 225));
                computerChoiceLabel.setBorder(BorderFactory.createLineBorder(MainFrame.ACCENT_RED, 2));
                break;
            case "LOSE":
                computerScore++;
                resultLabel.setText("YOU LOSE!   (" + computerChoice + " beats " + playerChoice + ")");
                resultLabel.setForeground(MainFrame.ACCENT_RED);
                playerChoiceLabel.setBackground(new Color(250, 225, 225));
                playerChoiceLabel.setBorder(BorderFactory.createLineBorder(MainFrame.ACCENT_RED, 2));
                computerChoiceLabel.setBackground(new Color(220, 245, 225));
                computerChoiceLabel.setBorder(BorderFactory.createLineBorder(MainFrame.ACCENT_GREEN, 2));
                break;
            default:
                tieCount++;
                resultLabel.setText("IT'S A TIE!   (Both chose " + playerChoice + ")");
                resultLabel.setForeground(MainFrame.ACCENT_GOLD);
                playerChoiceLabel.setBackground(new Color(255, 248, 220));
                playerChoiceLabel.setBorder(BorderFactory.createLineBorder(MainFrame.ACCENT_GOLD, 2));
                computerChoiceLabel.setBackground(new Color(255, 248, 220));
                computerChoiceLabel.setBorder(BorderFactory.createLineBorder(MainFrame.ACCENT_GOLD, 2));
                break;
        }

        updateScoreLabel();
    }

    private String determineWinner(int playerChoice, int computerChoice) {
        if (playerChoice == computerChoice) return "TIE";

        if ((playerChoice == 0 && computerChoice == 2) ||
            (playerChoice == 1 && computerChoice == 0) ||
            (playerChoice == 2 && computerChoice == 1)) {
            return "WIN";
        }
        return "LOSE";
    }

    private void updateScoreLabel() {
        scoreLabel.setText("PLAYER: " + playerScore +
                "     COMPUTER: " + computerScore +
                "     TIES: " + tieCount);
    }

    private void resetRound() {
        playerChoiceLabel.setText("?");
        computerChoiceLabel.setText("?");
        resultLabel.setText("Make your move!");
        resultLabel.setForeground(MainFrame.TEXT_DARK);

        playerChoiceLabel.setBackground(Color.WHITE);
        computerChoiceLabel.setBackground(Color.WHITE);
        playerChoiceLabel.setBorder(BorderFactory.createLineBorder(MainFrame.BORDER_COLOR, 2));
        computerChoiceLabel.setBorder(BorderFactory.createLineBorder(MainFrame.BORDER_COLOR, 2));
    }

    private void resetGame() {
        playerScore = 0;
        computerScore = 0;
        tieCount = 0;
        updateScoreLabel();
        resetRound();
    }

    private JLabel createChoiceCard() {
        JLabel label = new JLabel("?", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(MainFrame.TEXT_DARK);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(MainFrame.BORDER_COLOR, 2));
        label.setPreferredSize(new Dimension(160, 90));
        return label;
    }

    private JPanel wrapWithTitle(JLabel label, String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(MainFrame.PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JButton createChoiceButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(MainFrame.SECONDARY_COLOR);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        return button;
    }

    private JButton createControlButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(MainFrame.TEXT_DARK);
        button.setBackground(new Color(225, 225, 230));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
}