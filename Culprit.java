public class Culprit extends Suspect {
    public Culprit(String name, String role, String alibi) {
        super(name, role, alibi);
    }

    @Override
    public String interrogate() {
        return "\"" + alibi + "\" ... (" + getName() + " avoids your eyes and fidgets nervously)";
    }
}
