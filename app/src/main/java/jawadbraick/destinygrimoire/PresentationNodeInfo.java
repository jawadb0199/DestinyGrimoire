package jawadbraick.destinygrimoire;

public class PresentationNodeInfo{
    private int iconId;
    private String name;
    private long nodeId;


    public PresentationNodeInfo(int iconId, String name){
        this.iconId = iconId;
        this.name = name;
    }

    public PresentationNodeInfo(int iconId, String name, long nodeId){
        this.iconId = iconId;
        this.name = name;
        this.nodeId = nodeId;
    }


    public int getIconId(){
        return iconId;
    }

    public String getName(){
        return name;
    }

    public long getNodeId(){
        return nodeId;
    }
}
