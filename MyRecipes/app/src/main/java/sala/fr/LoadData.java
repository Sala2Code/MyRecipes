package sala.fr;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoadData {
    public ArrayList loadData(Context context, ArrayList listCateg){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedCateg", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list categ", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        listCateg = gson.fromJson(json, type);

        if(listCateg==null || listCateg.isEmpty()){
            listCateg=new ArrayList<>();
            listCateg.add(0,"ENTREES");
            listCateg.add(1,"PLATS");
            listCateg.add(2,"DESSERTS");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gsonN = new Gson();
            String jsonN = gsonN.toJson(listCateg);
            editor.putString("list categ",jsonN);
            editor.apply();
        }

        return listCateg;
    }
}
