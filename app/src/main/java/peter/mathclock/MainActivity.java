package peter.mathclock;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private ListMultimap<Integer, Integer> mapping;
    private ImageView image;
    private TextView clockText;
    private UpdateTask updateTask;
    private Handler handler;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mapping = loadMapping();

        image = (ImageView) findViewById(R.id.imageView);

        clockText = (TextView) findViewById(R.id.clockText);
        clockText.setText("onCreate");

        handler = new Handler();
        schedule();
    }

    private void schedule() {
        updateTask = new UpdateTask();
        handler.postDelayed(updateTask, 1000);
    }

    private ListMultimap<Integer, Integer> loadMapping() {
        try {
            InputStream inputStream = getBaseContext().getAssets().open("mapping.properties");
            Properties mappingProp = new Properties();
            mappingProp.load(inputStream);
            ListMultimap<Integer, Integer> mapping = ArrayListMultimap.create(12, mappingProp.size() / 12 + 1);
            for (String file : mappingProp.stringPropertyNames()) {
                int id = getResources().getIdentifier(file, "drawable", getPackageName());
                if (id != 0) {
                    mapping.put(Integer.valueOf(mappingProp.getProperty(file)), id);
                } else {
                    Log.w("mapping", "cannot find " + file);
                }
            }
            if (mapping.isEmpty()) {
                throw new IllegalStateException("cannot find image resources");
            }
            return mapping;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class UpdateTask implements Runnable {

        @UiThread
        @Override
        public void run() {
            do {
                state = (state + 1) % 12;
            }
            while (!mapping.containsKey(state));
            image.setImageResource(mapping.get(state).get(0));
            clockText.setText("scheduled " + state);
            schedule();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
