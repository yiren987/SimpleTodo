package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.IOExceptionWithCause;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd=findViewById(R.id.btnAdd);
        etItem=findViewById(R.id.etitem);
        rvItems=findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongCLickListener =new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the items list
                items.remove(position);
                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"item was removed",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemsAdapter= new ItemsAdapter(items, onLongCLickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem=etItem.getText().toString();
                //additem to the model(items)
                items.add(todoItem);
                //notify the adapter to display the item
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(),"item was added",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }
    //this function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items=new ArrayList<>(FileUtils.readLines(getDataFile(), String.valueOf(Charset.defaultCharset())));
        }
        catch(IOException e){

            items=new ArrayList<>();
        }
    }
    //this function saves items by wiritng them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        }
        catch(IOException e){
            Log.e("MainActivity","Error reading items", e);
        }

    }
}