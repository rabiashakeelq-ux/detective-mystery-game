import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final List<Room> rooms = new ArrayList<>();
    private final List<Suspect> suspects = new ArrayList<>();
    private final List<Clue> inventory = new ArrayList<>();
    private final Scanner sc = new Scanner(System.in);
    private Room current;
    private boolean running = true;

    private void setup() {
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
        current = study;
    }

    public void start() {
        setup();
        UI.banner();
        UI.line();
        UI.type("Lord Blackwood has been murdered in his own mansion...", UI.YELLOW, 40);
        UI.type("Four suspects. One killer. You are the detective.", UI.YELLOW, 40);
        UI.line();

        while (running) {
            showMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> changeRoom();
                case "2" -> searchRoom();
                case "3" -> talk();
                case "4" -> showInventory();
                case "5" -> accuse();
                case "6" -> running = false;
                default -> System.out.println(UI.RED + "Invalid choice." + UI.RESET);
            }
        }
        System.out.println("Thanks for playing!");
    }

    private void showMenu() {
        UI.line();
        System.out.println(UI.BOLD + "Location: " + UI.GREEN + current.getName() + UI.RESET);
        System.out.println(current.getDescription());
        UI.line();
        System.out.println("1. Go to another room");
        System.out.println("2. Search this room");
        System.out.println("3. Talk to suspect here");
        System.out.println("4. View clues (" + inventory.size() + ")");
        System.out.println("5. Accuse the killer");
        System.out.println("6. Quit");
        System.out.print("> ");
    }

    private void changeRoom() {
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println((i + 1) + ". " + rooms.get(i).getName());
        }
        System.out.print("Where to? > ");
        int idx = readNumber() - 1;
        if (idx >= 0 && idx < rooms.size()) {
            current = rooms.get(idx);
            UI.type("You walk into the " + current.getName() + "...", UI.CYAN, 30);
        } else {
            System.out.println(UI.RED + "No such room." + UI.RESET);
        }
    }

    private void searchRoom() {
        UI.type("Searching...", UI.YELLOW, 80);
        UI.pause(500);
        boolean foundAny = false;
        for (Clue c : current.getClues()) {
            if (!c.isFound()) {
                c.markFound();
                inventory.add(c);
                foundAny = true;
                System.out.println(UI.GREEN + "CLUE FOUND: " + c + UI.RESET);
            }
        }
        if (!foundAny) System.out.println("Nothing new here.");
    }

    private void talk() {
        Suspect s = current.getSuspect();
        if (s == null) {
            System.out.println("Nobody is here.");
            return;
        }
        UI.type(s.getName() + " the " + s.getRole() + " says:", UI.CYAN, 30);
        UI.type(s.interrogate(), UI.RESET, 25);   // polymorphism
    }

    private void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("No clues yet.");
            return;
        }
        for (Clue c : inventory) System.out.println(UI.YELLOW + "- " + c + UI.RESET);
    }

    private void accuse() {
        if (inventory.size() < 3) {
            System.out.println(UI.RED + "Not enough evidence! Find at least 3 clues." + UI.RESET);
            return;
        }
        for (int i = 0; i < suspects.size(); i++) {
            System.out.println((i + 1) + ". " + suspects.get(i));
        }
        System.out.print("Who is the killer? > ");
        int idx = readNumber() - 1;
        if (idx < 0 || idx >= suspects.size()) {
            System.out.println(UI.RED + "Invalid suspect." + UI.RESET);
            return;
        }
        UI.type("You point at " + suspects.get(idx).getName() + "...", UI.YELLOW, 70);
        UI.pause(1000);
        if (suspects.get(idx) instanceof Culprit) {
            UI.type("CORRECT! Clara confesses. Case closed, detective!", UI.GREEN, 40);
        } else {
            UI.type("WRONG! The real killer escapes into the night...", UI.RED, 40);
        }
        running = false;
    }

    private int readNumber() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
