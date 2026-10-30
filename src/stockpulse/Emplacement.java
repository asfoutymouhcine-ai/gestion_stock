package stockpulse;

public class Emplacement {
    private String id;
    private String code;

    public Emplacement(String id, String code) {
        this.id = id;
        this.code = code;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @Override
    public String toString() {
        return code;
    }
}