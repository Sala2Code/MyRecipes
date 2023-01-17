package sala.fr;

import static sala.fr.RecipesCreate.getBytesFromBitmap;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class RecipesEdit extends AppCompatActivity {

    EditText recTitleEdit, recTimeEdit, recPersEdit, recIngredientEdit, recRecetteEdit;
    ImageView recImgEdit;
    ActivityResultLauncher<Intent> takeImg;
    Bitmap imgBitmap;
    Uri uri;
    private androidx.appcompat.widget.AppCompatButton saveButton, delButton;
    TextView textSeekBarEdit;
    SeekBar categBarEdit;
    private ArrayList<String> listCateg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_edit);
        getSupportActionBar().hide();

        int id = Integer.parseInt( getIntent().getStringExtra("id") );
        Cursor cursor = new DatabaseHelper(this).readSpecificID(id);
        cursor.moveToNext();

        recTitleEdit = findViewById(R.id.recTitleEdit);
        recTitleEdit.setText(cursor.getString(1));

        listCateg = new LoadData().loadData(this, listCateg);

        textSeekBarEdit = findViewById(R.id.textSeekBarEdit);
        textSeekBarEdit.setText(listCateg.get(0));

        categBarEdit = findViewById(R.id.categBarEdit);
        categBarEdit.setMax(listCateg.size()-1);
        categBarEdit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textSeekBarEdit.setText(listCateg.get(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        recTimeEdit = findViewById(R.id.recTimeEdit);
        recTimeEdit.setText(cursor.getString(3) );

        recPersEdit = findViewById(R.id.recPersEdit);
        recPersEdit.setText(cursor.getString(4) );

        recIngredientEdit = findViewById(R.id.recIngredientEdit);
        recIngredientEdit.setText(cursor.getString(5));

        recRecetteEdit = findViewById(R.id.recRecetteEdit);
        recRecetteEdit.setText(cursor.getString(6));

        recImgEdit = findViewById(R.id.recImgEdit);
        recImgEdit.setImageBitmap( BitmapFactory.decodeByteArray(cursor.getBlob(7), 0, cursor.getBlob(7).length ) );

        recImgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                takeImg.launch(intent);
            }
        });

        takeImg = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK && result.getData() != null){
                    uri = result.getData().getData();
                    recImgEdit.setImageURI(uri);
                    try {
                        imgBitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
        saveButton = findViewById(R.id.editButtonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Update data */
                int timeEditVal, persEditVal;
                try{timeEditVal = Integer.valueOf(recTimeEdit.getText().toString());}catch (Exception e){timeEditVal = -1;};
                try{persEditVal = Integer.valueOf(recPersEdit.getText().toString());}catch (Exception e){persEditVal = -1;};

                new DatabaseHelper(view.getContext()).editRecipes(
                        String.valueOf(id),
                        recTitleEdit.getText().toString(),
                        textSeekBarEdit.getText().toString(),
                        timeEditVal,
                        persEditVal,
                        recIngredientEdit.getText().toString(),
                        recRecetteEdit.getText().toString(),
                        getBytesFromBitmap(imgBitmap)
                );

                /* Leave activity */
                Intent recAct = new Intent(getApplicationContext(), RecipesRecette.class);
                recAct.putExtra("id", String.valueOf(id) );
                startActivity(recAct);
                finish();

            }
        });


        delButton = findViewById(R.id.editButtonDel);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatabaseHelper(view.getContext()).deleteRecipes( String.valueOf(id));

                Intent mainAct = new Intent(getApplicationContext(), MainActivity.class);
                mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainAct);
            }
        });
    }
}