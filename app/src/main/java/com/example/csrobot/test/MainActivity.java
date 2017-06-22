package com.example.csrobot.test;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean isNewIP = false;
    private Spinner masterIPSpinner;
    private EditText enterNewIPEdit;
    private Button backToListBtn;
    private Button enterNewIPBtn;
    private List<String> str = new ArrayList<String>();
    private List<String> revStr = new ArrayList<String>();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String line;
        int index;

        try {
            FileInputStream fis = openFileInput("previousData");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            line = in.readLine();
            while (line != null) {
                str.add(line);
                line = in.readLine();
            }
            in.close();
            fis.close();
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
        //Reverse str so that the most recent IP is on top
        for(index = str.size() - 1; index >= 0; index--){
            revStr.add(str.get(index));
        }

        masterIPSpinner = (Spinner) findViewById(R.id.MASTER_IP_SPINNER);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, revStr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        masterIPSpinner.setAdapter(adapter);
    }

    public void enterNewIP(View view) {
        isNewIP = true;
        masterIPSpinner = (Spinner) findViewById(R.id.MASTER_IP_SPINNER);
        masterIPSpinner.setVisibility(View.INVISIBLE);
        enterNewIPBtn = (Button) findViewById(R.id.NEW_IP_BUTTON);
        enterNewIPBtn.setVisibility(View.INVISIBLE);
        enterNewIPEdit = (EditText) findViewById(R.id.EDIT_NEW_IP);
        enterNewIPEdit.setVisibility(View.VISIBLE);
        backToListBtn = (Button) findViewById(R.id.BACK_TO_LIST_BTN);
        backToListBtn.setVisibility(View.VISIBLE);
    }

    public void backToList(View view) {
        isNewIP = false;
        enterNewIPEdit = (EditText) findViewById(R.id.EDIT_NEW_IP);
        enterNewIPEdit.setVisibility(View.INVISIBLE);
        backToListBtn = (Button) findViewById(R.id.BACK_TO_LIST_BTN);
        backToListBtn.setVisibility(View.INVISIBLE);
        masterIPSpinner = (Spinner) findViewById(R.id.MASTER_IP_SPINNER);
        masterIPSpinner.setVisibility(View.VISIBLE);
        enterNewIPBtn = (Button) findViewById(R.id.NEW_IP_BUTTON);
        enterNewIPBtn.setVisibility(View.VISIBLE);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText editText;
        String message;
        String masterIP_message;
        int index;

        Intent intent = new Intent(this, DisplayMessageActivity.class);

        editText = (EditText) findViewById(R.id.editText);
        message = editText.getText().toString();

        if(!isNewIP) {
            masterIPSpinner = (Spinner) findViewById(R.id.MASTER_IP_SPINNER);
            masterIP_message = masterIPSpinner.getSelectedItem().toString();
        }
        else{
            enterNewIPEdit = (EditText) findViewById(R.id.EDIT_NEW_IP);
            masterIP_message = enterNewIPEdit.getText().toString();

            //Removes the oldest ip to make space for the new one
            while (str.size() >= 5) {
                str.remove(0);
            }
            str.add(masterIP_message);

            try {
                FileOutputStream fos = openFileOutput("previousData", Context.MODE_PRIVATE);

                for(index = 0; index < str.size(); index ++) {
                    fos.write(str.get(index).getBytes());
                    fos.write(System.getProperty("line.separator").getBytes());
                }
                fos.close();
            }
            catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }

        intent.putExtra("EXTRA_MESSAGE", message);
        intent.putExtra("EXTRA_SPINNER", masterIP_message);

        startActivity(intent);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
