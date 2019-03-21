package com.example.assignment03;
/**
 * Name: LeAerialle White
 * Date: 3/21/2019
 * Assignment: TO-DO List
 */

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    ListView todoList;
    ArrayList<String> userList;
    EditText itemToAdd;
    ArrayAdapter<String> arrayAdapter;
    private String listFile = "ToDoList.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userList = new ArrayList<>();
        itemToAdd = findViewById(R.id.itemEdit);
        todoList = findViewById(R.id.userList);
        //Receive from savedInstance
        if(savedInstanceState != null) {
            userList = savedInstanceState.getStringArrayList("items");
        } else {
            readFile();
        }
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.activity_listview,userList);
        todoList.setAdapter(arrayAdapter);
        createListener();

    }

    /**
     * Creates a listener for the listView
     */
    public void createListener() {
        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                userList.remove(position);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    /**
     * reads the to do list from a file in internal storage of phone
     */
    private void readFile() {
        try {
            InputStream in = getApplicationContext().openFileInput(listFile);
            if(in != null) {
                InputStreamReader inputRead = new InputStreamReader(in);
                BufferedReader buff = new BufferedReader(inputRead);
                String item = "";
                while((item = buff.readLine()) != null) {
                    userList.add(item);
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            Log.e("File Error", " " + e.toString());
        } catch (IOException e) {
            Log.e("File Error2", " " + e.toString());
        }
    }

    /**
     * Writes to the to-do list to a file in internal storage
     */
    private void writeToFile() {
        try {
            OutputStreamWriter out = new OutputStreamWriter(getApplicationContext().openFileOutput(listFile, Context.MODE_PRIVATE));
            for(int i = 0; i < userList.size(); i++) {
                String item = userList.get(i) + "\n";
                out.write(item);
            }
            out.close();
        } catch (IOException e) {
            Log.e("Exception ", "" + e.toString());
        }
    }



    /**
     * Adds an item to the list
     * @param v
     */
    public void addItem(View v) {
        String newItem = itemToAdd.getText().toString();
        if(newItem.length()>0) {
            userList.add(newItem);
            arrayAdapter.notifyDataSetChanged();
            itemToAdd.setText("");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("items", userList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userList = savedInstanceState.getStringArrayList("items");
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeToFile();

    }
}

