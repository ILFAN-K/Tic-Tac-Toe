// importing section

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tic-Tac-Toe game with an integrated Main Frame UI, enhanced graphics,
 * difficulty levels, and improved game flow with a custom pop-up for winner announcement.
 */
public class TicTacToe implements ActionListener {
    // Main frame and layout
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels
    private JPanel menuPanel;
    private JPanel setupPanel;
    private JPanel gamePanel;

    // Menu Panel Components
    private JButton startButton;
    private JButton exitButton;

    // Setup Panel Components
    private JRadioButton twoPlayerRadio;
    private JRadioButton vsComputerRadio;
    private ButtonGroup modeGroup;
    private JLabel difficultyLabel;
    private JComboBox<String> difficultyComboBox;
    private JTextField player1Field;
    private JTextField player2Field;
    private JButton setupStartButton;
    private JButton setupBackButton;

    // Game Panel Components
    private JButton[] boardButtons;
    private JLabel statusLabel;
    private JButton gameExitButton;

    // Game state variables
    private boolean playerTurn; // true for Player 1 (X), false for Player 2 or Computer (O)
    private boolean vsComputer;
    private String player1Name;
    private String player2Name;
    private int aiLevel; // 1: Easy, 2: Medium, 3: Hard
    private Random random;

    /**
     * Constructor to initialize the game.
     */
    public TicTacToe() {
        random = new Random();
        initializeFrame();
        initializeMenuPanel();
        initializeSetupPanel();
        initializeGamePanel();
        frame.setVisible(true);
    }

    /**
     * Initialize the main frame with CardLayout.
     */
    private void initializeFrame() {
        frame = new JFrame("Advanced Tic-Tac-Toe");
        frame.setSize(700, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        frame.add(mainPanel);
    }

    /**
     * Initialize the Main Menu Panel.
     */
    private void initializeMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        menuPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to Tic-Tac-Toe!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        menuPanel.add(titleLabel, gbc);

        // Start Game Button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(255, 215, 0)); // Gold
        startButton.setForeground(Color.BLACK);
        startButton.setPreferredSize(new Dimension(200, 60));
        startButton.addActionListener(this);

        gbc.gridy = 1;
        menuPanel.add(startButton, gbc);

        // Exit Button
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setFocusPainted(false);
        exitButton.setBackground(new Color(220, 20, 60)); // Crimson
        exitButton.setForeground(Color.WHITE);
        exitButton.setPreferredSize(new Dimension(200, 60));
        exitButton.addActionListener(this);

        gbc.gridy = 2;
        menuPanel.add(exitButton, gbc);

        mainPanel.add(menuPanel, "Menu");
    }

