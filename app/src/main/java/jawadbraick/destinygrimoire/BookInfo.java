package jawadbraick.destinygrimoire;

public class BookInfo {
    private int iconId;
    private String nodeName;
    private long nodeId;


    public BookInfo(int iconId, String nodeName){
        this.iconId = iconId;
        this.nodeName = nodeName;
    }

    public BookInfo(int iconId, String nodeName, long nodeId){
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
