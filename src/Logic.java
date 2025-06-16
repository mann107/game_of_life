import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main game engine that orchestrates the Game of Life
 * Handles game flow, player turns, and game state management
 */
public class Logic {
    private Player[] players;
    private GameBoard board;
    private Wheel wheel;
    private Scanner scanner;
    private Career[] nonDegreeCareers;
    private Career[] degreeCareers;
    private House[] availableHouses;
    private ActionCard[] actionCardDeck;
    private int currentPlayerIndex;
    private boolean gameEnded;
    
    public Logic() {
        this.players = new Player[0];
        this.board = new GameBoard();
        this.wheel = new Wheel();
        this.scanner = new Scanner(System.in);
        this.nonDegreeCareers = Career.createNonDegreeCareers();
        this.degreeCareers = Career.createDegreeCareers();
        this.availableHouses = House.createHouses();
        this.actionCardDeck = ActionCard.createActionCards();
        shuffleActionCardDeck();
        this.currentPlayerIndex = 0;
        this.gameEnded = false;
    }
    
    public void startGame() {
        slowPrint("Welcome to the Game of Life!");
        slowPrint("In this game, you'll navigate through life's journey...");
        
        setupPlayers();
        
        slowPrint("\nLet the game begin!");
        pressEnterToContinue();
        
        gameLoop();
        
        endGame();
    }
    
    private void setupPlayers() {
        slowPrint("\nHow many players will be playing? (2-4): ");
        int numPlayers = getIntInput(2, 4);
        
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            slowPrint("Enter name for Player " + (i + 1) + ": ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = "Player " + (i + 1);
            }
            players[i] = new Player(name);
        }
        
