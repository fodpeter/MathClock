package peter.mathclock;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ListMultimap;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    private final DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm:ss");

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

        MappingParser parser = new MappingParser(this);
        ListMultimap<Integer, Integer> mapping = parser.loadMapping("mapping.properties");

        clock = new Clock(getImage(R.id.clockHours), getImage(R.id.clockMinutes), getImage(R.id.clockSeconds), mapping);
        clockText = (TextView) findViewById(R.id.clockText);
        updateTask = new UpdateTask();
        handler = new Handler();
    }

    private ImageView getImage(@IdRes int id) {
        return (ImageView) findViewById(id);
    }

    private class UpdateTask implements Runnable {

        private boolean paused;

        @Override
        public void run() {
            if (!paused) {
                updateClock();
                schedule();
            }
        }

        public void pause() {
            this.paused = true;
        }

        public void start() {
            this.paused = false;
            run();
        }
    }

    private void schedule() {
        handler.postDelayed(updateTask, 1000);
    }

    private void updateClock() {
        LocalTime now = LocalTime.now();
        clock.show(now);
        clockText.setText(format.print(now));
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateTask.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTask.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateTask.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateTask.start();
    }
}
