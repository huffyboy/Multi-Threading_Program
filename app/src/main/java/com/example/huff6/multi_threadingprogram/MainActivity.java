package com.example.huff6.multi_threadingprogram;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Does simple multi-threading to read and write items
 */
public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter;
    private List<String> numberList;
    private ListView listView;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView    = (ListView)    findViewById(R.id.listView);
        numberList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numberList);
        file = new File(this.getFilesDir(), "numbers.txt");
    }

    /**
     * Creates the file in the background on another thread
     *
     * @param v the view for XML reference
     */
    public void createFunction(View v) {
        new WritingTask().execute();
    }

    /**
     * Reads the file in the background on another thread
     *
     * @param v the view for XML reference
     */
    public void loadFunction(View v) {
        new ReadingTask().execute();
    }

    /**
     * Clears the items viewable on the screen
     *
     * @param v the view for XML reference
     */
    public void clearFunction(View v) {
        adapter.clear();
    }

    /**
     * Allows you to write a file in the background on another thread
     *      I made this in-class, because the other one was in class.
     */
    class WritingTask extends AsyncTask<Void, Integer, String> {

        /**
         * Writes 1 to 10 in a file
         *
         * @param params null
         * @return a pointless string
         */
        @Override
        protected String doInBackground(Void... params) {
            try {
                String text;
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (int i = 0; i < 10; i++) {
                    text = (i + 1) + "\n";
                    writer.write(text);
                    publishProgress((i + 1) * 10);
                    Thread.sleep(250);
                }
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "updated";
        }

        /**
         * Updates the progress bar on the screen
         *
         * @param progress the integer value of the progress
         */
        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        /**
         * Resets the progress bar on the screen
         *
         * @param text a pointless string
         */
        @Override
        protected void onPostExecute(String text) {
            progressBar.setProgress(0);
        }
    }

    /**
     * Allows you to read a file in the background on another thread
     *      I made this in-class, because it made access to the UI
     *      variables a lot easier.
     */
    class ReadingTask extends AsyncTask<Void, Integer, String> {

        /**
         * Reads 1 - 10 from the file into a list
         *
         * @param params null
         * @return a pointless string
         */
        @Override
        protected String doInBackground(Void... params) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    i++;
                    publishProgress(i * 10);
                    numberList.add(line);
                    Thread.sleep(250);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "updated";
        }

        /**
         * Displays the progress on the screen, and displays
         * the elements read on the screen as well
         *
         * @param progress the progress value
         */
        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
            listView.setAdapter(adapter);
        }

        /**
         * Resets the progress on the screen
         *
         * @param text a pointless string
         */
        @Override
        protected void onPostExecute(String text) {
            progressBar.setProgress(0);
        }
    }
}
