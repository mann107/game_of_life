import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int position;
    private int money;
    private Career career;
    private House[] houses;
    private int houseCount;
    private int children;
    private int loanAmount;
    private ActionCard[] actionCards;
    private int actionCardCount;
    private boolean isMarried;
    private boolean hasRetired;

    private static final int MAX_HOUSES = 10;
    private static final int MAX_ACTION_CARDS = 50;

    public Player(String name) {
        this.name = name;
        this.position = 0;
        this.money = 10000; // Starting money
        this.career = null;
        this.houses = new House[MAX_HOUSES];
        this.houseCount = 0;
        this.children = 0;
        this.loanAmount = 0;
        this.actionCards = new ActionCard[MAX_ACTION_CARDS];
        this.actionCardCount = 0;
        this.isMarried = false;
        this.hasRetired = false;
    }

    // Getters
    public String getName() { return name; }
    public int getPosition() { return position; }
    public int getMoney() { return money; }
    public Career getCareer() { return career; }
    public House[] getHouses() {
        House[] result = new House[houseCount];
        System.arraycopy(houses, 0, result, 0, houseCount);
        return result;
    }
    public int getChildren() { return children; }
    public int getLoanAmount() { return loanAmount; }
    public ActionCard[] getActionCards() {
        ActionCard[] result = new ActionCard[actionCardCount];
        System.arraycopy(actionCards, 0, result, 0, actionCardCount);
        return result;
    }
    public boolean isMarried() { return isMarried; }
    public boolean hasRetired() { return hasRetired; }

    // Setters
    public void setPosition(int position) { this.position = position; }
    public void setCareer(Career career) { this.career = career; }
    public void setMarried(boolean married) { this.isMarried = married; }
    public void setRetired(boolean retired) { this.hasRetired = retired; }

    // Money operations
    public void addMoney(int amount) {
        this.money += amount;
    }

    public boolean subtractMoney(int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            return true;
        }
        return false;
    }

    public void receiveSalary() {
        if (career != null) {
            addMoney(career.getSalary());
        }
    }

    // Loan operations
    public void takeLoan(int amount) {
        this.loanAmount += amount;
        this.money += amount;
    }

    public void payLoan(int amount) {
        int payment = Math.min(amount, loanAmount);
        if (subtractMoney(payment)) {
            loanAmount -= payment;
        }
    }

    public void payOffAllLoans() {
        if (money >= loanAmount) {
            money -= loanAmount;
            loanAmount = 0;
        }
    }

    // House operations
    public void buyHouse(House house) {
        if (houseCount < MAX_HOUSES && subtractMoney(house.getBuyPrice())) {
            houses[houseCount++] = house;
        }
    }

    public void sellHouse(House house, boolean isRed) {
        for (int i = 0; i < houseCount; i++) {
            if (houses[i] == house) {
                int sellPrice = isRed ? house.getSellPriceRed() : house.getSellPriceBlack();
                addMoney(sellPrice);
                // Remove house from array
                for (int j = i; j < houseCount - 1; j++) {
                    houses[j] = houses[j + 1];
                }
                houses[--houseCount] = null;
                break;
            }
        }
    }

    // Other operations
    public void addChild() {
        children++;
    }

    public void addActionCard(ActionCard card) {
        if (actionCardCount < MAX_ACTION_CARDS) {
            actionCards[actionCardCount++] = card;
        }
    }

    public void moveForward(int spaces) {
        position += spaces;
    }

    // Display methods
    public void displayStats() {
        System.out.println("\n--- " + name + "'s Stats ---");
        System.out.println("Position: " + position);
        System.out.println("Money: $" + String.format("%,d", money));
        System.out.println("Career: " + (career != null ? career.getName() + " ($" + String.format("%,d", career.getSalary()) + ")" : "None"));
        System.out.println("Houses: " + houseCount);
        for (int i = 0; i < houseCount; i++) {
            System.out.println("  - " + houses[i].getName());
        }
        System.out.println("Children: " + children);
        System.out.println("Married: " + (isMarried ? "Yes" : "No"));
        System.out.println("Loan Amount: $" + String.format("%,d", loanAmount));
        System.out.println("Action Cards: " + actionCardCount);
        System.out.println("------------------------");
    }

    public int calculateFinalMoney(boolean finalSpinIsRed) {
        int finalMoney = money;

        // Sell all houses based on final spin
        for (int i = 0; i < houseCount; i++) {
            House house = houses[i];
            int sellPrice = finalSpinIsRed ? house.getSellPriceRed() : house.getSellPriceBlack();
            finalMoney += sellPrice;
        }

        // Cash in action cards
        finalMoney += actionCardCount * 10000;

        // Children bonus
        finalMoney += children * 10000;

        // Pay off loans
        finalMoney -= loanAmount;

        return finalMoney;
    }
}