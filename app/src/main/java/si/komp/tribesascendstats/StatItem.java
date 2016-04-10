package si.komp.tribesascendstats;

/**
 * Created by alen_ on 10. 04. 2016.
 */
public class StatItem {
    private String value;
    private String name;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    private String extra;

    StatItem(String name, String value){
        this.value = value;
        this.name = name;
    }

    StatItem(String name, String value, String extra){
        this(name,value);
        this.extra = extra;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
