public class Clue {
    private final String name;
    private final String description;
    private boolean found;

    public Clue(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isFound() { return found; }
    public void markFound() { found = true; }

    @Override
    public String toString() { return name + " - " + description; }
}
