package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        buildDialog();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                dialog.show();
            }
        });

        loadTasks();
    }

    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter your Task")
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCard(name.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
    }

    private void addCard(String name) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);
        nameView.setText(name);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
                removeTask(name);
            }
        });
        layout.addView(view);

        saveTask(name);
    }

    private void saveTask(String task) {
        SharedPreferences sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> tasks = sharedPreferences.getStringSet("task_set", new HashSet<String>());
        tasks.add(task);
        editor.putStringSet("task_set", tasks);
        editor.apply();
    }

    private void removeTask(String task) {
        SharedPreferences sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> tasks = sharedPreferences.getStringSet("task_set", new HashSet<String>());
        tasks.remove(task);
        editor.putStringSet("task_set", tasks);
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE);
        Set<String> tasks = sharedPreferences.getStringSet("task_set", new HashSet<String>());
        for (String task : tasks) {
            addCard(task);
        }
    }
}