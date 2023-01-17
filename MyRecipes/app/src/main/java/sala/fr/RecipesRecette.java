package sala.fr;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class RecipesRecette extends AppCompatActivity {

    TextView recTitle, recTime, recPers, recIngredient, recRecette;
    ImageView recImg;
    private androidx.appcompat.widget.AppCompatButton editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_recette);
        getSupportActionBar().hide();

        int id = Integer.parseInt( getIntent().getStringExtra("id") );
        Cursor cursor = new DatabaseHelper(this).readSpecificID(id);
        cursor.moveToNext();

        recTitle = findViewById(R.id.recTitle);
        recTitle.setText(cursor.getString(1));

        recTime = findViewById(R.id.recTime);
        recTime.setText(cursor.getString(3) + " min");

        recPers = findViewById(R.id.recPers);
        recPers.setText(cursor.getString(4) + " Personne(s)");

        recIngredient = findViewById(R.id.recIngredient);
        recIngredient.setText(cursor.getString(5));

        recRecette = findViewById(R.id.recRecette);
        recRecette.setText(cursor.getString(6));

        recImg = findViewById(R.id.recImg);
        recImg.setImageBitmap( BitmapFactory.decodeByteArray(cursor.getBlob(7), 0, cursor.getBlob(7).length ) );

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editAct = new Intent(getApplicationContext(), RecipesEdit.class);
                editAct.putExtra("id", String.valueOf(id) );

                startActivity(editAct);
                finish();
            }
        });
    }
}