public class ActionCard {
    private String title;
    private String description;
    private int moneyEffect;
    private boolean isGroupSpin;
    private boolean affectsAllPlayers;
    
    public ActionCard(String title, String description, int moneyEffect) {
        this.title = title;
        this.description = description;
        this.moneyEffect = moneyEffect;
        this.isGroupSpin = false;
        this.affectsAllPlayers = false;
    }
    
    public ActionCard(String title, String description, int moneyEffect, boolean isGroupSpin, boolean affectsAllPlayers) {
        this.title = title;
        this.description = description;
        this.moneyEffect = moneyEffect;
        this.isGroupSpin = isGroupSpin;
        this.affectsAllPlayers = affectsAllPlayers;
    }
    
    public String getTitle() { 
        return title; 
    }
    public String getDescription() { 
        return description; 
    }
    public int getMoneyEffect() { 
        return moneyEffect; 
    }
    public boolean isGroupSpin() { 
        return isGroupSpin; 
    }
    public boolean affectsAllPlayers() { 
        return affectsAllPlayers; 
}
    
    public String toString() {
        return title + ": " + description;
    }
    
    public static ActionCard[] createActionCards() {
        return new ActionCard[] {
            new ActionCard("Birthday Gift", "Receive $10,000 from the bank", 10000),
            new ActionCard("Tax Refund", "Receive $15,000 from the bank", 15000),
            new ActionCard("Inheritance", "Receive $25,000 from the bank", 25000),
            new ActionCard("Lottery Winner", "Receive $30,000 from the bank", 30000),
            new ActionCard("Medical Bills", "Pay $8,000 to the bank", -8000),
            new ActionCard("Car Repair", "Pay $5,000 to the bank", -5000),
            new ActionCard("Speeding Ticket", "Pay $2,000 to the bank", -2000),
            new ActionCard("Home Repair", "Pay $12,000 to the bank", -12000),
            new ActionCard("Investment Success", "Receive $20,000 from the bank", 20000),
            new ActionCard("Stock Market Crash", "Pay $18,000 to the bank", -18000),
            new ActionCard("Bonus at Work", "Receive $8,000 from the bank", 8000),
            new ActionCard("Charity Donation", "Pay $5,000 to the bank", -5000),
            new ActionCard("Found Money", "Receive $3,000 from the bank", 3000),
            new ActionCard("Parking Ticket", "Pay $1,000 to the bank", -1000),
            new ActionCard("Side Business", "Receive $12,000 from the bank", 12000),
            new ActionCard("Computer Virus", "Pay $4,000 to the bank", -4000),
            new ActionCard("Wedding Gift", "Receive $7,000 from the bank", 7000),
            new ActionCard("Vacation Expense", "Pay $10,000 to the bank", -10000),
            new ActionCard("Garage Sale", "Receive $2,000 from the bank", 2000),
            new ActionCard("Insurance Claim", "Receive $15,000 from the bank", 15000),
            new ActionCard("Group Spin", "All players spin - highest gets spin × $10,000", 0, true, false),
            new ActionCard("Pay All Players", "Pay $2,000 to each other player", -2000, false, true),
            new ActionCard("Collect from All", "Collect $3,000 from each other player", 3000, false, true),
            new ActionCard("Lucky Day", "Receive $22,000 from the bank", 22000),
            new ActionCard("Bad Investment", "Pay $15,000 to the bank", -15000)
        };
    }
}
