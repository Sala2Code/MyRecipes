package sala.fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.AppCompatButton buttonCollection;
    private androidx.appcompat.widget.AppCompatButton buttonCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        this.buttonCollection = findViewById(R.id.buttonCollection);
        buttonCollection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent collectionAct = new Intent(getApplicationContext(), RecipesCollection.class);
                startActivity(collectionAct);
                /*finish();*/
            }
        });

        this.buttonCreate = findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent createAct = new Intent(getApplicationContext(), RecipesCreate.class);
                startActivity(createAct);
                /*finish();*/
            }
        });

    }
}