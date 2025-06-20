public class House {
    private String name;
    private int buyPrice;
    private int sellPriceRed;
    private int sellPriceBlack;
    
    public House(String name, int buyPrice, int sellPriceRed, int sellPriceBlack) {
        this.name = name;
        this.buyPrice = buyPrice;
        this.sellPriceRed = sellPriceRed;
        this.sellPriceBlack = sellPriceBlack;
    }
    
    public String getName() { 
        return name; 
    }
    public int getBuyPrice() { 
        return buyPrice; 
    }
    public int getSellPriceRed() { 
        return sellPriceRed; 
    }
    public int getSellPriceBlack() { 
        return sellPriceBlack; 
}


    public String toString() {
        return name + " (Buy: $" + buyPrice +
               ", Sell Red: $" + sellPriceRed +
               ", Sell Black: $" + sellPriceBlack + ")";
    }
    public static House[] createHouses() {
        return new House[] {
          new House("Starter Home", 80000, 60000, 100000),
            new House("Apartment", 60000, 45000, 80000),
            new House("Ranch House", 120000, 130000, 80000),
            new House("Colonial House", 150000, 160000, 110000),
            new House("Tudor House", 180000, 195000, 140000),
            new House("Victorian House", 200000, 180000, 250000),
            new House("Modern House", 220000, 270000, 180000),
            new House("Mansion", 300000, 320000, 260000),
            new House("Beach House", 250000, 230000, 280000),
            new House("Mountain Cabin", 180000, 200000, 140000)
        };
    }
}
