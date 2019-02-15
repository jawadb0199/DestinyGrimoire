package jawadbraick.destinygrimoire;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        try {
            final TextView loreText = view.findViewById(R.id.loreText);
            final Thread getLore = new Thread(new Runnable(){
                @Override
                public void run(){
                    try {
                        ArrayList<Long> args = new ArrayList<>();
                        args.add(info.getLoreId());
                        LoreDefinition loreDefinition = database.getDao().getLoreById(args).get(0);
                        String text = loreDefinition.getJson().getAsJsonObject("displayProperties").get("description").getAsString();
                        loreText.setText(text);

                    } catch (Exception e) {
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                Toast.makeText(getActivity(), "Error accessing database", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
            getLore.start();

            ImageView icon = view.findViewById(R.id.loreIcon);
            icon.setImageResource(info.getIconId());

            TextView title = view.findViewById(R.id.loreTitle);
            title.setText(info.getRecordName());

            ImageView background = view.findViewById(R.id.background);
            background.setImageResource(getImageResource(info.getNodeName(), true));

            getLore.join();

            return view;
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error Loading Lore", Toast.LENGTH_LONG).show();
             return view;
        }

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
