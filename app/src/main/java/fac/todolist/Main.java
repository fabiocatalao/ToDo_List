package fac.todolist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import android.widget.ListView;
import android.widget.EditText;

import org.apache.commons.io.*;

public class Main extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Load Items
        lvItems = (ListView) findViewById(R.id.list);
        items = new ArrayList<String>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);


        setupAddTaskButton();
        setupCompletedButton();

        //setupAboutButton();
    }


    public void setupAboutButton() {
        FloatingActionButton add_task = (FloatingActionButton) findViewById(R.id.about);
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
                String itemText = etNewItem.getText().toString();
                Snackbar.make(view, getResources().getString(R.string.about), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }

    public void setupCompletedButton() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        Snackbar.make(item, getResources().getString(R.string.task_completed), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        writeItems();
                        return true;
                    }
                });
    }

    public void setupAddTaskButton() {
        FloatingActionButton add_task = (FloatingActionButton) findViewById(R.id.add_task);
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
                String itemText = etNewItem.getText().toString();

                if (!itemText.isEmpty()) {
                    itemsAdapter.add(itemText);
                    Snackbar.make(view, getResources().getString(R.string.task_added), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    etNewItem.setText("");
                    writeItems();
                } else {
                    Snackbar.make(view, getResources().getString(R.string.task_null), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
     /*   if (id == R.id.action_settings) {
            return true;
        }*/

        switch (item.getItemId()) {
            case R.id.add_task:
                Log.d("Main", "Add a new task");
                return true;
            case R.id.about:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
