package sala.fr;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;


public class RecipesCreate extends AppCompatActivity implements Dialog.DialogListener {

    EditText titleInput, timeInput, persInput, ingredientInput, recetteInput;
    SeekBar categBarInput;
    TextView textSeekBar;
    ImageView chooseImg;
    private androidx.appcompat.widget.AppCompatButton sendButton, dialogButton;
    ArrayList<String> listCateg;
    Bitmap imgBitmap;
    Uri uri;

    ActivityResultLauncher<Intent> takeImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_create);
        getSupportActionBar().hide();

        dialogButton =  findViewById(R.id.dialogButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        listCateg = new LoadData().loadData(this, listCateg);

        titleInput = findViewById(R.id.nameInput);
        categBarInput = findViewById(R.id.categBarInput);
        timeInput = findViewById(R.id.timeInput);
        persInput  = findViewById(R.id.persInput);
        ingredientInput  = findViewById(R.id.ingredientInput);
        recetteInput = findViewById(R.id.recetteInput);


        textSeekBar = findViewById(R.id.textSeekBar);
        textSeekBar.setText(listCateg.get(0));

        categBarInput.setMax(listCateg.size()-1);
        categBarInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textSeekBar.setText(listCateg.get(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //* CHOISIR IMAGE *//

        chooseImg = findViewById(R.id.chooseImg);
        imgBitmap = ((BitmapDrawable)chooseImg.getDrawable()).getBitmap();
        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                takeImg.launch(intent);


            }
        });

        takeImg = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()==RESULT_OK && result.getData() != null){
                        uri = result.getData().getData();
                        chooseImg.setImageURI(uri);
                        try {
                            imgBitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper DB = new DatabaseHelper(RecipesCreate.this);
                int timeInputVal, persInputVal;
                try{timeInputVal = Integer.valueOf(timeInput.getText().toString());}catch (Exception e){timeInputVal = -1;};
                try{persInputVal = Integer.valueOf(persInput.getText().toString());}catch (Exception e){persInputVal = -1;};

                DB.addRecipes(
                        titleInput.getText().toString().trim(),
                        listCateg.get(categBarInput.getProgress()),
                        timeInputVal,
                        persInputVal,
                        ingredientInput.getText().toString().trim(),
                        recetteInput.getText().toString().trim(),
                        getBytesFromBitmap(imgBitmap)
                        );

                Intent intent = new Intent(getApplicationContext(), RecipesCollection.class);
                startActivity(intent);
                /*finish();*/
            }
        });
    }

    public void openDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "Nouvelle catégorie");
    }

    @Override
    public void addToCateg(String newCateg) {
        if(!listCateg.contains(newCateg.toUpperCase())){
            listCateg.add(newCateg.toUpperCase());
        }
        else{
            Toast.makeText(this, "Cette catégorie existe déjà !", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("SharedCateg", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listCateg);
        editor.putString("list categ",json);
        editor.apply();

    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }
        return null;
    }

}