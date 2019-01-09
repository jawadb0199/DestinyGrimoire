package jawadbraick.destinygrimoire;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordsFragment extends Fragment{
    String name;
    long[] recordIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        ImageView icon = (ImageView) view.findViewById(R.id.bookIcon);
        icon.setImageResource(getImageResource(name));

        TextView header = (TextView) view.findViewById(R.id.bookNameText);
        header.setText(name);

        return view;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRecordIds(long[] recordIds){
        this.recordIds = recordIds;
    }

    private int getImageResource(String name){
        String fileName = name.replaceAll("[^a-zA-Z0-9 ]", "").replaceAll(" ", "_").toLowerCase();
        return getResources().getIdentifier(fileName, "drawable", getActivity().getPackageName());
    }
}
