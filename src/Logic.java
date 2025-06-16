import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

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
    slowPrint("=================================");
    slowPrint("   WELCOME TO THE GAME OF LIFE   ");
    slowPrint("=================================");
    
    boolean shouldExit = false;
    while (shouldExit == false) {
        clearScreen();
        slowPrint("Main Menu:");
        slowPrint("1. New Game");
        slowPrint("2. Load Game");
        slowPrint("3. Exit");
        slowPrint("Choose an option (1-3): ");
        
        int choice = getIntInput(1, 3);
        
        if (choice == 1) {
            setupPlayers();
            slowPrint("\nLet the game begin!");
            pressEnterToContinue();
            gameLoop();
            endGame();
            shouldExit = true;
        } else if (choice == 2) {
            loadGame();
            if (players.length > 0) {
                gameLoop();
                endGame();
                shouldExit = true;
            }
        } else if (choice == 3) {
            slowPrint("Thanks for playing! Goodbye!");
            shouldExit = true;
        }
    }
}
    
    private void setupPlayers() {
        slowPrint("\nHow many players will be playing? (2-4): ");
        int numPlayers = getIntInput(2, 4);
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            slowPrint("Enter name for Player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            if (name.length() == 0) {
                name = "Player " + (i + 1);
            }
            players[i] = new Player(name);
        }
        slowPrint("\nPlayers created:");
        for (int i = 0; i < players.length; i++) {
            slowPrint("- " + players[i].getName());
        }
    }
    
    private void gameLoop() {
    gameEnded = false; // Reset this when starting a new game
    while (gameEnded == false) {
        Player currentPlayer = players[currentPlayerIndex];
        if (currentPlayer.hasRetired()) {
            nextPlayer();
            continue;
        }
        clearScreen();
        playPlayerTurn(currentPlayer);
        
        // Check if player chose to exit to main menu
        if (gameEnded) {
            return; // Exit the game loop
        }
        
        boolean allRetired = true;
        for (int i = 0; i < players.length; i++) {
            if (players[i].hasRetired() == false) {
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
    

    if (checkAllPlayersRetired()) {
        endGame();
    }
}

private boolean checkAllPlayersRetired() {
    boolean allRetired;
    for (int i = 0; i < players.length; i++) {
        if (players[i].hasRetired() == false) {
            allRetired = false;
            return allRetired;
        }
    }
    allRetired = true;
    return  allRetired;
}
    
private void playPlayerTurn(Player player) {
    slowPrint("=== " + player.getName() + "'s Turn ===");
    showProgress(player);
    
    slowPrint("\nOptions:");
    slowPrint("1. Spin the wheel");
    slowPrint("2. View detailed stats");
    slowPrint("3. Save game");
    slowPrint("4. Exit to main menu");
    slowPrint("Choose an option (1-4): ");
    
    int choice = getIntInput(1, 4);
    
    if (choice == 1) {
        slowPrint("\nPress ENTER to spin the wheel...");
        scanner.nextLine();
        int spinResult = wheel.spin();
        boolean isRed = wheel.isRed(spinResult);
        String color = wheel.getColor(spinResult);
        slowPrint("You spun a " + spinResult + " (" + color + ")!");
        if (player.getPosition() == 0) {
            handleInitialChoice(player, spinResult);
        } else {
            player.moveForward(spinResult);
            handleTileEffect(player);
        }
        slowPrint("\nCurrent Stats:");
        player.displayStats();
        pressEnterToContinue();
    } else if (choice == 2) {
        showDetailedStats(player);
    } else if (choice == 3) {
        saveGame();
    } else if (choice == 4) {
        slowPrint("\nReturning to main menu...");
        pressEnterToContinue();
        gameEnded = true; // This will exit the game loop
    }
}
    
    private void handleInitialChoice(Player player, int spinResult) {
        slowPrint("\nChoose your path:");
        slowPrint("1. Career Path (start working immediately)");
        slowPrint("2. College Path (20 spaces, then get degree career)");
        int choice = getIntInput(1, 2);
        if (choice == 1) {
            slowPrint("You chose the Career Path!");
            Career career = getRandomCareer(nonDegreeCareers);
            player.setCareer(career);
            slowPrint("You got the career: " + career);
            player.setPosition(5);
        } else {
            slowPrint("You chose College! You'll need to complete 20 spaces first.");
            slowPrint("You take out a $50,000 student loan.");
            player.takeLoan(50000);
            player.setPosition(-20);
        }
        player.moveForward(spinResult);
        handleTileEffect(player);
    }
    
    private void handleTileEffect(Player player) {
        if (player.getPosition() >= 0 && player.getCareer() == null) {
            slowPrint("Congratulations! You graduated from college!");
            Career career = getRandomCareer(degreeCareers);
            player.setCareer(career);
            slowPrint("You got the career: " + career);
            player.setPosition(board.getMergePoint());
            return;
        }
        if (player.getPosition() < 0) {
            slowPrint("You're still in college... " + Math.abs(player.getPosition()) + " spaces to go!");
            return;
        }
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
        }
    }
    
    private void handlePayDay(Player player) {
        if (player.getCareer() != null) {
            player.receiveSalary();
            slowPrint("You received your salary of $" + player.getCareer().getSalary() + "!");
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
        String choice = scanner.nextLine().toUpperCase();
        if (choice.equals("Y")) {
            if (player.getMoney() >= house.getBuyPrice()) {
                player.buyHouse(house);
                slowPrint("You bought the " + house.getName() + "!");
            } else {
                slowPrint("You don't have enough money! You need $" + 
                        (house.getBuyPrice() - player.getMoney()) + " more.");
            }
        } else {
            slowPrint("You decided not to buy the house.");
        }
    }
    
    private void handleActionCard(Player player) {
        if (actionCardDeck.length == 0) {
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
            if (card.getMoneyEffect() > 0) {
                player.addMoney(card.getMoneyEffect());
                slowPrint("You gained $" + card.getMoneyEffect() + "!");
            } else if (card.getMoneyEffect() < 0) {
                int amount = Math.abs(card.getMoneyEffect());
                if (player.subtractMoney(amount)) {
                    slowPrint("You paid $" + amount + "!");
                } else {
                    slowPrint("You don't have enough money! You only pay $" + 
                            player.getMoney() + "!");
                    player.subtractMoney(player.getMoney());
                }
            }
        }
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
            slowPrint(winner.getName() + " wins $" + winnings + "!");
        }
    }
    
    private void handleAllPlayersCard(Player player, ActionCard card) {
        int amount = Math.abs(card.getMoneyEffect());
        if (card.getMoneyEffect() > 0) {
            for (Player other : players) {
                if (other != player) {
                    if (other.subtractMoney(amount)) {
                        player.addMoney(amount);
                        slowPrint(other.getName() + " pays you $" + amount + "!");
                    } else {
                        int available = other.getMoney();
                        other.subtractMoney(available);
                        player.addMoney(available);
                        slowPrint(other.getName() + " only has $" + available + " to pay!");
                    }
                }
            }
        } else {
            for (Player other : players) {
                if (other != player) {
                    if (player.subtractMoney(amount)) {
                        other.addMoney(amount);
                        slowPrint("You pay " + other.getName() + " $" + amount + "!");
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
        slowPrint("Starting money: $" + player.getMoney());
        slowPrint("Houses sold (" + color + " prices): $" + 
                 calculateHouseSellValue(player, finalIsRed));
        slowPrint("Action cards (" + player.getActionCards().length + " × $10,000): $" + 
                 player.getActionCards().length * 10000);
        slowPrint("Children (" + player.getChildren() + " × $10,000): $" + 
                 player.getChildren() * 10000);
        slowPrint("Loans paid off: -$" + player.getLoanAmount());
        slowPrint("FINAL TOTAL: $" + finalMoney);
        pressEnterToContinue();
    }
    
    private int calculateHouseSellValue(Player player, boolean isRed) {
        int total = 0;
        House[] houses = player.getHouses();
        for (int i = 0; i < houses.length; i++) {
            House house = houses[i];
            if (isRed) {
                total += house.getSellPriceRed();
            } else {
                total += house.getSellPriceBlack();
            }
        }
        return total;
    }
    
    private void endGame() {
        clearScreen();
        slowPrint("=== GAME OVER ===");
        slowPrint("Final Results:");
        Player[] sortedPlayers = Arrays.copyOf(players, players.length);
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
                     (player.getMoney() + (player.getActionCards().length * 10000) + 
                                                             (player.getChildren() * 10000)));
        }
        slowPrint("\n🎉 " + sortedPlayers[0].getName() + " WINS! 🎉");
        slowPrint("\nThank you for playing the Game of Life!");
    }
    
    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    }
    
    private void showProgress(Player player) {
        double progress = board.getProgressPercentage(player.getPosition());
        slowPrint("Board Progress: " + progress + "% complete");
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
    
    //got this code from online, it is not my own
    // but it is a simple way to print text slowly to simulate typing
    private void slowPrint(String text) {
        System.out.println(text);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void pressEnterToContinue() {
        slowPrint("Press ENTER to continue...");
        scanner.nextLine();
    }
    
    private void clearScreen() {
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
        System.out.println("==========================================");
    }
    
    private int getIntInput(int min, int max) {
        int input = -1;
        while (input < min || input > max) {
            
                String line = scanner.nextLine();
                input = Integer.parseInt(line);
                if (input < min || input > max) {
                    slowPrint("Please enter a number between " + min + " and " + max + ":");
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
    private void saveGame() {
    try {
        String fileName = "game_of_life_save.txt";
        java.io.FileWriter writer = new java.io.FileWriter(fileName);
        
        writer.write("CURRENT_PLAYER_INDEX:" + currentPlayerIndex + "\n");
        
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            writer.write("PLAYER:\n");
            writer.write("NAME:" + player.getName() + "\n");
            writer.write("POSITION:" + player.getPosition() + "\n");
            writer.write("MONEY:" + player.getMoney() + "\n");
            writer.write("CHILDREN:" + player.getChildren() + "\n");
            writer.write("LOAN:" + player.getLoanAmount() + "\n");
            writer.write("MARRIED:" + player.isMarried() + "\n");
            writer.write("RETIRED:" + player.hasRetired() + "\n");
            
            if (player.getCareer() != null) {
                writer.write("CAREER:" + player.getCareer().getName() + "\n");
            }
            
            House[] houses = player.getHouses();
            for (int j = 0; j < houses.length; j++) {
                writer.write("HOUSE:" + houses[j].getName() + "\n");
            }
            
            ActionCard[] cards = player.getActionCards();
            for (int k = 0; k < cards.length; k++) {
                writer.write("ACTION_CARD:" + cards[k].getTitle() + "\n");
            }
        }
        
        writer.close();
        slowPrint("Game saved successfully to " + fileName);
    } catch (Exception e) {
        slowPrint("Error saving game: " + e.getMessage());
    }
    pressEnterToContinue();
}
private void loadGame() {
    try {
        String fileName = "game_of_life_save.txt";
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fileName));
        
        String line;
        Player currentPlayer = null;
        Player[] loadedPlayers = new Player[4];
        int loadedPlayerCount = 0;
        
        while ((line = reader.readLine()) != null) {
            if (line.equals("PLAYER:")) {
                currentPlayer = new Player(""); // Initialize with empty name
                loadedPlayers[loadedPlayerCount++] = currentPlayer;
            } else if (currentPlayer != null) {
                int colonIndex = line.indexOf(":");
                if (colonIndex > 0) {
                    String key = line.substring(0, colonIndex).trim();
                    String value = line.substring(colonIndex + 1).trim();
                    
                    if (key.equals("NAME")) {
                        currentPlayer.setName(value);
                    } else if (key.equals("POSITION")) {
                        currentPlayer.setPosition(Integer.parseInt(value));
                    } else if (key.equals("MONEY")) {
                        currentPlayer.setMoney(Integer.parseInt(value));
                    } else if (key.equals("CHILDREN")) {
                        currentPlayer.setChildren(Integer.parseInt(value));
                    } else if (key.equals("LOAN")) {
                        currentPlayer.setLoanAmount(Integer.parseInt(value));
                    } else if (key.equals("MARRIED")) {
                        currentPlayer.setMarried(Boolean.parseBoolean(value));
                    } else if (key.equals("RETIRED")) {
                        currentPlayer.setRetired(Boolean.parseBoolean(value));
                    } else if (key.equals("CAREER")) {
                        Career career = findCareerByName(value);
                        if (career != null) {
                            currentPlayer.setCareer(career);
                        }
                    } else if (key.equals("HOUSE")) {
                        House house = findHouseByName(value);
                        if (house != null) {
                            currentPlayer.buyHouse(house);
                        }
                    } else if (key.equals("ACTION_CARD")) {
                        ActionCard card = findActionCardByTitle(value);
                        if (card != null) {
                            currentPlayer.addActionCard(card);
                        }
                    } else if (key.equals("CURRENT_PLAYER_INDEX")) {
                        currentPlayerIndex = Integer.parseInt(value);
                    }
                }
            }
        }
        
        reader.close();
        
        if (loadedPlayerCount > 0) {
            players = new Player[loadedPlayerCount];
            for (int i = 0; i < loadedPlayerCount; i++) {
                players[i] = loadedPlayers[i];
            }
            slowPrint("Game loaded successfully with " + players.length + " players");
        } else {
            slowPrint("No player data found in save file");
        }
    } catch (Exception e) {
        slowPrint("Error loading game: " + e.getMessage());
        e.printStackTrace();
    }
    pressEnterToContinue();
}
private Career findCareerByName(String name) {
    for (int i = 0; i < nonDegreeCareers.length; i++) {
        Career career = nonDegreeCareers[i];
        if (career.getName().equals(name)) {
            return career;
        }
    }
    for (int i = 0; i < degreeCareers.length; i++) {
        Career career = degreeCareers[i];
        if (career.getName().equals(name)) {
            return career;
        }
    }
    return null;
}

private House findHouseByName(String name) {
    for (int i = 0; i < availableHouses.length; i++) {
        House house = availableHouses[i];
        if (house.getName().equals(name)) {
            return house;
        }
    }
    return null;
}

private ActionCard findActionCardByTitle(String title) {
    for (int i = 0; i < actionCardDeck.length; i++) {
        ActionCard card = actionCardDeck[i];
        if (card.getTitle().equals(title)) {
            return card;
        }
    }
    ActionCard[] allCards = ActionCard.createActionCards();
    for (int i = 0; i < allCards.length; i++) {
        ActionCard card = allCards[i];
        if (card.getTitle().equals(title)) {
            return card;
        }
    }
    return null;
}
}

