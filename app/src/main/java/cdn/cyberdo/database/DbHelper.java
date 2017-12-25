package cdn.cyberdo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by cyberdude on 25/12/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cdnTodo";
    private static final int DB_Ver = 1;
    private static final String DB_TABLE = "task";
    private static final String DB_COLUMN = "task_name";
    private static final String DB_COLUMN_TIME = "task_created_on";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_Ver);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s DATETIME NOT NULL)", DB_TABLE, DB_COLUMN, DB_COLUMN_TIME);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = String.format("DELETE TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask(String task, String task_created_on){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);
        values.put(DB_COLUMN_TIME, task_created_on);
        db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN + " = ?", new String[]{task});
        db.close();
    }

    public ArrayList<String> getTaskList(){
            ArrayList<String> taskList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN}, null, null, null, null, null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }
}
