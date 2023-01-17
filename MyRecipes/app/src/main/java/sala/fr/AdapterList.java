package sala.fr;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;


public class AdapterList extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private ListView recipesListView;

    private ArrayList r_id, r_title, r_categ, r_time, r_pers, r_ingredient, r_recette;
    private ArrayList<Bitmap> r_img;

    private TextView titleList, timeList, persList;
    private ImageView imgList;

    private LinearLayout layoutButtonList;

    private EditText idInput;

    public AdapterList(Context context,
                       ArrayList r_id,
                       ArrayList r_title,
                       ArrayList r_categ,
                       ArrayList r_time,
                       ArrayList r_pers,
                       ArrayList r_ingredient,
                       ArrayList r_recette,
                       ArrayList<Bitmap> r_img){
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        this.r_id = r_id;
        this.r_title = r_title;
        this.r_categ = r_categ;
        this.r_time = r_time;
        this.r_pers = r_pers;
        this.r_ingredient = r_ingredient;
        this.r_recette = r_recette;
        this.r_img = r_img;
    }


    @Override
    public int getCount() {return r_id.size();}

    @Override
    public String getItem(int position) {return "0";}

    @Override
    public long getItemId(int i) {
        return Integer.parseInt( r_id.get(i).toString() );
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        this.idInput = ((Activity)context).findViewById(R.id.idInput);

        view = inflater.inflate(R.layout.adapterlist, null);

        this.titleList = view.findViewById(R.id.titleRecList);
        titleList.setText(r_title.get(i).toString());

        this.timeList = view.findViewById(R.id.timeRecList);
        timeList.setText(r_time.get(i).toString() + " min");

        this.persList = view.findViewById(R.id.persRecList);
        persList.setText(r_pers.get(i).toString() + " Personne(s)");


        this.imgList = view.findViewById(R.id.imgRecList);
        imgList.setImageBitmap(  r_img.get(i) );


        this.layoutButtonList = view.findViewById(R.id.layoutButtonList);
        layoutButtonList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                recipesListView = ((Activity) context).findViewById(R.id.listRecipes);
                idInput.setText(String.valueOf( getItemId(recipesListView.getPositionForView(view))));

                Intent recetteAct = new Intent(context.getApplicationContext(), RecipesRecette.class);

                String idStr = idInput.getText().toString();
                recetteAct.putExtra("id", idStr );

                context.startActivity(recetteAct);
                /*((Activity)context).finish();*/
            }
        });

        return view;
    }
}
