package jawadbraick.destinygrimoire;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.JsonObject;

@Entity(tableName = "DestinyPresentationNodeDefinition")
public class PresentationNodeDefinition extends ManifestDefintion{}
