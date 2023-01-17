package sala.fr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.ListView;

import java.util.ArrayList;

public class RecipesCollection extends AppCompatActivity {

    private ListView layoutCollection;
    private ArrayList<String> listCateg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_collection);

        getSupportActionBar().hide();

        listCateg = new LoadData().loadData(this, listCateg);

        ListView categListView = findViewById(R.id.listCollection);
        categListView.setAdapter(new AdapterCategorie(this, listCateg));
    }


}