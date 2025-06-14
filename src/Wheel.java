import java.util.Random;

/**
 * Represents the spinning wheel mechanism
 * Handles random number generation and red/black color determination
 */
public class Wheel {
    private Random random;
    
    public Wheel() {
        this.random = new Random();
    }
    
    /**
     * Spin the wheel and return a number between 1-10
     */
    public int spin() {
        return random.nextInt(10) + 1;
    }
    
    /**
     * Determine if a number is red or black
     * Even numbers are red, odd numbers are black
     */
    public boolean isRed(int number) {
        return number % 2 == 0;
    }
    
    /**
     * Get color name for display
     */
    public String getColor(int number) {
        return isRed(number) ? "RED" : "BLACK";
    }
}