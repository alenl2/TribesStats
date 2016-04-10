package si.komp.tribesascendstats;
public class StatItem {
    private final String value;
    private final String name;
    private final String extra;

    StatItem(String name, String value, String extra){
        this.value = value;
        this.name = name;
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }
    public String getValue() {
        return value;
    }
    public String getName() {
        return name;
    }
}
