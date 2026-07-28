package RockPaperScissors;



import javax.swing.*;
import java.awt.*;

public class PvPGamePanel extends JPanel {

    // ===== Score Tracking =====
    private int player1Score = 0;
    private int player2Score = 0;
    private int tieCount = 0;

    // ===== Round State =====
    private int player1Choice = -1;
    private int player2Choice = -1;

    private final String[] choices = {"ROCK ✊", "PAPER 🖐️", "SCISSORS ✌️"};

    // ===== Shared UI Components =====
    private JLabel scoreLabel;
    private CardLayout stageLayout;
    private JPanel stagePanel;

    // Turn stage components
    private JLabel turnTitleLabel;
    private JLabel turnInstructionLabel;

    // Pass-device stage components
    private JLabel passMessageLabel;
    private JButton readyButton;

    // Result stage components
    private JLabel player1ResultCard;
    private JLabel player2ResultCard;
    private JLabel resultTextLabel;

    private static final String STAGE_TURN = "TURN";
    private static final String STAGE_PASS = "PASS";
    private static final String STAGE_RESULT = "RESULT";

    public PvPGamePanel(MainFrame frame) {

        setLayout(new BorderLayout());
        setBackground(MainFrame.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ================= TOP: Score Bar =================
        JPanel scoreBar = new JPanel();
        scoreBar.setBackground(MainFrame.ACCENT_PURPLE);
        scoreBar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel modeLabel = new JLabel("VS PLAYER MODE (2 HUMANS)");
        modeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        modeLabel.setForeground(new Color(225, 215, 240));

        scoreLabel = new JLabel("PLAYER 1: 0     PLAYER 2: 0     TIES: 0");
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

        // ================= CENTER: Stage Container (CardLayout) =================
        stageLayout = new CardLayout();
        stagePanel = new JPanel(stageLayout);
        stagePanel.setOpaque(false);

        stagePanel.add(buildTurnStage(), STAGE_TURN);
        stagePanel.add(buildPassStage(), STAGE_PASS);
        stagePanel.add(buildResultStage(), STAGE_RESULT);

        add(stagePanel, BorderLayout.CENTER);

        // ================= BOTTOM: Control Buttons =================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setOpaque(false);

        JButton mainMenuButton = createControlButton("Main Menu");
        JButton exitButton = createControlButton("Exit");

        bottomPanel.add(mainMenuButton);
        bottomPanel.add(exitButton);

        add(bottomPanel, BorderLayout.SOUTH);

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

        // Start fresh
        startPlayer1Turn();
    }

    // ============================================================
    // STAGE 1: Turn Stage (shared for both Player 1 and Player 2)
    // ============================================================
    private JPanel buildTurnStage() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        turnTitleLabel = new JLabel("PLAYER 1 - MAKE YOUR MOVE");
        turnTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        turnTitleLabel.setForeground(MainFrame.PRIMARY_COLOR);
        turnTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        turnInstructionLabel = new JLabel("Choose secretly. Player 2 should look away!");
        turnInstructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        turnInstructionLabel.setForeground(Color.GRAY);
        turnInstructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel choiceButtonsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        choiceButtonsPanel.setOpaque(false);
        choiceButtonsPanel.setMaximumSize(new Dimension(500, 70));
        choiceButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        choiceButtonsPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        JButton rockButton = createChoiceButton("ROCK ✊");
        JButton paperButton = createChoiceButton("PAPER 🖐️");
        JButton scissorsButton = createChoiceButton("SCISSORS ✌️");

        choiceButtonsPanel.add(rockButton);
        choiceButtonsPanel.add(paperButton);
        choiceButtonsPanel.add(scissorsButton);

        rockButton.addActionListener(e -> handleChoice(0));
        paperButton.addActionListener(e -> handleChoice(1));
        scissorsButton.addActionListener(e -> handleChoice(2));

        panel.add(turnTitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(turnInstructionLabel);
        panel.add(choiceButtonsPanel);

        return panel;
    }

    // ============================================================
    // STAGE 2: Pass-the-Device Privacy Screen
    // ============================================================
    private JPanel buildPassStage() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(60, 0, 20, 0));

        JLabel lockIcon = new JLabel("PASS DEVICE");
        lockIcon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lockIcon.setForeground(Color.GRAY);
        lockIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        passMessageLabel = new JLabel("Player 1 has chosen!");
        passMessageLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        passMessageLabel.setForeground(MainFrame.ACCENT_PURPLE);
        passMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passMessageLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel subMessage = new JLabel("Hand the device to the next player.");
        subMessage.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subMessage.setForeground(Color.DARK_GRAY);
        subMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        readyButton = new JButton("I'M READY");
        readyButton.setFont(MainFrame.FONT_BUTTON);
        readyButton.setForeground(Color.WHITE);
        readyButton.setBackground(MainFrame.ACCENT_GREEN);
        readyButton.setFocusPainted(false);
        readyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        readyButton.setBorder(BorderFactory.createEmptyBorder(14, 50, 14, 50));
        readyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        readyButton.addActionListener(e -> {
            if (player2Choice == -1 && player1Choice != -1) {
                startPlayer2Turn();
            }
        });

        panel.add(lockIcon);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(passMessageLabel);
        panel.add(subMessage);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(readyButton);

