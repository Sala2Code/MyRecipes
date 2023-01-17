package sala.fr;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import android.os.Handler;



public class AdapterCategorie extends BaseAdapter {

    private Context context;
    private ArrayList<String> categList;
    private LayoutInflater inflater;
    private EditText categInput;
    private ListView categListView;

    private androidx.appcompat.widget.AppCompatButton buttonList;
    private androidx.appcompat.widget.AppCompatButton buttonTemp;

    private final int DELAY = 500; // 0.5s
    private boolean isDoubleClick = false;


    public AdapterCategorie(Context context, ArrayList<String> categList ){

        this.context = context;
        this.categList = categList;
        this.inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return categList.size();
    }

    @Override
    public String getItem(int position) {
        return categList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.adaptercategorie, null);


        String currentItem = getItem(i); // Normalement il faut récupérer ses attributs mais c'est que du texte

        this.buttonList = view.findViewById(R.id.CategButton);
        buttonList.setText(currentItem);
        buttonList.setBackgroundResource(R.drawable.redbutton);

        this.categInput = ((Activity)context).findViewById(R.id.categInput);
        this.categListView = ((Activity)context).findViewById(R.id.listCollection);


        buttonList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LinearLayout layoutCateg = (LinearLayout) view.getParent();
                if (isDoubleClick) {
                    if(layoutCateg.getChildCount()<2){
                        Button buttonDel = new Button(context);
                        buttonDel.setText("\uD83D\uDDD1");
                        buttonDel.setTextSize(30);
                        buttonDel.setBackground(context.getResources().getDrawable(R.drawable.redbutton));

                        buttonDel.setPadding(50, 50, 50, 50);

                        layoutCateg.addView(buttonDel);

                        buttonDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "Catégorie supprimée",Toast.LENGTH_LONG).show();
                                buttonTemp = (androidx.appcompat.widget.AppCompatButton) layoutCateg.getChildAt(0);

                                String categToDel = buttonTemp.getText().toString();

                                DatabaseHelper myDB = new DatabaseHelper(context.getApplicationContext());
                                Cursor cursor = myDB.readAllData();
                                if(cursor.getCount()==0){
                                    Toast.makeText(context, "Pas de données...", Toast.LENGTH_SHORT).show();
                                }else{
                                    while(cursor.moveToNext()){
                                        if(categToDel.equals(cursor.getString(2))){
                                            myDB.getWritableDatabase().delete(
                                                    "RECIPES",
                                                    "r_categ = '" + categToDel + "'",
                                                    null
                                            );
                                        }
                                    }
                                }

                                SharedPreferences sharedPreferences = context.getSharedPreferences("SharedCateg", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.remove("list categ").apply();
                                Gson gson = new Gson();
                                buttonTemp.getText().toString();
                                categList.remove(categToDel);
                                String json = gson.toJson(categList);
                                editor.putString("list categ", json).apply();

                                ((Activity) context).finish();
                                /*((Activity) getContext()).overridePendingTransition(0, 0);*/
                                context.startActivity(((Activity) context).getIntent());
                                /*((Activity) getContext()).overridePendingTransition(0, 0);*/

                            }
                        });
                    }
                    else{
                        layoutCateg.removeViewAt(1);
                    }
                    isDoubleClick = false;
                } else {
                    isDoubleClick = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isDoubleClick) {
                                categInput.setText( getItem(categListView.getPositionForView(view)) );
                                Intent listAct = new Intent(context.getApplicationContext(), RecipesList.class);
                                String categStr = categInput.getText().toString();
                                listAct.putExtra("categ", categStr );

                                context.startActivity(listAct);
                                /*((Activity)context).finish();*/

                                isDoubleClick = false;
                            }
                        }
                    }, DELAY);
                }
            }
        });

        return view;
    }



}
