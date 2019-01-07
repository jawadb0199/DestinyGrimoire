package jawadbraick.destinygrimoire;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.JsonObject;

public abstract class ManifestDefintion{
    @PrimaryKey
    protected int id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    protected JsonObject json;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public JsonObject getJson(){
        return json;
    }

    public void setJson(JsonObject json){
        this.json = json;
    }
}
