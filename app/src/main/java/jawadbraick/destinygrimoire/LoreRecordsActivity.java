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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LoreRecordsActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private PresentationNodeAdapter presentationNodeAdapter;
    private ManifestDatabase database;
    private Thread getNodeChildrenThread;
    private ConcurrentHashMap<String, long[]> bookMap = new ConcurrentHashMap<>();
    private final String[] NODE_NAMES = {"The Light", "Dusk and Dawn", "The Darkness"};
    private ImageButton prevView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        database = ManifestDatabase.getInstance(this);
        getNodeChildrenThread = new NodeChildrenThread(NODE_NAMES);
        getNodeChildrenThread.start();

        super.onCreate(savedInstanceState);
        Boolean isDarkThemeEnabled = getSharedPreferences("userData", Context.MODE_PRIVATE).getBoolean("isDarkThemeEnabled", false);
        if(isDarkThemeEnabled){
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }
        setContentView(R.layout.activity_lore_records);

        SwitchCompat theme = (SwitchCompat) findViewById(R.id.themeSwitch);
        if(isDarkThemeEnabled){
            theme.setChecked(true);
            theme.setText("Dark Theme");
        }

        recyclerView = (RecyclerView) findViewById(R.id.bookList);

        prevView = (ImageButton) findViewById(R.id.theLightButton);
        prevView.setBackgroundTintList(getResources().getColorStateList(R.color.darkTint));

        showBooks(findViewById(R.id.theLightButton));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
