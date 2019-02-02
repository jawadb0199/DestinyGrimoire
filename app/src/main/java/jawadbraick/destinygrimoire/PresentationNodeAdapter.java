package jawadbraick.destinygrimoire;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PresentationNodeAdapter extends RecyclerView.Adapter<PresentationNodeAdapter.PresentationNodeViewHolder>{
    private LayoutInflater inflater;
    List<PresentationNodeInfo> presentationNodeData;
    private ManifestDatabase database;
    private ConcurrentHashMap<String, long[]> childIdMap;
    private ThreadGroup childIdThreads;
    private FragmentManager fm;

    public PresentationNodeAdapter(Context context, List<PresentationNodeInfo> presentationNodeData, FragmentManager fm){
        super();
        this.inflater = LayoutInflater.from(context);
        this.presentationNodeData = presentationNodeData;
        this.database = ManifestDatabase.getInstance(context);
        this.fm = fm;
        loadChildIdMap();
    }


    @Override
    public PresentationNodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.presentation_node_row, parent, false);
        PresentationNodeViewHolder holder = new PresentationNodeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(PresentationNodeViewHolder holder, int position){
        PresentationNodeInfo current = presentationNodeData.get(position);

        holder.recordIcon.setImageResource(current.getIconId());
        holder.recordText.setText(current.getNodeName());
    }

    @Override
    public int getItemCount(){
        return presentationNodeData.size();
    }

    class PresentationNodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView recordIcon;
        TextView recordText;


        public PresentationNodeViewHolder(View itemView){
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
            RecordsFragment frag = new RecordsFragment();
            frag.setName(name);
            frag.setRecordIds(childIdMap.get(name));
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.loreRecordsActivity, frag, "RecordsFragment").addToBackStack(null).commit();
        }
    }

    private void loadChildIdMap(){
        childIdMap = new ConcurrentHashMap<>();
        childIdThreads = new ThreadGroup("childIdThreads");

        for(int i = 0; i < presentationNodeData.size(); i++){

            final String name = presentationNodeData.get(i).getNodeName();
            final long id = presentationNodeData.get(i).getNodeId();

            new Thread(childIdThreads, new Runnable(){
                @Override
                public void run(){
                    try {
                        ArrayList<Long> args = new ArrayList<>();
                        args.add(id);
                        PresentationNodeDefinition node = database.getDao().getPresentationNodeById(args).get(0);
                        JsonObject json = node.getJson();
                        JsonArray childNodes = json.getAsJsonObject("children").getAsJsonArray("records");

                        long[] ids = new long[childNodes.size()];
                        for (int j = 0; j < childNodes.size(); j++) {
                            JsonObject child = (JsonObject) childNodes.get(j);
                            long hash = Long.parseLong(child.get("recordHash").getAsString());
                            ids[j] = convertHash(hash);
                        }
                        childIdMap.put(name, ids);
                    } catch (Exception e){
                        return;
                    }
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
