package jawadbraick.destinygrimoire;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huma.room_for_asset.RoomAsset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoreRecordsActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private ManifestDatabase database;
    private Thread getNodeChildrenThread;
    private HashMap<String, long[]> bookMap = new HashMap<>();
    private final String[] NODE_NAMES = {"The Light", "Dusk and Dawn", "The Darkness"};
    private ImageButton prevView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lore_records);

        database = RoomAsset.databaseBuilder(this, ManifestDatabase.class, "destiny2_manifest_lore.db").allowMainThreadQueries().build();

        getNodeChildrenThread = new NodeChildrenThread(NODE_NAMES);
        getNodeChildrenThread.start();

//        LayoutInflater inflater = this.getLayoutInflater();
        recyclerView = (RecyclerView) findViewById(R.id.bookList);

        ArrayList<RecordInfo> recordData = (ArrayList) getRecordData();
        recordAdapter = new RecordAdapter(this, recordData);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        prevView = (ImageButton) findViewById(R.id.theLightButton);
        prevView.setBackgroundTintList(getResources().getColorStateList(R.color.darkTint));

    }

    private List<RecordInfo> getRecordData(){
        int[] iconIds = {R.drawable.the_lawless_frontier,
                         R.drawable.the_man_they_call_cayde,
                         R.drawable.most_loyal,
                         R.drawable.ghost_stories,
                         R.drawable.evas_journey,
                         R.drawable.dawning_delights};
        String[] names = {"The Lawless Frontier",
                          "The Man They Call Cayde",
                          "Most Loyal",
                          "Ghost Stories",
                          "Eva's Journey",
                          "Dawning Delights"};

        ArrayList<RecordInfo> recordInfoList = new ArrayList<>();
        for(int i = 0; i < iconIds.length; i++){
            RecordInfo current = new RecordInfo(iconIds[i], names[i]);
            recordInfoList.add(current);
        }

        return recordInfoList;

    }

    public void showBooks(View view){
        prevView.setBackgroundTintList(null);
        prevView = (ImageButton) view;
        view.setBackgroundTintList(getResources().getColorStateList(R.color.darkTint));

        String name = view.getTag().toString();

        try {
            getNodeChildrenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long[] nodes = bookMap.get(name);
//        long[] nodes = {1582800871L, -1232389968L};
//        String[] nodeIds = {"'1582800871'", "'-1232389968'"};
        ArrayList<RecordInfo> recordInfoList = new ArrayList<>();
        ArrayList<Long> nodeId = new ArrayList<>(1);
        nodeId.add(0L);
        Log.i("Theme: ", name);

        for(int i = 0; i < nodes.length; i++){

            nodeId.set(0, nodes[i]);
            List<PresentationNode> list = database.getDao().getPresentationNodeById(nodeId);
            JsonObject json = list.get(0).getJson();
            String bookName = json.getAsJsonObject("displayProperties").get("name").getAsString();
            if(bookName.equals("Classified")){
                continue;
            }
            int bookImg = getImageResource(bookName);
            recordInfoList.add(new RecordInfo(bookImg, bookName));

            Log.i("Book: ", bookName);
        }

        // FIXME SQL exception: syntax error near ',' when list > 1
//        long[] nodeIds = bookMap.get(name);
//        List<PresentationNode> list = database.getDao().getPresentationNodeById(nodeIds);
//
//        for(PresentationNode n: list){
//            JsonObject json = n.getJson();
//            String bookName = json.getAsJsonObject("displayProperties").get("name").getAsString();
//            Log.i("Book: ", bookName);
//        }

        recordAdapter = new RecordAdapter(this, recordInfoList);
        recyclerView.setAdapter(recordAdapter);

    }

    private class NodeChildrenThread extends Thread{
        String[] names;

        NodeChildrenThread(String[] names){
            this.names = names;
        }
        @Override
        public void run(){
            for(String name: names){
                PresentationNode light = database.getDao().getPresentationNodeByText("%" + name + "%").get(0);
                JsonObject json = light.getJson();
                JsonArray childNodes = json.getAsJsonObject("children").getAsJsonArray("presentationNodes");

                long[] ids = new long[childNodes.size()];
                for(int i = 0; i < childNodes.size(); i++){
                    JsonObject child = (JsonObject) childNodes.get(i);
                    long hash  = Long.parseLong(child.get("presentationNodeHash").getAsString());
                    ids[i] = convertHash(hash);
                }
                bookMap.put(name, ids);
            }
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
    private int getImageResource(String name){
        Resources resources = this.getResources();
        String fileName = name.replaceAll("[^a-zA-Z0-9 ]", "").replaceAll(" ", "_").toLowerCase();
        return resources.getIdentifier(fileName, "drawable", this.getPackageName());
    }
}
