package jawadbraick.destinygrimoire;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.JsonObject;

@Entity(tableName = "DestinyPresentationNodeDefinition")
public class PresentationNode{
    @PrimaryKey
    private long id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private JsonObject json;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public JsonObject getJson(){
        return json;
    }

    public void setJson(JsonObject json){
        this.json = json;
    }
}
