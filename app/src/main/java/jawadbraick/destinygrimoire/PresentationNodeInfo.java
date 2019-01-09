package jawadbraick.destinygrimoire;

public class PresentationNodeInfo{
    private int iconId;
    private String name;

    public PresentationNodeInfo(int iconId, String name){
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