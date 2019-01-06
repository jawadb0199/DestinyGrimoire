package jawadbraick.destinygrimoire;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.google.gson.JsonObject;

import java.util.List;

@Dao
public interface DAO{

    @Query("select * from DestinyPresentationNodeDefinition where id = (:nodeIds)")
    public List<PresentationNode> getPresentationNodeById(List<Long> nodeIds);

    @Query("select * from DestinyPresentationNodeDefinition where json like :s")
    public List<PresentationNode> getPresentationNodeByText(String s);
}
