package jawadbraick.destinygrimoire;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoreFragment extends Fragment{
    private ManifestDatabase database;

    public LoreFragment(){
        database = ManifestDatabase.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_lore, container, false);
        return view;
    }
}
