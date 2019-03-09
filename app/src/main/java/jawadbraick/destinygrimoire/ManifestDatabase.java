package jawadbraick.destinygrimoire;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.huma.room_for_asset.RoomAsset;

@Database(entities = {PresentationNodeDefinition.class, RecordDefinition.class, LoreDefinition.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class ManifestDatabase extends RoomDatabase{
    private static volatile ManifestDatabase instance;

    public static ManifestDatabase getInstance(Context context){
        if (instance == null){
            initDatabase(context);
        }
        return instance;
    }
    private static void initDatabase(Context context){
        synchronized (ManifestDatabase.class) {
            if (instance == null) {
                int appVersion = PreferenceManager.getDefaultSharedPreferences(context).getInt("appVersion", -1);

                if (appVersion != BuildConfig.VERSION_CODE){
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    editor.putInt("appVersion", BuildConfig.VERSION_CODE);
                    editor.apply();
                    editor.putBoolean("instantiated", false);
                    editor.commit();
                    context.deleteDatabase("destiny2_manifest_lore.db");
                }
                instance = RoomAsset.databaseBuilder(context, ManifestDatabase.class, "destiny2_manifest_lore.db").build();
            }
        }
    }
    public abstract DAO getDao();
}
