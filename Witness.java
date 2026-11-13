public class Witness extends Suspect {
    private final String hint;

    public Witness(String name, String role, String alibi, String hint) {
        super(name, role, alibi);
        this.hint = hint;
    }

    @Override
    public String interrogate() {
        return "\"" + alibi + " " + hint + "\"";
    }
}
