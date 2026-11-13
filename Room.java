import java.util.ArrayList;
import java.util.List;

public class Room {
    private final String name;
    private final String description;
    private final Suspect suspect;
    private final List<Clue> clues = new ArrayList<>();

    public Room(String name, String description, Suspect suspect) {
        this.name = name;
        this.description = description;
        this.suspect = suspect;
    }

    public void addClue(Clue c) { clues.add(c); }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Suspect getSuspect() { return suspect; }
    public List<Clue> getClues() { return clues; }
}
