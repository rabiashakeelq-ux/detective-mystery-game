public abstract class Suspect implements Interrogatable {
    private final String name;
    private final String role;
    protected final String alibi;

    public Suspect(String name, String role, String alibi) {
        this.name = name;
        this.role = role;
        this.alibi = alibi;
    }

    public String getName() { return name; }
    public String getRole() { return role; }

    @Override
    public abstract String interrogate();

    @Override
    public String toString() { return name + " (" + role + ")"; }
}
