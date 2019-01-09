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

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordsFragment extends Fragment{
    RecyclerView recyclerView;
    RecordAdapter recordAdapter;
    ManifestDatabase database;

    String name;
    int iconId;
    long[] recordIds;

    public RecordsFragment(){
        database = ManifestDatabase.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        ImageView icon = (ImageView) view.findViewById(R.id.bookIcon);
        iconId = getImageResource(name);
        icon.setImageResource(iconId);

        TextView header = (TextView) view.findViewById(R.id.bookNameText);
        header.setText(name);

        recyclerView = view.findViewById(R.id.recordList);
        recordAdapter = new RecordAdapter(getActivity(), createRecordInfoList());
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRecordIds(long[] recordIds){
        this.recordIds = recordIds;
    }

    private List<RecordInfo> createRecordInfoList(){
        final ArrayList<RecordInfo> recordInfoList = new ArrayList<>();
        Collections.synchronizedList(recordInfoList);

        ThreadGroup getRecordInfo = new ThreadGroup("getRecordInfo");

        for(int i = 1; i < recordIds.length; i++){
            final long id = recordIds[i];
            new Thread(getRecordInfo, new Runnable(){
                @Override
                public void run(){
                    ArrayList<Long> args = new ArrayList<>();
                    args.add(id);
                    RecordDefinition record = database.getDao().getRecordById(args).get(0);
                    JsonObject json = record.getJson();

                    String loreName = json.getAsJsonObject("displayProperties").get("name").getAsString();
                    long loreId = convertHash(json.get("loreHash").getAsLong());

                    recordInfoList.add(new RecordInfo(iconId, loreName, loreId));

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
        String fileName = name.replaceAll("[^a-zA-Z0-9 ]", "").replaceAll(" ", "_").toLowerCase();
        return getResources().getIdentifier(fileName, "drawable", getActivity().getPackageName());
    }
}
