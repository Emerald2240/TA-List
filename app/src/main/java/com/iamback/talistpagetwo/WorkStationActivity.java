package com.iamback.talistpagetwo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

public class WorkStationActivity extends AppCompatActivity {

    public static ArrayList<Object> arrayList = new ArrayList();
    public static ArrayList<Object> delArrayList = new ArrayList<>();
    public static ArrayList<Object> rotatorArrayList = new ArrayList<Object>();

    ArrayList<Object> titleArrayObj = new ArrayList<>();
    ArrayList<TitleItems> titleArray = new ArrayList<>();
    ArrayList<NewListItems> newListArray = new ArrayList<>();
    WorkMenuListAdapter listViewAdapter;


    static int play = android.R.drawable.ic_media_play;
   static int pause = android.R.drawable.ic_media_pause;

    ListView listView;
    Toolbar toolbar;
    static  TinyDB tinyDB2;

    static int originalArraySize;

   static String Mainkey = " ";
    int pos = 0;
    int position = 0;

    TitleItems titleItems;

    static Boolean loadState = false;
    static TitleItems statesaveTitleitem;

    boolean searchable = false;
    int searchedListPosition = 0;

    public  static  final String SORT = "SORT";
    public static final String SORTVALUES = "SORT_VALUES";
    public static final String A_OR_D = "A_OR_D";
    public static final String THEMES_ON = "THEMES_ON";
    public static final String THEME = "THEME";


    //boolean sortable = true;
    //int sortBy = 2;
    //int order = 1;
    boolean themeOn = false;
    int theme = 1;

    Double total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updatePreferences();
        activateTheme(theme, themeOn);
        setContentView(R.layout.activity_work_station);
        InitializeViews();

        //Method for loading Screen/ArrayList/ListView with already saved Items
        loadSharedPref(tinyDB2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(Mainkey);
        }

        listViewAdapter = new WorkMenuListAdapter(this, R.layout.list_item, arrayList);
        listView.setAdapter(listViewAdapter);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewListItems items;
                //MainActivity.this.items = listViewAdapter.getItem(position);
                //MainActivity.this.showDialog(1);

                delArrayList.add(listViewAdapter.getItem(position));
                items = listViewAdapter.getItem(position);
                total -= items.getPrice();
                arrayList.remove(listViewAdapter.getItem(position));


