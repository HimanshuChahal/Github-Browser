package com.githubbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddRepoActivity extends AppCompatActivity {

    EditText ownerNameEditText;
    EditText repoNameEditText;

    public void backOnClick(View view)
    {
        finish();
    }

    public void addRepoOnClick(View view)
    {
        if(!TextUtils.isEmpty(ownerNameEditText.getText()) && !TextUtils.isEmpty(repoNameEditText.getText())) {

            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Repositories", MODE_PRIVATE, null);

            ContentValues contentValues = new ContentValues();
            contentValues.put("owner", ownerNameEditText.getText().toString());
            contentValues.put("name", repoNameEditText.getText().toString());

            sqLiteDatabase.insert("Repos", null, contentValues);

            sqLiteDatabase.close();

            startActivity(new Intent(AddRepoActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

        } else
        {
            Toast.makeText(AddRepoActivity.this, "Please enter the values", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repo);

        ownerNameEditText=findViewById(R.id.ownerNameEditText);
        repoNameEditText=findViewById(R.id.repoNameEditText);

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.anim_start, R.anim.anim_end);

    }
}