package jawadbraick.destinygrimoire;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void openGrimoire(View view){
        startActivity(new Intent(MainActivity.this, GrimoireActivity.class));
    }
    public void viewRecords(View view){
        startActivity(new Intent(MainActivity.this, LoreRecordsActivity.class));
    }

}
