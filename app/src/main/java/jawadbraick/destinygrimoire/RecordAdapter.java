package jawadbraick.destinygrimoire;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{
    private LayoutInflater inflater;
    List<RecordInfo> recordData = Collections.emptyList();
    private ManifestDatabase database;
    private ConcurrentHashMap<String, long[]> childIdMap;
    private ThreadGroup childIdThreads;

    public RecordAdapter(Context context, List<RecordInfo> recordData){
        super();
        this.inflater = LayoutInflater.from(context);
        this.recordData = recordData;
        this.database = ManifestDatabase.getInstance(context);
        loadChildIdMap();
    }


    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        RecordViewHolder holder = new RecordViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position){
        RecordInfo current = recordData.get(position);

        holder.recordIcon.setImageResource(current.getIconId());
        holder.recordText.setText(current.getName());
    }

    @Override
    public int getItemCount(){
        return recordData.size();
    }

    class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView recordIcon;
        TextView recordText;


        public RecordViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            recordIcon = itemView.findViewById(R.id.recordIcon);
            recordText = itemView.findViewById(R.id.recordText);
        }

        @Override
        public void onClick(View view){
            String name = recordText.getText().toString();
            while (childIdThreads.activeCount() > 0){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i("onClickChildRecords: ", Arrays.toString(childIdMap.get(name)));

        }
    }

    private void loadChildIdMap(){
        childIdMap = new ConcurrentHashMap<>();
        childIdThreads = new ThreadGroup("childIdThreads");

        for(int i = 0; i < recordData.size(); i++){

            final String name = recordData.get(i).getName();

            new Thread(childIdThreads, new Runnable(){
                @Override
                public void run(){
                    PresentationNodeDefinition node = database.getDao().getPresentationNodeByText("%" + name + "%").get(0);
                    JsonObject json = node.getJson();
                    JsonArray childNodes = json.getAsJsonObject("children").getAsJsonArray("records");

                    long[] ids = new long[childNodes.size()];
                    for(int j = 0; j < childNodes.size(); j++){
                        JsonObject child = (JsonObject) childNodes.get(j);
                        long hash  = Long.parseLong(child.get("recordHash").getAsString());
                        ids[j] = convertHash(hash);
                    }
                    childIdMap.put(name, ids);
                }
            }).start();
        }
    }

    private long convertHash(long hash){
        final long offset = 4294967296L;
        if ((hash & (offset/2)) != 0){
            return hash-offset;
        } else {
            return hash;
        }
    }

}
