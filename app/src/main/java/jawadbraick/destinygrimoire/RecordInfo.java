package jawadbraick.destinygrimoire;

public class RecordInfo{
    private int iconId;
    private String name;

    public RecordInfo(int iconId, String name){
        this.iconId = iconId;
        this.name = name;
    }

    public int getIconId(){
        return iconId;
    }

    public String getName(){
        return name;
    }
}
