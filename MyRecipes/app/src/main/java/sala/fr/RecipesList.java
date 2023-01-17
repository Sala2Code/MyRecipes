package sala.fr;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipesList extends AppCompatActivity {

    private TextView titleTemp ;

    DatabaseHelper myDB;
    ArrayList<String> r_id, r_title, r_categ, r_time, r_pers, r_ingredient, r_recette, listCateg ;
    ArrayList<Bitmap> r_img;
    private String categStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        getSupportActionBar().hide();

        this.titleTemp = findViewById(R.id.titleTemp);
        categStr = getIntent().getStringExtra("categ").toUpperCase();
        titleTemp.setText(categStr);

        myDB = new DatabaseHelper(getApplicationContext());
        r_id = new ArrayList<>();
        r_title = new ArrayList<>();
        r_categ = new ArrayList<>();
        r_time = new ArrayList<>();
        r_pers = new ArrayList<>();
        r_ingredient = new ArrayList<>();
        r_recette = new ArrayList<>();
        r_img = new ArrayList<>();

        storeDataInArrays();

        ListView RecipesListView = findViewById(R.id.listRecipes);
        RecipesListView.setAdapter(new AdapterList(this, r_id, r_title, r_categ, r_time, r_pers, r_ingredient, r_recette, r_img));
    }

    void storeDataInArrays(){
        listCateg = new LoadData().loadData(this, listCateg);

        Cursor cursor = myDB.readAllData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Pas de données...", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                if( categStr.equals(cursor.getString(2)) ){
                    r_id.add(cursor.getString(0));
                    r_title.add(cursor.getString(1));
                    r_categ.add(cursor.getString(2));
                    r_time.add(cursor.getString(3));
                    r_pers.add(cursor.getString(4));
                    r_ingredient.add(cursor.getString(5));
                    r_recette.add(cursor.getString(6));
                    r_img.add( BitmapFactory.decodeByteArray(cursor.getBlob(7), 0, cursor.getBlob(7).length ) );
                }
            }
        }
    }


}