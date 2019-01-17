package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoreRecordsActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private PresentationNodeAdapter presentationNodeAdapter;
    private ManifestDatabase database;
    private Thread getNodeChildrenThread;
    private HashMap<String, long[]> bookMap = new HashMap<>();
    private final String[] NODE_NAMES = {"The Light", "Dusk and Dawn", "The Darkness"};
    private ImageButton prevView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        Boolean isDarkThemeEnabled = getSharedPreferences("userData", Context.MODE_PRIVATE).getBoolean("isDarkThemeEnabled", false);
        if(isDarkThemeEnabled){
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lore_records);

        SwitchCompat theme = (SwitchCompat) findViewById(R.id.themeSwitch);
        if(isDarkThemeEnabled){
            theme.setChecked(true);
            theme.setText("Dark Theme");
        }
        database = ManifestDatabase.getInstance(this);

        getNodeChildrenThread = new NodeChildrenThread(NODE_NAMES);
        getNodeChildrenThread.start();

//        LayoutInflater inflater = this.getLayoutInflater();
        recyclerView = (RecyclerView) findViewById(R.id.bookList);

        ArrayList<PresentationNodeInfo> presentationNodeData = (ArrayList) getRecordData();
        presentationNodeAdapter = new PresentationNodeAdapter(this, presentationNodeData, getFragmentManager());
        recyclerView.setAdapter(presentationNodeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        prevView = (ImageButton) findViewById(R.id.theLightButton);
        prevView.setBackgroundTintList(getResources().getColorStateList(R.color.darkTint));

        ArrayList<Long> args = new ArrayList<>();
        args.add(-2119364226L);
        RecordDefinition record = database.getDao().getRecordById(args).get(0);
        JsonObject recordJson = record.getJson();
        String recordName = recordJson.getAsJsonObject("displayProperties").get("name").getAsString();
        Log.i("Record: ", recordName);

    }

    private List<PresentationNodeInfo> getRecordData(){
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
        long[] ids = {-1847159559L, 396866327L, 648415847L, 1420597821L, 335014236L, -822671482L};

        ArrayList<PresentationNodeInfo> presentationNodeInfoList = new ArrayList<>();
        for(int i = 0; i < iconIds.length; i++){
            PresentationNodeInfo current = new PresentationNodeInfo(iconIds[i], names[i], ids[i]);
            presentationNodeInfoList.add(current);
        }

        return presentationNodeInfoList;

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
        ArrayList<PresentationNodeInfo> presentationNodeInfoList = new ArrayList<>();
        ArrayList<Long> nodeId = new ArrayList<>(1);
        nodeId.add(0L);
        Log.i("Theme: ", name);

        for(int i = 0; i < nodes.length; i++){

            nodeId.set(0, nodes[i]);
            List<PresentationNodeDefinition> list = database.getDao().getPresentationNodeById(nodeId);
            JsonObject json = list.get(0).getJson();
            String bookName = json.getAsJsonObject("displayProperties").get("name").getAsString();
            if(bookName.equals("Classified")){
                continue;
            }
            int bookImg = getImageResource(bookName);
            presentationNodeInfoList.add(new PresentationNodeInfo(bookImg, bookName, list.get(0).getId()));

            Log.i("Book: ", bookName);
        }

        // FIXME SQL exception: syntax error near ',' when list > 1
//        long[] nodeIds = bookMap.get(name);
//        List<PresentationNodeDefinition> list = database.getDao().getPresentationNodeById(nodeIds);
//
//        for(PresentationNodeDefinition n: list){
//            JsonObject json = n.getJson();
//            String bookName = json.getAsJsonObject("displayProperties").get("name").getAsString();
//            Log.i("Book: ", bookName);
//        }

        presentationNodeAdapter = new PresentationNodeAdapter(this, presentationNodeInfoList, getFragmentManager());
        recyclerView.setAdapter(presentationNodeAdapter);

    }

    public void toggleTheme(View view){
        SwitchCompat theme = (SwitchCompat) view;
        if(theme.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
            editor.putBoolean("isDarkThemeEnabled", true).apply();
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
            theme.setText("Dark Theme");
        } else {
            SharedPreferences.Editor editor = getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
            editor.putBoolean("isDarkThemeEnabled", false).apply();
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
            theme.setText("Light Theme");
        }

        recreate();
    }

    private class NodeChildrenThread extends Thread{
        String[] names;

        NodeChildrenThread(String[] names){
            this.names = names;
        }
        @Override
        public void run(){
            for(String name: names){
                PresentationNodeDefinition light = database.getDao().getPresentationNodeByText("%" + name + "%").get(0);
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
