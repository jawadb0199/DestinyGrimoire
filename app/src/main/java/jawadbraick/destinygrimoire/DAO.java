package jawadbraick.destinygrimoire;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DAO{
//    DestinyPresentationNodeDefinition
    @Query("select * from DestinyPresentationNodeDefinition where id = (:nodeIds)")
    public List<PresentationNodeDefinition> getPresentationNodeById(List<Long> nodeIds);

    @Query("select * from DestinyPresentationNodeDefinition where json like :s")
    public List<PresentationNodeDefinition> getPresentationNodeByText(String s);

//    DestinyRecordDefinition
    @Query("select * from DestinyRecordDefinition where id = (:nodeIds)")
    public List<RecordDefinition> getRecordById(List<Long> nodeIds);
}
