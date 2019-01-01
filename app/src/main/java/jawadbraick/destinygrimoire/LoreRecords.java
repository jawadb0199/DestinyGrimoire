package jawadbraick.destinygrimoire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LoreRecords extends AppCompatActivity{
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lore_records);

//        LayoutInflater inflater = this.getLayoutInflater();
        recyclerView = (RecyclerView) findViewById(R.id.bookList);

        ArrayList<RecordInfo> recordData = (ArrayList) getRecordData();
        recordAdapter = new RecordAdapter(this, recordData);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private List<RecordInfo> getRecordData(){
        int[] iconIds = {R.drawable.the_lawless_frontier,
                         R.drawable.the_man_they_call_cayde,
                         R.drawable.most_loyal,
                         R.drawable.ghost_stories,
                         R.drawable.evas_journey};
        String[] names = {"The Lawless Frontier",
                          "The Man They Call Cayde",
                          "Most Loyal",
                          "Ghost Stories",
                          "Eva's Journey"};

        ArrayList<RecordInfo> recordInfoList = new ArrayList<>();
        for(int i = 0; i < iconIds.length; i++){
            RecordInfo current = new RecordInfo();
            current.iconId = iconIds[i];
            current.name = names[i];
            recordInfoList.add(current);
        }

        return recordInfoList;

    }
}
