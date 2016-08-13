package peter.mathclock;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private Clock clock;
    private TextView clockText;
    private UpdateTask updateTask;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListMultimap<Integer, Integer> mapping = loadMapping();

        clock = new Clock(getImage(R.id.clockHours), getImage(R.id.clockMinutes), getImage(R.id.clockSeconds), mapping);
        clockText = (TextView) findViewById(R.id.clockText);
        updateTask = new UpdateTask();
        handler = new Handler();

        schedule();
    }

    private ImageView getImage(@IdRes int id) {
        return (ImageView) findViewById(id);
    }

    private void schedule() {
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

        private DateTimeFormatter format = DateTimeFormat.mediumTime();

        @UiThread
        @Override
        public void run() {
            LocalTime now = LocalTime.now();
            clock.show(now);
            clockText.setText(format.print(now));
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
