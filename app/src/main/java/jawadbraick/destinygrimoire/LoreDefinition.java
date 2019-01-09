package jawadbraick.destinygrimoire;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.JsonObject;

@Entity(tableName = "DestinyLoreDefinition")
public class LoreDefinition extends ManifestDefintion{}
