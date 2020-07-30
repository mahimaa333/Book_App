package android.example.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CheckBox checkBox1;
    CheckBox checkBox2;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_text);
        checkBox1 = (CheckBox) findViewById(R.id.author_cb);
        checkBox2 = (CheckBox) findViewById(R.id.title_cb);
    }

    public void onCLick(View view){
        String inputText = editText.getText().toString().trim();
        if(TextUtils.isEmpty(inputText)){
            Toast.makeText(this, "Please input text", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder query = new StringBuilder();
        query.append(inputText);

        if(checkBox1.isChecked()){
            query.append("+inauthor");
        }
        if(checkBox2.isChecked()){
            query.append("+intitle");
        }

        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra("search",query.toString());
        startActivity(intent);
    }
}