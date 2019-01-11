package jawadbraick.destinygrimoire;

public class PresentationNodeInfo{
    private int iconId;
    private String nodeName;
    private long nodeId;


    public PresentationNodeInfo(int iconId, String nodeName){
        this.iconId = iconId;
        this.nodeName = nodeName;
    }

    public PresentationNodeInfo(int iconId, String nodeName, long nodeId){
        this.iconId = iconId;
        this.nodeName = nodeName;
        this.nodeId = nodeId;
    }


    public int getIconId(){
        return iconId;
    }

    public String getNodeName(){
        return nodeName;
    }

    public long getNodeId(){
        return nodeId;
    }
}
