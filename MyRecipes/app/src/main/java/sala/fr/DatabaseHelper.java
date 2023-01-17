package sala.fr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Recipes.db";
    private static final int DATABASE_VERSION = 1;



    private static final String TABLE_NAME = "RECIPES";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "r_title";
    private static final String COLUMN_CATEG = "r_categ";
    private static final String COLUMN_TIME = "r_time";
    private static final String COLUMN_PERS = "r_pers";
    private static final String COLUMN_RECETTE = "r_recette";
    private static final String COLUMN_INGREDIENT = "r_ingredient";
    private static final String COLUMN_IMG = "r_img";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, "+
                        COLUMN_CATEG + " TEXT, "+
                        COLUMN_TIME + " INTEGER, "+
                        COLUMN_PERS + " INTEGER, "+
                        COLUMN_INGREDIENT + " TEXT, "+
                        COLUMN_RECETTE + " TEXT, "+
                        COLUMN_IMG + " BLOB);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    void addRecipes(String title, String categ, int time, int pers, String ingredient, String recette,
                    byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_CATEG, categ);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_PERS, pers);
        cv.put(COLUMN_INGREDIENT, ingredient);
        cv.put(COLUMN_RECETTE, recette);
        cv.put(COLUMN_IMG, img);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result==-1){
            Toast.makeText(context, "Erreur...", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Recette ajoutée !", Toast.LENGTH_LONG).show();
        }
    }

    void editRecipes(String id, String title, String categ, int time, int pers, String ingredient, String recette,
                    byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_CATEG, categ);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_PERS, pers);
        cv.put(COLUMN_INGREDIENT, ingredient);
        cv.put(COLUMN_RECETTE, recette);
        cv.put(COLUMN_IMG, img);

        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?",new String[]{id});
        if(result==-1){
            Toast.makeText(context, "Erreur...", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Recette modifiée !", Toast.LENGTH_LONG).show();
        }
    }

    void deleteRecipes(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        if(result==-1){
            Toast.makeText(context, "Erreur...", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "Recette supprimée !", Toast.LENGTH_LONG).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db!= null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor readSpecificID(int id){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +" = "+ id ;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db!= null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
