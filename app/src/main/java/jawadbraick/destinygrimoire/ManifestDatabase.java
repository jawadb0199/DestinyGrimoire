package jawadbraick.destinygrimoire;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.huma.room_for_asset.RoomAsset;

@Database(entities = {PresentationNodeDefinition.class, RecordDefinition.class }, version = 2)
@TypeConverters({Converters.class})
public abstract class ManifestDatabase extends RoomDatabase{
    private static volatile ManifestDatabase instance;

    public static ManifestDatabase getInstance(Context context){
        if (instance == null){
            synchronized (ManifestDatabase.class) {
                if (instance == null) {
                    instance = RoomAsset.databaseBuilder(context, ManifestDatabase.class, "destiny2_manifest_lore.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
    private static void initDatabase(){}
    public abstract DAO getDao();
}
