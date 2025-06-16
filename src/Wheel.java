import java.util.Random;

public class Wheel {
    private Random random;
    
    public Wheel() {
        this.random = new Random();
    }
    
    public int spin() {
        return random.nextInt(10) + 1;
    }
    
    public boolean isRed(int number) {
        if (number % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public String getColor(int number) {
        if (isRed(number)) {
            return "RED";
        } else {
            return "BLACK";
        }
    }
}
