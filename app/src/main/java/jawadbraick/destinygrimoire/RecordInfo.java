package jawadbraick.destinygrimoire;

public class RecordInfo extends PresentationNodeInfo{
    private String recordName;
    private long loreId;

    public RecordInfo(int iconId, String nodeName, String recordName, long loreId){
        super(iconId, nodeName);
        this.recordName = recordName;
        this.loreId = loreId;
    }

    public String getRecordName(){
        return recordName;
    }

    public long getLoreId(){
        return loreId;
    }
}