    /**
     * Initialize the Game Setup Panel.
     */
    private void initializeSetupPanel() {
        setupPanel = new JPanel();
        setupPanel.setBackground(new Color(245, 245, 245)); // White Smoke
        setupPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel setupTitleLabel = new JLabel("Game Setup");
        setupTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        setupTitleLabel.setForeground(new Color(0, 128, 128)); // Teal
        gbc.gridwidth = 2; // Span two columns
        gbc.gridy = 0;
        setupPanel.add(setupTitleLabel, gbc);

        // Reset for normal component layout
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Game Mode Selection
        JLabel modeLabel = new JLabel("Select Game Mode:");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        setupPanel.add(modeLabel, gbc);

        gbc.gridy = 2;
        twoPlayerRadio = new JRadioButton("Two Player");
        twoPlayerRadio.setFont(new Font("Arial", Font.PLAIN, 18));
        twoPlayerRadio.setBackground(new Color(245, 245, 245));
        twoPlayerRadio.setSelected(true);  // Default is Two Player
        setupPanel.add(twoPlayerRadio, gbc);

        gbc.gridy = 3;
        vsComputerRadio = new JRadioButton("Play vs Computer");
        vsComputerRadio.setFont(new Font("Arial", Font.PLAIN, 18));
        vsComputerRadio.setBackground(new Color(245, 245, 245));
        setupPanel.add(vsComputerRadio, gbc);

        // Group the radio buttons
        modeGroup = new ButtonGroup();
        modeGroup.add(twoPlayerRadio);
        modeGroup.add(vsComputerRadio);

        // Add ActionListener to radio buttons to toggle player 2 name and difficulty selection
        twoPlayerRadio.addActionListener(e -> toggleDifficultySelection());
        vsComputerRadio.addActionListener(e -> toggleDifficultySelection());

        // Difficulty Selection
        gbc.gridy = 4;
        difficultyLabel = new JLabel("Select AI Difficulty:");
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 20));
        difficultyLabel.setVisible(false); // Initially hidden
        setupPanel.add(difficultyLabel, gbc);

        gbc.gridy = 5;
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        difficultyComboBox.setVisible(false); // Initially hidden
        setupPanel.add(difficultyComboBox, gbc);

        // Player Names
        gbc.gridy = 6;
        JLabel player1Label = new JLabel("Player 1 (X) Name:");
        player1Label.setFont(new Font("Arial", Font.BOLD, 20));
        setupPanel.add(player1Label, gbc);

        gbc.gridy = 7;
        player1Field = new JTextField("Player 1");
        player1Field.setFont(new Font("Arial", Font.PLAIN, 18));
        setupPanel.add(player1Field, gbc);

        gbc.gridy = 8;
        JLabel player2Label = new JLabel("Player 2 (O) Name:");
        player2Label.setFont(new Font("Arial", Font.BOLD, 20));
        setupPanel.add(player2Label, gbc);

        gbc.gridy = 9;
        player2Field = new JTextField("Player 2");
        player2Field.setFont(new Font("Arial", Font.PLAIN, 18));
        setupPanel.add(player2Field, gbc);

        // Player 2 name will be disabled when VS Computer is selected
        player2Field.setEnabled(true); // Enable by default for two-player mode

        // Buttons
        gbc.gridy = 10;
        setupStartButton = new JButton("Start");
        setupStartButton.setFont(new Font("Arial", Font.BOLD, 20));
        setupStartButton.setFocusPainted(false);
        setupStartButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        setupStartButton.setForeground(Color.WHITE);
        setupStartButton.setPreferredSize(new Dimension(150, 50));
        setupStartButton.addActionListener(this);
        setupPanel.add(setupStartButton, gbc);

        gbc.gridy = 11;
        setupBackButton = new JButton("Back");
        setupBackButton.setFont(new Font("Arial", Font.BOLD, 20));
        setupBackButton.setFocusPainted(false);
        setupBackButton.setBackground(new Color(220, 20, 60)); // Crimson
        setupBackButton.setForeground(Color.WHITE);
        setupBackButton.setPreferredSize(new Dimension(150, 50));
        setupBackButton.addActionListener(this);
        setupPanel.add(setupBackButton, gbc);

        mainPanel.add(setupPanel, "Setup");
    }

    /**
     * Toggle visibility of difficulty selection and Player 2 name field based on game mode.
     */
    private void toggleDifficultySelection() {
        if (vsComputerRadio.isSelected()) {
            // If "VS Computer" is selected, show difficulty and disable Player 2 name field
            difficultyLabel.setVisible(true);
            difficultyComboBox.setVisible(true);
            player2Field.setEnabled(false);  // Disable Player 2 name field
            player2Field.setText("Computer"); // Set Player 2's name as "Computer"
        } else {
            // If "Two Player" is selected, hide difficulty and enable Player 2 name field
            difficultyLabel.setVisible(false);
            difficultyComboBox.setVisible(false);
            player2Field.setEnabled(true);  // Enable Player 2 name field
            player2Field.setText("Player 2");  // Reset to default
        }
        // Refresh the panel
        setupPanel.revalidate();
        setupPanel.repaint();
    }

    /**
     * Initialize the Game Panel.
     */
    private void initializeGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setBackground(new Color(245, 245, 245)); // White Smoke
        gamePanel.setLayout(new BorderLayout());

        // Status Label
        statusLabel = new JLabel("Status");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        gamePanel.add(statusLabel, BorderLayout.NORTH);

        // Game Board Panel
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 10, 10));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        boardPanel.setBackground(new Color(245, 245, 245)); // White Smoke

        boardButtons = new JButton[9];
        Font buttonFont = new Font("Arial", Font.BOLD, 60);
        for (int i = 0; i < 9; i++) {
            boardButtons[i] = new JButton("");
            boardButtons[i].setFont(buttonFont);
            boardButtons[i].setFocusPainted(false);
            boardButtons[i].setBackground(new Color(255, 255, 255)); // White
            boardButtons[i].setForeground(Color.BLACK);
            boardButtons[i].addActionListener(this);
            boardPanel.add(boardButtons[i]);
        }

        gamePanel.add(boardPanel, BorderLayout.CENTER);

        // Exit Button
        gameExitButton = new JButton("Exit to Menu");
        gameExitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        gameExitButton.setFocusPainted(false);
        gameExitButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        gameExitButton.setForeground(Color.WHITE);
        gameExitButton.setPreferredSize(new Dimension(150, 40));
        gameExitButton.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245)); // White Smoke
        bottomPanel.add(gameExitButton);

        gamePanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(gamePanel, "Game");
    }

    /**
     * Start the game by configuring settings and displaying the game panel.
     */
    private void startGame() {
        // Retrieve player names from text fields
        player1Name = player1Field.getText().trim();
        player2Name = player2Field.getText().trim();

        // Validate and set player names
        if (player1Name.isEmpty()) {
            player1Name = "Player 1";
        }
        if (vsComputer) {
            player2Name = "Computer";
        } else {
            if (player2Name.isEmpty()) {
                player2Name = "Player 2";
            }
        }

        // Initialize game state
        resetGame();

        setupGameStatus();
        cardLayout.show(mainPanel, "Game");

        // If computer starts first
        if (vsComputer && !playerTurn) {
            makeComputerMove();
        }
    }

    /**
     * Setup the game status label.
     */
    private void setupGameStatus() {
        updateStatusLabel();
    }

    /**
     * Update the status label based on the current turn.
     */
    private void updateStatusLabel() {
        if (playerTurn) {
            statusLabel.setText(player1Name + "'s turn (X)");
        } else {
            if (vsComputer) {
                statusLabel.setText(player2Name + "'s turn (O)");
            } else {
                statusLabel.setText(player2Name + "'s turn (O)");
            }
        }
    }

    /**
     * Handle button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Handle Main Menu Buttons
        if (source == startButton) {
            cardLayout.show(mainPanel, "Setup");
        } else if (source == exitButton) {
            exitApplication();
        }
        // Handle Setup Panel Buttons
        else if (source == setupStartButton) {
            vsComputer = vsComputerRadio.isSelected();
            if (vsComputer) {
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                switch (selectedDifficulty) {
                    case "Easy":
                        aiLevel = 1;
                        break;
                    case "Medium":
                        aiLevel = 2;
                        break;
                    case "Hard":
                        aiLevel = 3;
                        break;
                    default:
                        aiLevel = 1;
                }
            }
            startGame();
        } else if (source == setupBackButton) {
            cardLayout.show(mainPanel, "Menu");
        }
        // Handle Game Panel Buttons
        else if (source == gameExitButton) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit to the Main Menu?",
                    "Exit to Menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                resetGame();
                cardLayout.show(mainPanel, "Menu");
            }
        }
        // Handle Board Buttons
        else {
            JButton clickedButton = (JButton) source;

            if (!clickedButton.getText().equals("")) {
                return; // Ignore if button already clicked
            }

            if (playerTurn || !vsComputer) {
                // Player's move
                if (playerTurn) {
                    clickedButton.setForeground(new Color(30, 144, 255)); // Dodger Blue for X
                    clickedButton.setText("X");
                } else {
                    clickedButton.setForeground(new Color(220, 20, 60)); // Crimson for O
                    clickedButton.setText("O");
                }

                clickedButton.setEnabled(false);
                playerTurn = !playerTurn; // Toggle turn
                updateStatusLabel();

                if (checkForWinner()) {
                    return;
                }

                if (vsComputer && !playerTurn) {
                    // Computer's move
                    makeComputerMove();
                }
            }
        }
    }

    /**
     * Computer makes its move based on the selected difficulty level.
     */
    private void makeComputerMove() {
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(500); // Pause for better UX
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            int move;
            switch (aiLevel) {
                case 1:
                    move = getRandomMove();
                    break;
                case 2:
                    move = getMediumMove();
                    break;
                case 3:
                default:
                    move = getBestMove();
                    break;
            }

            if (move != -1) {
                boardButtons[move].setForeground(new Color(220, 20, 60)); // Crimson for O
                boardButtons[move].setText("O");
                boardButtons[move].setEnabled(false);
                playerTurn = true;
                updateStatusLabel();
                checkForWinner();
            }
        });
    }

    /**
     * AI Level 1: Easy - Random Move
     *
     * @return The index of the chosen move.
     */
    private int getRandomMove() {
        List<Integer> availableMoves = getAvailableMoves();
        if (availableMoves.isEmpty()) {
            return -1;
        }
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    /**
     * AI Level 2: Medium - Block player's win or win if possible
     *
     * @return The index of the chosen move.
     */
    private int getMediumMove() {
        // First, check if AI can win in the next move
        for (int move : getAvailableMoves()) {
            boardButtons[move].setText("O");
            if (checkForWinnerInternal("O")) {
                boardButtons[move].setText("");
                return move;
            }
            boardButtons[move].setText("");
        }

        // Then, check if player can win in the next move, and block them
        for (int move : getAvailableMoves()) {
            boardButtons[move].setText("X");
            if (checkForWinnerInternal("X")) {
                boardButtons[move].setText("");
                return move;
            }
            boardButtons[move].setText("");
        }

        // Otherwise, make a random move
        return getRandomMove();
    }

    /**
     * AI Level 3: Hard - Minimax Algorithm
     *
     * @return The index of the chosen move.
     */
    private int getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int move : getAvailableMoves()) {
            boardButtons[move].setText("O");
            int score = minimax(false, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            boardButtons[move].setText("");
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Minimax algorithm with alpha-beta pruning.
     *
     * @param isMaximizing Indicates if the current move is maximizing or minimizing.
     * @param depth        The current depth in the game tree.
     * @param alpha        The best already explored option along the path to the maximizer.
     * @param beta         The best already explored option along the path to the minimizer.
     * @return The score of the board.
     */
    private int minimax(boolean isMaximizing, int depth, int alpha, int beta) {
        if (checkForWinnerInternal("O")) {
            return 10 - depth;
        }
        if (checkForWinnerInternal("X")) {
            return depth - 10;
        }
        if (getAvailableMoves().isEmpty()) {
            return 0;
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int move : getAvailableMoves()) {
                boardButtons[move].setText("O");
                int eval = minimax(false, depth + 1, alpha, beta);
                boardButtons[move].setText("");
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int move : getAvailableMoves()) {
                boardButtons[move].setText("X");
                int eval = minimax(true, depth + 1, alpha, beta);
                boardButtons[move].setText("");
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break;
            }
            return minEval;
        }
    }

    /**
     * Get list of available moves.
     *
     * @return A list of available button indices.
     */
    private List<Integer> getAvailableMoves() {
        List<Integer> available = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (boardButtons[i].getText().equals("")) {
                available.add(i);
            }
        }
        return available;
    }

    /**
     * Check for a winner or draw after each move.
     *
     * @return True if the game has concluded, otherwise false.
     */
    private boolean checkForWinner() {
        String winner = null;

        // Define all winning combinations
        int[][] winConditions = {
                {0, 1, 2}, // Row 1
                {3, 4, 5}, // Row 2
                {6, 7, 8}, // Row 3
                {0, 3, 6}, // Column 1
                {1, 4, 7}, // Column 2
                {2, 5, 8}, // Column 3
                {0, 4, 8}, // Diagonal
                {2, 4, 6}  // Diagonal
        };

        // Check all win conditions
        for (int[] condition : winConditions) {
            String a = boardButtons[condition[0]].getText();
            String b = boardButtons[condition[1]].getText();
            String c = boardButtons[condition[2]].getText();

            if (!a.equals("") && a.equals(b) && b.equals(c)) {
                winner = a.equals("X") ? player1Name : player2Name;
                highlightWinningCombination(condition);
                break;
            }
        }

        if (winner != null) {
            showResult(winner + " wins!");
            return true;
        } else if (getAvailableMoves().isEmpty()) {
            showResult("It's a draw!");
            return true;
        }

        return false;
    }

    /**
     * Internal method to check for a specific player's win (used in AI calculations).
     *
     * @param playerSymbol The symbol to check for ("X" or "O").
     * @return True if the player has won, otherwise false.
     */
    private boolean checkForWinnerInternal(String playerSymbol) {
        // Define all winning combinations
        int[][] winConditions = {
                {0, 1, 2}, // Row 1
                {3, 4, 5}, // Row 2
                {6, 7, 8}, // Row 3
                {0, 3, 6}, // Column 1
                {1, 4, 7}, // Column 2
                {2, 5, 8}, // Column 3
                {0, 4, 8}, // Diagonal
                {2, 4, 6}  // Diagonal
        };

        // Check all win conditions
        for (int[] condition : winConditions) {
            String a = boardButtons[condition[0]].getText();
            String b = boardButtons[condition[1]].getText();
            String c = boardButtons[condition[2]].getText();

            if (a.equals(playerSymbol) && b.equals(playerSymbol) && c.equals(playerSymbol)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Highlight the winning combination on the board.
     *
     * @param condition The indices of the winning combination.
     */
    private void highlightWinningCombination(int[] condition) {
        for (int index : condition) {
            boardButtons[index].setBackground(new Color(144, 238, 144)); // Light Green
        }
    }

    /**
     * Display the game result in a custom pop-up dialog.
     *
     * @param message The result message to display.
     */
    private void showResult(String message) {
        // Create a custom dialog
        JDialog resultDialog = new JDialog(frame, "Game Over", true);
        resultDialog.setSize(400, 300);
        resultDialog.setLayout(new BorderLayout());
        resultDialog.setLocationRelativeTo(frame); // Center on parent

        // Create a panel for the message
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(245, 245, 245));
        messagePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Result Label
        JLabel resultLabel = new JLabel(message);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultLabel.setForeground(new Color(220, 20, 60)); // Crimson
        gbc.gridx = 0;
        gbc.gridy = 0;
        messagePanel.add(resultLabel, gbc);

        // Emoji Label
        JLabel emojiLabel = new JLabel();
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        if (message.contains("wins")) {
            emojiLabel.setText("🏆"); // Trophy emoji for win
            emojiLabel.setForeground(new Color(0, 128, 128)); // Teal
        } else {
            emojiLabel.setText("🤝"); // Handshake emoji for draw
            emojiLabel.setForeground(Color.BLACK);
        }
        gbc.gridy = 1;
        messagePanel.add(emojiLabel, gbc);

        resultDialog.add(messagePanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(245, 245, 245));

        // Play Again Button
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 18));
        playAgainButton.setFocusPainted(false);
        playAgainButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setPreferredSize(new Dimension(120, 40));
        playAgainButton.addActionListener(e -> {
            resultDialog.dispose();
            resetGame();
            cardLayout.show(mainPanel, "Game");
            if (vsComputer && !playerTurn) {
                makeComputerMove();
            }
        });
        buttonsPanel.add(playAgainButton);

        // Main Menu Button
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 18));
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setBackground(new Color(220, 20, 60)); // Crimson
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setPreferredSize(new Dimension(120, 40));
        mainMenuButton.addActionListener(e -> {
            resultDialog.dispose();
            resetGame();
            cardLayout.show(mainPanel, "Menu");
        });
        buttonsPanel.add(mainMenuButton);

        resultDialog.add(buttonsPanel, BorderLayout.SOUTH);

        resultDialog.setVisible(true);
    }

    /**
     * Reset the game board to play again with the same settings.
     */
    private void resetGame() {
        // Reset buttons
        for (JButton button : boardButtons) {
            button.setText("");
            button.setEnabled(true);
            button.setBackground(new Color(255, 255, 255)); // White
            button.setForeground(Color.BLACK);
        }

        // Reset game state
        playerTurn = true;
        updateStatusLabel();
    }

    /**
     * Exit the application gracefully.
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
        }
    }

    /**
     * Main method to run the game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
