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
    
    public String getName() { return name; }
    public int getBuyPrice() { return buyPrice; }
    public int getSellPriceRed() { return sellPriceRed; }
    public int getSellPriceBlack() { return sellPriceBlack; }


    public String toString() {
        return name + " (Buy: $" + buyPrice +
               ", Sell Red: $" + sellPriceRed +
               ", Sell Black: $" + sellPriceBlack + ")";
    }
    public static House[] createHouses() {
        return new House[] {
            new House("Starter Home", 80000, 60000, 40000),
            new House("Apartment", 60000, 45000, 30000),
            new House("Ranch House", 120000, 100000, 80000),
            new House("Colonial House", 150000, 130000, 110000),
            new House("Tudor House", 180000, 160000, 140000),
            new House("Victorian House", 200000, 180000, 160000),
            new House("Modern House", 220000, 200000, 180000),
            new House("Mansion", 300000, 280000, 260000),
            new House("Beach House", 250000, 230000, 210000),
            new House("Mountain Cabin", 180000, 160000, 140000)
        };
    }
}