                listViewAdapter.notifyDataSetChanged();
            }
        });

    }
    private void loadSharedPref(TinyDB tdb2) {
    //searchable = tdb2.getBoolean("searchable");
        titleArrayObj = tdb2.getListObject("2240", TitleItems.class);
        Mainkey = tdb2.getString("MainListName");
        ReturnObjecttoTitleItems(titleArrayObj);

        //int position = 0;

        ArrayList<String> titleKey = new ArrayList<String>();
        titleKey = tdb2.getListString("TTK");

        /*if (searchable == true){
            for (int i = 0; i<titleKey.size(); i++){
                String key = titleKey.get(i);
                if (key.contains(Mainkey) && key.length() == Mainkey.length()){
                    position = i;
                }
            }
        }else {*/
            position = tdb2.getInt("MainListPosition");
        //}

        ReturnObjecttoTitleItems(titleArrayObj);

        statesaveTitleitem = titleArray.get(position);
total = statesaveTitleitem.getTotalPrice();
        //titleArray.remove(position);
        if (statesaveTitleitem.getImageSrc() != pause ) {
            arrayList.clear();
            //int keyindex = tdb2.getInt("1111");
            //arrayList = tdb2.getListObject(Mainkey, TitleItems.class);

            arrayList = tdb2.getListObject(Mainkey, NewListItems.class);
            originalArraySize = arrayList.size();
        } else {
               arrayList.clear();
               arrayList = tdb2.getListObject(Mainkey + "ongo", NewListItems.class);
               originalArraySize = arrayList.size();

        }
        //statesaveTitleitem.setImageSrc(pause);
        //titleArray.add(position, statesaveTitleitem);

        //titleArrayObj.clear();
        //titleArrayObj.addAll(titleArray);
        //tinyDB.putListObject("2240", titleArrayObj);
    }
    private void InitializeViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbarwork);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = this.findViewById(R.id.workmenuListView);
        tinyDB2 = new TinyDB(this);
    }
    @Override
    public void onBackPressed() {
        int remarray = arrayList.size();
        if (remarray < originalArraySize && remarray != 0){
            TinyDB tinyDB = new TinyDB(this);
            tinyDB.putListObject(Mainkey+"ongo", arrayList);
            Toast.makeText(WorkStationActivity.this,
                    "Saved", Toast.LENGTH_SHORT).show();

            //position = tinyDB.getInt("MainListPosition");

            titleArrayObj = tinyDB.getListObject("2240", TitleItems.class);
            ReturnObjecttoTitleItems(titleArrayObj);
             getTotalPrice();
            TitleItems statesaveTitleitem = titleArray.get(position);
            titleArray.remove(position);
            statesaveTitleitem.setImageSrc(pause);
            statesaveTitleitem.setTotalPrice(total);
            titleArray.add(position, statesaveTitleitem);

            titleArrayObj.clear();
            titleArrayObj.addAll(titleArray);
            tinyDB.putListObject("2240", titleArrayObj);
        }
        if (remarray == 0){
            TinyDB tinyDB = new TinyDB(this);

            int position;
            position = tinyDB.getInt("MainListPosition");

            getTotalPrice();
            total = originalListPrice;
            titleArrayObj = tinyDB.getListObject("2240", TitleItems.class);
            ReturnObjecttoTitleItems(titleArrayObj);
            //Collections.reverse(titleArray);
            TitleItems statesaveTitleitem = titleArray.get(position);
            titleArray.remove(position);
            statesaveTitleitem.setImageSrc(play);
            statesaveTitleitem.setTotalPrice(total);
            titleArray.add(position, statesaveTitleitem);
            //Collections.reverse(titleArrayObj);
            titleArrayObj.clear();
            titleArrayObj.addAll(titleArray);
            tinyDB.putListObject("2240", titleArrayObj);

            Toast.makeText(WorkStationActivity.this, Mainkey+" Session Complete!", Toast.LENGTH_SHORT).show();
        }

        arrayList.clear();
        delArrayList.clear();
        rotatorArrayList.clear();

        titleArray.clear();
        titleArrayObj.clear();
        super.onBackPressed();
        overridePendingTransition(R.anim.out_zoom_out, R.anim.out_zoom_in);

    }
    private void ReturnObjecttoTitleItems(ArrayList arrayList){
        titleArray.clear();
        titleArray.addAll(arrayList);
    }
    private void returnObjecttoNewListItems(ArrayList arrayList){
        newListArray.clear();
        newListArray.addAll(arrayList);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_refresh:
                arrayList.clear();
                delArrayList.clear();
                rotatorArrayList.clear();

                titleArray.clear();
                titleArrayObj.clear();
                arrayList.clear();
                //int keyindex = tdb2.getInt("1111");
                //arrayList = tdb2.getListObject(Mainkey, TitleItems.class);
                TinyDB tinyDB = new TinyDB(this);

                int position;
                position = tinyDB.getInt("MainListPosition");

                getTotalPrice();
                total = originalListPrice;

                titleArrayObj = tinyDB.getListObject("2240", TitleItems.class);
                ReturnObjecttoTitleItems(titleArrayObj);

                TitleItems statesaveTitleitem = titleArray.get(position);
                titleArray.remove(position);
                statesaveTitleitem.setImageSrc(play);
                statesaveTitleitem.setTotalPrice(total);
                titleArray.add(position, statesaveTitleitem);

                tinyDB.remove(Mainkey+"ongo");
                titleArrayObj.clear();
                titleArrayObj.addAll(titleArray);
                tinyDB.putListObject("2240", titleArrayObj);

                Toast.makeText(WorkStationActivity.this,
                        "Session cleared", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.app_bar_revert:
                if (delArrayList.size() > 0)
                {

                    int delarraySize = delArrayList.size()-1;
                    rotatorArrayList.addAll(arrayList);
                    //for (int k = 0; k <= arrayList.size(); k++){
                    // arrayList.add(k, delArrayList.get(0));}
                    arrayList.clear();
                    arrayList.add(delArrayList.get(delarraySize));
                    arrayList.addAll(rotatorArrayList);

                    //int maxsize = arrayList.size();
                    //arrayList.add(maxsize, delArrayList.get(0));

                    delArrayList.remove(delarraySize);
                    rotatorArrayList.clear();
                    listViewAdapter.notifyDataSetChanged();
                }else
                    Toast.makeText(WorkStationActivity.this,
                            "History Empty", Toast.LENGTH_SHORT).show();
            default:
                //if we got here, the users action was not recognized
                //invoke theSuperClass
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.work_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    Double originalListPrice = 0.0;
    public void getTotalPrice(){
        originalListPrice = 0.0;
        arrayList  = tinyDB2.getListObject(Mainkey, NewListItems.class);
        returnObjecttoNewListItems(arrayList);
        for (int i = 0; i < newListArray.size(); i++){
            NewListItems newListItems;
            newListItems = newListArray.get(i);
            originalListPrice += newListItems.getPrice();
        }
        arrayList.clear();
    }
    public void updatePreferences(){
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //sortable = defaultSharedPreferences.getBoolean(SORT, false);
        themeOn = defaultSharedPreferences.getBoolean(THEMES_ON, true);
        //sortBy = Integer.parseInt(defaultSharedPreferences.getString(SORTVALUES, "2"));
        //order = Integer.parseInt(defaultSharedPreferences.getString(A_OR_D, "1"));
        theme = Integer.parseInt(defaultSharedPreferences.getString(THEME, "2"));
    }
    public void activateTheme(int theme, boolean themeOn){
        if (themeOn){
            if (theme == 1){setTheme(R.style.AppTheme2);}
            if (theme == 2){setTheme(R.style.AppTheme);}
            if (theme == 3){setTheme(R.style.AppThemeRed);}
            if (theme == 4){setTheme(R.style.AppThemeOrange);}
            if (theme == 5){setTheme(R.style.AppThemeYellow);}
            if (theme == 6){setTheme(R.style.AppThemeGreen);}
            if (theme == 7){setTheme(R.style.AppThemeBlue);}
            if (theme == 8){setTheme(R.style.AppThemeIndigo);}
            if (theme == 9){setTheme(R.style.AppThemeCyan);}
            if (theme == 10){setTheme(R.style.AppThemeBrown);}
            if (theme == 11){setTheme(R.style.AppThemeGrey);}

        }
    }

}