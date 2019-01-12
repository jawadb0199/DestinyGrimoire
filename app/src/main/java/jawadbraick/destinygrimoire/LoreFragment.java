package jawadbraick.destinygrimoire;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LoreFragment extends Fragment{
    private ManifestDatabase database;
    private RecordInfo info;

    public LoreFragment(){
        database = ManifestDatabase.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_lore, container, false);

        final TextView loreText = view.findViewById(R.id.loreText);
        Thread getLore = new Thread(new Runnable(){
            @Override
            public void run(){
                ArrayList<Long> args = new ArrayList<>();
                args.add(info.getLoreId());
                LoreDefinition loreDefinition = database.getDao().getLoreById(args).get(0);
                String text = loreDefinition.getJson().getAsJsonObject("displayProperties").get("description").getAsString();
                loreText.setText(text);
            }
        });
        getLore.start();

        ImageView icon = view.findViewById(R.id.loreIcon);
        icon.setImageResource(info.getIconId());

        TextView title = view.findViewById(R.id.loreTitle);
        title.setText(info.getRecordName());

        ImageView background = view.findViewById(R.id.background);
        background.setImageResource(getImageResource(info.getNodeName(), true));

        try {
            getLore.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setInfo(RecordInfo info){
        this.info = info;
    }

    private int getImageResource(String name, boolean getLarge){
        if(getLarge){
            name += "_large";
        }

        return getImageResource(name);
    }
    private int getImageResource(String name){
        String fileName = name.replaceAll("[^a-zA-Z0-9_ ]", "").replaceAll(" ", "_").toLowerCase();
        return getResources().getIdentifier(fileName, "drawable", getActivity().getPackageName());
    }

}