        slowPrint("\nPlayers created:");
        for (Player player : players) {
            slowPrint("- " + player.getName());
        }
    }
    
    private void gameLoop() {
        while (!gameEnded) {
            Player currentPlayer = players[currentPlayerIndex];
            
            if (currentPlayer.hasRetired()) {
                nextPlayer();
                continue;
            }
            
            clearScreen();
            playPlayerTurn(currentPlayer);
            
            // Check if all players have retired
            boolean allRetired = true;
            for (Player player : players) {
                if (!player.hasRetired()) {
                    allRetired = false;
                    break;
                }
            }
            
            if (allRetired) {
                gameEnded = true;
            } else {
                nextPlayer();
            }
        }
    }
    
    private void playPlayerTurn(Player player) {
        slowPrint("=== " + player.getName() + "'s Turn ===");
        showProgress(player);
        
        slowPrint("\nPress ENTER to spin the wheel...");
        scanner.nextLine();
        
        int spinResult = wheel.spin();
        boolean isRed = wheel.isRed(spinResult);
        String color = wheel.getColor(spinResult);
        
        slowPrint("You spun a " + spinResult + " (" + color + ")!");
        
        // Handle initial career/college choice
        if (player.getPosition() == 0) {
            handleInitialChoice(player, spinResult);
        } else {
            // Normal movement
            player.moveForward(spinResult);
            handleTileEffect(player);
        }
        
        slowPrint("\nCurrent Stats:");
        player.displayStats();
        
        slowPrint("\nPress 'S' to view detailed stats, or ENTER to continue...");
        String input = scanner.nextLine().trim().toUpperCase();
        if (input.equals("S")) {
            showDetailedStats(player);
        }
    }
    
    private void handleInitialChoice(Player player, int spinResult) {
        slowPrint("\nChoose your path:");
        slowPrint("1. Career Path (start working immediately)");
        slowPrint("2. College Path (20 spaces, then get degree career)");
        
        int choice = getIntInput(1, 2);
        
        if (choice == 1) {
            // Career path
            slowPrint("You chose the Career Path!");
            Career career = getRandomCareer(nonDegreeCareers);
            player.setCareer(career);
            slowPrint("You got the career: " + career);
            player.setPosition(5); // Start after career choice
        } else {
            // College path
            slowPrint("You chose College! You'll need to complete 20 spaces first.");
            slowPrint("You take out a $50,000 student loan.");
            player.takeLoan(50000);
            player.setPosition(-20); // Negative to represent college path
        }
        
        player.moveForward(spinResult);
        handleTileEffect(player);
    }
    
    private void handleTileEffect(Player player) {
        // Handle college path completion
        if (player.getPosition() >= 0 && player.getCareer() == null) {
            slowPrint("Congratulations! You graduated from college!");
            Career career = getRandomCareer(degreeCareers);
            player.setCareer(career);
            slowPrint("You got the career: " + career);
            player.setPosition(board.getMergePoint());
            return;
        }

        // Handle negative positions (still in college)
        if (player.getPosition() < 0) {
            slowPrint("You're still in college... " + Math.abs(player.getPosition()) + " spaces to go!");
            return;
        }

        // If position is before merge point and no career, move to merge point
        if (player.getPosition() < board.getMergePoint() && player.getCareer() != null) {
            player.setPosition(board.getMergePoint());
        }

        Tile currentTile = board.getTile(player.getPosition());
        if (currentTile != null) {
            slowPrint("You landed on: " + currentTile.getDescription());

            String type = currentTile.getType().toString();
            if (type.equals("PAYDAY")) {
                handlePayDay(player);
            } else if (type.equals("MARRIAGE")) {
                handleMarriage(player);
            } else if (type.equals("BABY")) {
                handleBaby(player, currentTile.getDescription().contains("Twins"));
            } else if (type.equals("HOUSE")) {
                handleHouse(player);
            } else if (type.equals("ACTION")) {
                handleActionCard(player);
            } else if (type.equals("RETIREMENT")) {
                handleRetirement(player);
            }
            // else: Empty tile, no effect
        }
    }
    
    private void handlePayDay(Player player) {
        if (player.getCareer() != null) {
            player.receiveSalary();
            slowPrint("You received your salary of $" + String.format("%,d", player.getCareer().getSalary()) + "!");
        } else {
            slowPrint("You don't have a career yet, so no salary!");
        }
    }
    
    private void handleMarriage(Player player) {
        if (!player.isMarried()) {
            player.setMarried(true);
            slowPrint("Congratulations! You got married!");
            slowPrint("You receive $10,000 in wedding gifts!");
            player.addMoney(10000);
        } else {
            slowPrint("You're already married! Draw an action card instead.");
            handleActionCard(player);
        }
    }
    
    private void handleBaby(Player player, boolean twins) {
        if (twins) {
            player.addChild();
            player.addChild();
            slowPrint("Congratulations! You had twins!");
        } else {
            player.addChild();
            slowPrint("Congratulations! You had a baby!");
        }
        
        if (player.isMarried()) {
            slowPrint("Other players give you gifts! You receive $5,000!");
            player.addMoney(5000);
        }
    }
    
    private void handleHouse(Player player) {
        slowPrint("A house is for sale!");
        House house = getRandomHouse();
        slowPrint("Available: " + house);
        
        slowPrint("Do you want to buy this house? (Y/N): ");
        String choice = scanner.nextLine().trim().toUpperCase();
        
        if (choice.equals("Y")) {
            if (player.getMoney() >= house.getBuyPrice()) {
                player.buyHouse(house);
                slowPrint("You bought the " + house.getName() + "!");
            } else {
                slowPrint("You don't have enough money! You need $" + 
                         String.format("%,d", house.getBuyPrice() - player.getMoney()) + " more.");
            }
        } else {
            slowPrint("You decided not to buy the house.");
        }
    }
    
    private void handleActionCard(Player player) {
        if (actionCardDeck.length == 0) {
            // Reshuffle deck
            actionCardDeck = ActionCard.createActionCards();
            shuffleActionCardDeck();
        }
        
        slowPrint("Press ENTER to draw an action card...");
        scanner.nextLine();
        
        ActionCard card = actionCardDeck[0];
        actionCardDeck = Arrays.copyOfRange(actionCardDeck, 1, actionCardDeck.length);
        slowPrint("You drew: " + card);
        
        if (card.isGroupSpin()) {
            handleGroupSpinCard(player);
        } else if (card.affectsAllPlayers()) {
            handleAllPlayersCard(player, card);
        } else {
            // Regular money effect
            if (card.getMoneyEffect() > 0) {
                player.addMoney(card.getMoneyEffect());
                slowPrint("You gained $" + String.format("%,d", card.getMoneyEffect()) + "!");
            } else if (card.getMoneyEffect() < 0) {
                int amount = Math.abs(card.getMoneyEffect());
                if (player.subtractMoney(amount)) {
                    slowPrint("You paid $" + String.format("%,d", amount) + "!");
                } else {
                    slowPrint("You don't have enough money! You only pay $" + 
                             String.format("%,d", player.getMoney()) + "!");
                    player.subtractMoney(player.getMoney());
                }
            }
        }
        
        // Add card to player's collection for retirement
        player.addActionCard(card);
    }
    
    private void handleGroupSpinCard(Player player) {
        slowPrint("Group Spin! All players will spin, highest number wins!");
        
        int highestSpin = 0;
        Player winner = null;
        
        for (Player p : players) {
            slowPrint(p.getName() + " spins...");
            pressEnterToContinue();
            int spin = wheel.spin();
            slowPrint(p.getName() + " spun a " + spin + "!");
            
            if (spin > highestSpin) {
                highestSpin = spin;
                winner = p;
            }
        }
        
        if (winner != null) {
            int winnings = highestSpin * 10000;
            winner.addMoney(winnings);
            slowPrint(winner.getName() + " wins $" + String.format("%,d", winnings) + "!");
        }
    }
    
    private void handleAllPlayersCard(Player player, ActionCard card) {
        int amount = Math.abs(card.getMoneyEffect());
        
        if (card.getMoneyEffect() > 0) {
            // Collect from all other players
            for (Player other : players) {
                if (other != player) {
                    if (other.subtractMoney(amount)) {
                        player.addMoney(amount);
                        slowPrint(other.getName() + " pays you $" + String.format("%,d", amount) + "!");
                    } else {
                        int available = other.getMoney();
                        other.subtractMoney(available);
                        player.addMoney(available);
                        slowPrint(other.getName() + " only has $" + String.format("%,d", available) + " to pay!");
                    }
                }
            }
        } else {
            // Pay all other players
            for (Player other : players) {
                if (other != player) {
                    if (player.subtractMoney(amount)) {
                        other.addMoney(amount);
                        slowPrint("You pay " + other.getName() + " $" + String.format("%,d", amount) + "!");
                    } else {
                        slowPrint("You don't have enough money to pay everyone!");
                        break;
                    }
                }
            }
        }
    }
    
    private void handleRetirement(Player player) {
        slowPrint(player.getName() + " has reached retirement!");
        player.setRetired(true);
        
        slowPrint("Time for final calculations...");
        slowPrint("Press ENTER to spin for house selling prices...");
        scanner.nextLine();
        
        int finalSpin = wheel.spin();
        boolean finalIsRed = wheel.isRed(finalSpin);
        String color = wheel.getColor(finalSpin);
        
        slowPrint("Final spin: " + finalSpin + " (" + color + ")");
        
        int finalMoney = player.calculateFinalMoney(finalIsRed);
        
        slowPrint("\nFinal Calculation for " + player.getName() + ":");
        slowPrint("Starting money: $" + String.format("%,d", player.getMoney()));
        slowPrint("Houses sold (" + color + " prices): $" + 
                 String.format("%,d", calculateHouseSellValue(player, finalIsRed)));
        slowPrint("Action cards (" + player.getActionCards().length + " × $10,000): $" + 
                 String.format("%,d", player.getActionCards().length * 10000));
        slowPrint("Children (" + player.getChildren() + " × $10,000): $" + 
                 String.format("%,d", player.getChildren() * 10000));
        slowPrint("Loans paid off: -$" + String.format("%,d", player.getLoanAmount()));
        slowPrint("FINAL TOTAL: $" + String.format("%,d", finalMoney));
        
        pressEnterToContinue();
    }
    
    private int calculateHouseSellValue(Player player, boolean isRed) {
        int total = 0;
        for (House house : player.getHouses()) {
            total += isRed ? house.getSellPriceRed() : house.getSellPriceBlack();
        }
        return total;
    }
    
    private void endGame() {
        clearScreen();
        slowPrint("=== GAME OVER ===");
        slowPrint("Final Results:");
        
        // Calculate final scores for all players and sort descending
        Player[] sortedPlayers = Arrays.copyOf(players, players.length);
        // Simple bubble sort for clarity
        for (int i = 0; i < sortedPlayers.length - 1; i++) {
            for (int j = 0; j < sortedPlayers.length - i - 1; j++) {
            int scoreA = sortedPlayers[j].getMoney() + sortedPlayers[j].getActionCards().length * 10000 + sortedPlayers[j].getChildren() * 10000;
            int scoreB = sortedPlayers[j + 1].getMoney() + sortedPlayers[j + 1].getActionCards().length * 10000 + sortedPlayers[j + 1].getChildren() * 10000;
            if (scoreA < scoreB) {
                Player temp = sortedPlayers[j];
                sortedPlayers[j] = sortedPlayers[j + 1];
                sortedPlayers[j + 1] = temp;
            }
            }
        }
        
        for (int i = 0; i < sortedPlayers.length; i++) {
            Player player = sortedPlayers[i];
            slowPrint((i + 1) + ". " + player.getName() + " - Final estimated value: $" + 
                     String.format("%,d", player.getMoney() + (player.getActionCards().length * 10000) + 
                                                             (player.getChildren() * 10000)));
        }
        
        slowPrint("\n🎉 " + sortedPlayers[0].getName() + " WINS! 🎉");
        slowPrint("\nThank you for playing the Game of Life!");
    }
    
    // Helper methods
    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    }
    
    private void showProgress(Player player) {
        double progress = board.getProgressPercentage(player.getPosition());
        slowPrint("Board Progress: " + String.format("%.1f", progress) + "% complete");
        slowPrint("Position: " + player.getPosition() + " / " + board.getTotalTiles());
    }
    
    private void showDetailedStats(Player player) {
        clearScreen();
        player.displayStats();
        slowPrint("\nAction Cards:");
        for (ActionCard card : player.getActionCards()) {
            slowPrint("- " + card.getTitle());
        }
        pressEnterToContinue();
    }
    
    private Career getRandomCareer(Career[] careers) {
        return careers[(int) (Math.random() * careers.length)];
    }
    
    private House getRandomHouse() {
        return availableHouses[(int) (Math.random() * availableHouses.length)];
    }
    
    private void slowPrint(String text) {
        System.out.println(text);
        try {
            Thread.sleep(100); // Small delay for immersive effect
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void pressEnterToContinue() {
        slowPrint("Press ENTER to continue...");
        scanner.nextLine();
    }
    
    private void clearScreen() {
        // Clear screen by printing newlines
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
        System.out.println("==========================================");
    }
    
    private int getIntInput(int min, int max) {
        int input = -1;
        while (input < min || input > max) {
            try {
                String line = scanner.nextLine();
                input = Integer.parseInt(line.trim());
                if (input < min || input > max) {
                    slowPrint("Please enter a number between " + min + " and " + max + ":");
                }
            } catch (NumberFormatException e) {
                slowPrint("Please enter a valid number:");
            }
        }
        return input;
    }

    private void shuffleActionCardDeck() {
        for (int i = actionCardDeck.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            ActionCard temp = actionCardDeck[i];
            actionCardDeck[i] = actionCardDeck[j];
            actionCardDeck[j] = temp;
        }
    }
}