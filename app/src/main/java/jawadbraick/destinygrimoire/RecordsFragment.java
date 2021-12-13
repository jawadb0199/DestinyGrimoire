package jawadbraick.destinygrimoire;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecordsFragment extends Fragment{
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private ManifestDatabase database;

    private String name;
    private int iconId;
    private long[] recordIds;

    public RecordsFragment(){
        database = ManifestDatabase.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        try {

            ImageView icon = (ImageView) view.findViewById(R.id.bookIcon);
            iconId = getImageResource(name);
            icon.setImageResource(iconId);

            TextView header = (TextView) view.findViewById(R.id.bookNameText);
            header.setText(name);

            recyclerView = view.findViewById(R.id.recordList);
            recordAdapter = new RecordAdapter(getActivity(), createRecordInfoList(), getFragmentManager());
            recyclerView.setAdapter(recordAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            return view;
        } catch (Exception e){
            Toast.makeText(getActivity(), "Error Loading Records", Toast.LENGTH_LONG).show();
            return view;
        }
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRecordIds(long[] recordIds){
        this.recordIds = recordIds;
    }

    private List<RecordInfo> createRecordInfoList(){
        final ArrayList<RecordInfo> recordInfoList = new ArrayList<>(Arrays.asList(new RecordInfo[recordIds.length]));
        Collections.synchronizedList(recordInfoList);

        ThreadGroup getRecordInfo = new ThreadGroup("getRecordInfo");

        for(int i = 0; i < recordIds.length; i++){
            final int index = i;
            final long id = recordIds[i];

            new Thread(getRecordInfo, new Runnable(){
                @Override
                public void run(){
                    try {
                        ArrayList<Long> args = new ArrayList<>();
                        args.add(id);
                        RecordDefinition record = database.getDao().getRecordById(args).get(0);
                        JsonObject json = record.getJson();

                        String loreName = json.getAsJsonObject("displayProperties").get("name").getAsString();
                        if (json.get("loreHash") == null) {
                            recordInfoList.set(index, null);
                            return;
                        }
                        long loreId = convertHash(json.get("loreHash").getAsLong());

                        recordInfoList.set(index, new RecordInfo(iconId, name, loreName, loreId));

                    } catch (Exception e){

                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                Toast.makeText(getActivity(), "Error accessing Record database", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }).start();
        }

        while (getRecordInfo.activeCount() > 0){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (recordInfoList.get(0) == null) {
            recordInfoList.remove(0);
        }

        if(name.equals("Wall of Wishes")){
            recordInfoList.add(new RecordInfo(iconId, name, "Fifteenth Wish", 1646244851));
        }

        return recordInfoList;

    }

    private long convertHash(long hash){
        final long offset = 4294967296L;
        if ((hash & (offset/2)) != 0){
            return hash-offset;
        } else {
            return hash;
        }
    }

    private int getImageResource(String name){
        String fileName = name.replaceAll("[ -]+", "_").replaceAll("[^a-zA-Z0-9_]", "").toLowerCase();
        return getResources().getIdentifier(fileName, "drawable", getActivity().getPackageName());
    }
}
