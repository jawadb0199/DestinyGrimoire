package jawadbraick.destinygrimoire;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {PresentationNodeDefinition.class, RecordDefinition.class }, version = 2)
@TypeConverters({Converters.class})
public abstract class ManifestDatabase extends RoomDatabase{
    public abstract DAO getDao();
}
