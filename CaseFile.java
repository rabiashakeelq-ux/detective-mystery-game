import java.util.ArrayList;
import java.util.List;

// Holds the story data (rooms, suspects, clues) for the GUI version
public class CaseFile {
    public final List<Room> rooms = new ArrayList<>();
    public final List<Suspect> suspects = new ArrayList<>();

    public CaseFile() {
        Suspect butler = new Witness("James", "Butler",
                "I was polishing silver in the hall all night.",
                "But I did smell lavender perfume near the study around 9:40.");
        Suspect chef = new Witness("Maria", "Chef",
                "I was cooking dinner in the kitchen.",
                "Miss Clara came asking for the tea tray, she seemed in a hurry.");
        Suspect gardener = new Witness("Tom", "Gardener",
                "I was working in the garden until late.",
                "My boots are always muddy, but I never went inside the house.");
        Suspect clara = new Culprit("Clara", "Niece",
                "I was reading in my room, I heard nothing!");

        suspects.add(butler);
        suspects.add(chef);
        suspects.add(gardener);
        suspects.add(clara);

        Room study = new Room("Study", "A dark room. Lord Blackwood lies still on the floor.", butler);
        study.addClue(new Clue("Broken Watch", "Stopped at 9:40 PM, the time of death."));
        study.addClue(new Clue("Lavender Scent", "A faint lavender perfume lingers in the air."));

        Room library = new Room("Library", "Tall shelves, dusty books, a cold fireplace.", clara);
        library.addClue(new Clue("Torn Will", "The new will gives everything to Clara, signed yesterday."));

        Room kitchen = new Room("Kitchen", "Copper pots hang above a warm stove.", chef);
        kitchen.addClue(new Clue("Poison Bottle", "An empty bottle, labelled with lavender oil."));

        Room garden = new Room("Garden", "Moonlight over wet roses and muddy soil.", gardener);
        garden.addClue(new Clue("Muddy Boots", "Size 11 prints, but they stop far from the house."));

        rooms.add(study);
        rooms.add(library);
        rooms.add(kitchen);
        rooms.add(garden);
    }
}
