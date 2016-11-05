package peter.mathclock;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class MappingParser {

    private Activity activity;

    public MappingParser(Activity activity) {
        this.activity = Preconditions.checkNotNull(activity);
    }

    public ListMultimap<Integer, Integer> loadMapping(String filename) {
        try {
            InputStream inputStream = activity.getBaseContext().getAssets().open(filename);
            Properties mappingProp = new Properties();
            mappingProp.load(inputStream);
            ListMultimap<Integer, Integer> mapping = parseMapping(mappingProp);
            if (mapping.isEmpty()) {
                throw new IllegalStateException("cannot find image resources");
            }
            return mapping;
        } catch (IOException e) {
            throw new IllegalStateException("read error " + filename, e);
        }
    }

    @NonNull
    private ListMultimap<Integer, Integer> parseMapping(Properties mappingProp) {
        ListMultimap<Integer, Integer> mapping = ArrayListMultimap.create(12, mappingProp.size() / 12 + 1);
        for (String file : mappingProp.stringPropertyNames()) {
            int id = activity.getResources().getIdentifier(file, "drawable", activity.getPackageName());
            if (id != 0) {
                mapping.put(Integer.valueOf(mappingProp.getProperty(file)), id);
            }
        }
        return mapping;
    }
}
