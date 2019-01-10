package jawadbraick.destinygrimoire;

public class RecordInfo extends PresentationNodeInfo{
    private long loreId;

    public RecordInfo(int iconId, String name, long loreId){
        super(iconId, name);
        this.loreId = loreId;
    }

    public long getLoreId(){
        return loreId;
    }
}
