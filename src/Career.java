public class Career {
    private String name;
    private int salary;
    private boolean requiresDegree;
    private String specialRule;
    
    public Career(String name, int salary, boolean requiresDegree) {
        this.name = name;
        this.salary = salary;
        this.requiresDegree = requiresDegree;
        this.specialRule = "";
    }
    
    public Career(String name, int salary, boolean requiresDegree, String specialRule) {
        this.name = name;
        this.salary = salary;
        this.requiresDegree = requiresDegree;
        this.specialRule = specialRule;
    }
    
    public String getName() { return name; }
    public int getSalary() { return salary; }
    public boolean requiresDegree() { return requiresDegree; }
    public String getSpecialRule() { return specialRule; }
    
    public String toString() {
        String result = name + " ($" + salary + ")";
        if (requiresDegree) {
            result += " [Degree Required]";
        }
        return result;
    }
    
    public static Career[] createNonDegreeCareers() {
        return new Career[] {
            new Career("Salesperson", 30000, false),
            new Career("Mechanic", 35000, false),
            new Career("Hair Stylist", 25000, false),
            new Career("Police Officer", 40000, false),
            new Career("Firefighter", 38000, false),
            new Career("Artist", 20000, false, "Collect $5000 when someone spins 1"),
            new Career("Musician", 22000, false, "Collect $3000 when someone spins 2"),
            new Career("Chef", 32000, false),
            new Career("Truck Driver", 45000, false),
            new Career("Construction Worker", 42000, false)
        };
    }
    
    public static Career[] createDegreeCareers() {
        return new Career[] {
            new Career("Doctor", 80000, true),
            new Career("Lawyer", 75000, true),
            new Career("Engineer", 65000, true),
            new Career("Teacher", 35000, true),
            new Career("Scientist", 60000, true),
            new Career("Architect", 70000, true),
            new Career("Veterinarian", 55000, true),
            new Career("Pharmacist", 58000, true),
            new Career("Accountant", 45000, true),
            new Career("Computer Programmer", 62000, true, "Collect $2000 when someone spins 5")
        };

        // The special rules for careers can be expanded or modified as needed. But we did not have time to implement them in the game logic.
    }
}
