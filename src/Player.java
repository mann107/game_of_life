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
        this.money = 10000;
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

    public String getName() { 
        return name; 
    }
    public int getPosition() { 
        return position; 
    }
    public int getMoney() { 
        return money; 
    }
    public Career getCareer() { 
        return career; 
    }
    
    public House[] getHouses() {
        House[] result = new House[houseCount];
        for (int i = 0; i < houseCount; i++) {
            result[i] = houses[i];
        }
        return result;
    }
    public int getChildren() { return children; }
    public int getLoanAmount() { return loanAmount; }
    public ActionCard[] getActionCards() {
        ActionCard[] result = new ActionCard[actionCardCount];
        for (int i = 0; i < actionCardCount; i++) {
            result[i] = actionCards[i];
        }
        return result;
    }
    public boolean isMarried() { return isMarried; }
    public boolean hasRetired() { return hasRetired; }

    public void setPosition(int position) { this.position = position; }
    public void setCareer(Career career) { this.career = career; }
    public void setMarried(boolean married) { this.isMarried = married; }
    public void setRetired(boolean retired) { this.hasRetired = retired; }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public boolean subtractMoney(int amount) {
        if (this.money >= amount) {
            this.money -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void receiveSalary() {
        if (career != null) {
            addMoney(career.getSalary());
        }
    }

    public void takeLoan(int amount) {
        this.loanAmount += amount;
    }

    public void payLoan(int amount) {
        int payment = Math.min(amount, loanAmount);
        loanAmount -= payment;
    }

    public void payOffAllLoans() {
        if (money >= loanAmount) {
            money -= loanAmount;
            loanAmount = 0;
        }
    }

    public void buyHouse(House house) {
        if (houseCount < MAX_HOUSES && subtractMoney(house.getBuyPrice())) {
            houses[houseCount++] = house;
        }
    }

    public void sellHouse(House house, boolean isRed) {
        for (int i = 0; i < houseCount; i++) {
            if (houses[i] == house) {
                int sellPrice;
                if (isRed) {
                    sellPrice = house.getSellPriceRed();
                } else {
                    sellPrice = house.getSellPriceBlack();
                }
                addMoney(sellPrice);
                for (int j = i; j < houseCount - 1; j++) {
                    houses[j] = houses[j + 1];
                }
                houses[--houseCount] = null;
            }
        }
    }

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

    public void displayStats() {
        System.out.println("\n--- " + name + "'s Stats ---");
        System.out.println("Position: " + position);
        System.out.println("Money: $" + money);
        if (career != null) {
            System.out.println("Career: " + career.getName() + " ($" + career.getSalary() + ")");
        } else {
            System.out.println("Career: None");
        }
        System.out.println("Houses: " + houseCount);
        for (int i = 0; i < houseCount; i++) {
            System.out.println("  - " + houses[i].getName());
        }
        System.out.println("Children: " + children);
        if (isMarried) {
            System.out.println("Married: Yes");
        } else {
            System.out.println("Married: No");
        }
        System.out.println("Loan Amount: $" + loanAmount);
        System.out.println("Action Cards: " + actionCardCount);
        System.out.println("------------------------");
    }

    public int calculateFinalMoney(boolean finalSpinIsRed) {
        int finalMoney = money;
        for (int i = 0; i < houseCount; i++) {
            House house = houses[i];
            int sellPrice;
            if (finalSpinIsRed) {
                sellPrice = house.getSellPriceRed();
            } else {
                sellPrice = house.getSellPriceBlack();
            }
            finalMoney += sellPrice;
        }
        finalMoney += actionCardCount * 10000;
        finalMoney += children * 10000;
        finalMoney -= loanAmount;
        return finalMoney;
    }
    
    public void setName(String name) {
    this.name = name;
}

public void setMoney(int money) {
    this.money = money;
}

public void setChildren(int children) {
    this.children = children;
}

public void setLoanAmount(int loanAmount) {
    this.loanAmount = loanAmount;
}
    
}
