package jawadbraick.destinygrimoire;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

public class ItemsLoreActivity extends AppCompatActivity {
    private ManifestDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = ManifestDatabase.getInstance(this);

//        getNodeChildrenThread = new LoreHomeActivity.NodeChildrenThread(NODE_NAMES);
//        getNodeChildrenThread.start();

        super.onCreate(savedInstanceState);
        Boolean isDarkThemeEnabled = getSharedPreferences("userData", Context.MODE_PRIVATE).getBoolean("isDarkThemeEnabled", false);
        if(isDarkThemeEnabled){
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }
        setContentView(R.layout.activity_items_lore);

        SwitchCompat theme = (SwitchCompat) findViewById(R.id.themeSwitch);
        if(isDarkThemeEnabled){
            theme.setChecked(true);
            theme.setText("Dark Theme");
        }
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
}