        return panel;
    }

    // ============================================================
    // STAGE 3: Result Reveal
    // ============================================================
    private JPanel buildResultStage() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel choiceDisplayPanel = new JPanel(new GridBagLayout());
        choiceDisplayPanel.setOpaque(false);
        choiceDisplayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 0, 20);

        player1ResultCard = createChoiceCard();
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        vsLabel.setForeground(MainFrame.TEXT_DARK);
        player2ResultCard = createChoiceCard();

        gbc.gridx = 0;
        choiceDisplayPanel.add(wrapWithTitle(player1ResultCard, "PLAYER 1"), gbc);
        gbc.gridx = 1;
        choiceDisplayPanel.add(vsLabel, gbc);
        gbc.gridx = 2;
        choiceDisplayPanel.add(wrapWithTitle(player2ResultCard, "PLAYER 2"), gbc);

        resultTextLabel = new JLabel("", SwingConstants.CENTER);
        resultTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        resultTextLabel.setForeground(MainFrame.TEXT_DARK);
        resultTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultTextLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        JButton nextRoundButton = new JButton("NEXT ROUND");
        nextRoundButton.setFont(MainFrame.FONT_BUTTON);
        nextRoundButton.setForeground(Color.WHITE);
        nextRoundButton.setBackground(MainFrame.SECONDARY_COLOR);
        nextRoundButton.setFocusPainted(false);
        nextRoundButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextRoundButton.setBorder(BorderFactory.createEmptyBorder(14, 40, 14, 40));
        nextRoundButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextRoundButton.addActionListener(e -> startPlayer1Turn());

        panel.add(choiceDisplayPanel);
        panel.add(resultTextLabel);
        panel.add(nextRoundButton);

        return panel;
    }

    // ============================================================
    // Logic / State Transitions
    // ============================================================

    private void startPlayer1Turn() {
        player1Choice = -1;
        player2Choice = -1;

        turnTitleLabel.setText("PLAYER 1 - MAKE YOUR MOVE");
        turnInstructionLabel.setText("Choose secretly. Player 2 should look away!");

        stageLayout.show(stagePanel, STAGE_TURN);
    }

    private void startPlayer2Turn() {
        turnTitleLabel.setText("PLAYER 2 - MAKE YOUR MOVE");
        turnInstructionLabel.setText("Choose secretly. Player 1 should look away!");

        stageLayout.show(stagePanel, STAGE_TURN);
    }

    private void handleChoice(int choiceIndex) {
        if (player1Choice == -1) {
            // Player 1 is choosing
            player1Choice = choiceIndex;
            passMessageLabel.setText("Player 1 has chosen!");
            stageLayout.show(stagePanel, STAGE_PASS);
        } else if (player2Choice == -1) {
            // Player 2 is choosing
            player2Choice = choiceIndex;
            revealResult();
        }
    }

    private void revealResult() {
        player1ResultCard.setText(choices[player1Choice]);
        player2ResultCard.setText(choices[player2Choice]);

        String outcome = determineWinner(player1Choice, player2Choice);

        switch (outcome) {
            case "P1_WIN":
                player1Score++;
                resultTextLabel.setText("PLAYER 1 WINS!   (" + choices[player1Choice] + " beats " + choices[player2Choice] + ")");
                resultTextLabel.setForeground(MainFrame.ACCENT_GREEN);
                highlightCard(player1ResultCard, MainFrame.ACCENT_GREEN, new Color(220, 245, 225));
                highlightCard(player2ResultCard, MainFrame.ACCENT_RED, new Color(250, 225, 225));
                break;
            case "P2_WIN":
                player2Score++;
                resultTextLabel.setText("PLAYER 2 WINS!   (" + choices[player2Choice] + " beats " + choices[player1Choice] + ")");
                resultTextLabel.setForeground(MainFrame.ACCENT_RED);
                highlightCard(player1ResultCard, MainFrame.ACCENT_RED, new Color(250, 225, 225));
                highlightCard(player2ResultCard, MainFrame.ACCENT_GREEN, new Color(220, 245, 225));
                break;
            default:
                tieCount++;
                resultTextLabel.setText("IT'S A TIE!   (Both chose " + choices[player1Choice] + ")");
                resultTextLabel.setForeground(MainFrame.ACCENT_GOLD);
                highlightCard(player1ResultCard, MainFrame.ACCENT_GOLD, new Color(255, 248, 220));
                highlightCard(player2ResultCard, MainFrame.ACCENT_GOLD, new Color(255, 248, 220));
                break;
        }

        updateScoreLabel();
        stageLayout.show(stagePanel, STAGE_RESULT);
    }

    private String determineWinner(int p1Choice, int p2Choice) {
        if (p1Choice == p2Choice) return "TIE";

        if ((p1Choice == 0 && p2Choice == 2) ||
            (p1Choice == 1 && p2Choice == 0) ||
            (p1Choice == 2 && p2Choice == 1)) {
            return "P1_WIN";
        }
        return "P2_WIN";
    }

    private void updateScoreLabel() {
        scoreLabel.setText("PLAYER 1: " + player1Score +
                "     PLAYER 2: " + player2Score +
                "     TIES: " + tieCount);
    }

    private void resetGame() {
        player1Score = 0;
        player2Score = 0;
        tieCount = 0;
        updateScoreLabel();
        startPlayer1Turn();
    }

    private void highlightCard(JLabel card, Color borderColor, Color bgColor) {
        card.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        card.setBackground(bgColor);
    }

    // ============================================================
    // UI Helper Methods
    // ============================================================

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
        titleLabel.setForeground(MainFrame.ACCENT_PURPLE);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JButton createChoiceButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(MainFrame.ACCENT_PURPLE);
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