package jawadbraick.destinygrimoire;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecordsFragment extends Fragment{
    String name;
    long[] recordIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRecordIds(long[] recordIds){
        this.recordIds = recordIds;
    }
}
