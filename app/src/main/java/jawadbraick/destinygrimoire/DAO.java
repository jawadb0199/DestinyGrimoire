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
    @Query("select * from DestinyRecordDefinition where id = (:recordIds)")
    public List<RecordDefinition> getRecordById(List<Long> recordIds);

//    DestinyLoreDefintion
    @Query("select * from DestinyLoreDefinition where id = (:loreIds)")
    public List<LoreDefinition> getLoreById(List<Long> loreIds);
}
