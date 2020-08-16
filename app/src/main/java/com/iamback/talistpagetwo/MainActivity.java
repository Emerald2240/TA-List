package com.iamback.talistpagetwo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;

public  class MainActivity extends AppCompatActivity {
    //Toolbar toolbar;
    public  static ListView listView;
    //public static ArrayList<TitleItems> titleArrayList = new ArrayList<>();
    public static ArrayList<Object> arrayList = new ArrayList<>();
    public static ArrayList<Object> unSearchedArrayList = new ArrayList<>();
    public static ArrayList<TitleItems> arrayListTitle = new ArrayList<>();
    public static ArrayList<String> arrayListString = new ArrayList<>();

    //public static ArrayList<Object> arrayListItemsObj = new ArrayList<>();
    //public static ArrayList<NewListItems> arrayListItems = new ArrayList<>();
    //public static ArrayList<String> titleKeys = new ArrayList<>();

    public static String Mainkey = "2240";
    String itemMainkey = "";
    FloatingActionButton floatingActionButton;
    TitleItems titleItems;
    public static String itemname = "";
    public static int pos = 0;
    Object items1;
    TitleItems items;

    int play = android.R.drawable.ic_media_play;
    int pause = android.R.drawable.ic_media_pause;

    TextView renameBtn;
    TextView deleteBtn;
    TextView shareBtn;
    ConstraintLayout constraintLayout;

    EditText editTextBox;
    Button saveBtn;
    Button cancelBtn;

    boolean mainOptions = false;
    //boolean mainOptionsRename = false;
    boolean mainOptionsAskToDelete = false;

    int dialogres;
    String captureMainKey = "List";

    boolean editItem = false;

    ListViewAdapter listViewAdapter;

   public static ListView newListView;
   boolean searched = false;

   public  static  final String SORT = "SORT";
   public static final String SORTVALUES = "SORT_VALUES";
   public static final String A_OR_D = "A_OR_D";
   public static final String THEMES_ON = "THEMES_ON";
   public static final String THEME = "THEME";

   //boolean sortable = true;
   //int sortBy = 2;
  // int order = 1;
   boolean themeOn = false;
   int theme = 1;

   SharedPreferences preferences;

    //android:startColor="@color/colorPrimary"
    //android:endColor="@color/colorAccent"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TinyDB tinyDB =  new TinyDB(this);

        updatePreferences();
        activateTheme(theme, themeOn);

        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

            Toolbar toolbar;
            toolbar = (Toolbar) findViewById(R.id.toolbarMain);
            setSupportActionBar(toolbar);

       // ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.savedItemListView);
        constraintLayout = findViewById(R.id.conlay);
        floatingActionButton = findViewById(R.id.floatingActionButton3);
        final TinyDB tinyDB2 = new TinyDB(this);

        loadSharedPref(tinyDB2);
        //refreshList();
        ReturnObjecttoTitleItems(arrayList);

       //final ListViewAdapter
               listViewAdapter = new ListViewAdapter(MainActivity.this, R.layout.main_menu_listview_resource, arrayListTitle);
               listView.setAdapter(listViewAdapter);
        //InitializeItems();
        //tinyDB2.putListObject(Mainkey, arrayList);
        //Toast.makeText(MainActivity.this, " Saved", Toast.LENGTH_SHORT).show();
        //TitleItems items = new TitleItems("Indomie", "550.0", "2020:06:01-13:09:56");
        //arrayList.add(items);

        final Intent intent = new Intent(MainActivity.this, WorkStationActivity.class);
        //intent.putExtra("ListKey", itemname);
        //intent.putExtra("TitleIndex", itemname);

constraintLayout.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        listViewAdapter.notifyDataSetChanged();
    }
});
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.items = listViewAdapter.getItem(position);
                itemname = items.toString();
                int originalPos = position;

                if (searched) {
                    //originalPos = unSearchedArrayList.indexOf(arrayList.get(position));
                    for (int i = 0; i<arrayListString.size(); i++){
                        String security = arrayListString.get(i);
                        if (security.contains(itemname) && security.length() == itemname.length()){
                            originalPos = i;
                        }
                    }
                    getItemKey(itemname, originalPos, tinyDB2);
                }else {
                    getItemKey(itemname, position, tinyDB2);
                }

                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
            }
        });

        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
             MainActivity.this.items = listViewAdapter.getItem(position);
                itemname = items.getTitleName();
                pos = position;
                getItemKey(itemname, position, tinyDB2);
                MainActivity.this.showDialog(1);
                return true;
            }
        });

        floatingActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, NewListActivity.class));
                MainActivity.this.overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
            }
        });
    }
    public Dialog onCreateDialog(int i) {
        //View inflate = null;
        //InitializeDialogViewResources();
        View inflate = LayoutInflater.from(this).inflate(R.layout.main_options_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Full Details");
        builder.setView(inflate);
        return builder.create();
    }
    public void onPrepareDialog(int i, Dialog dialog) {
        /*DecimalFormat commaAdder;
        commaAdder = new DecimalFormat("#,###.####");
        commaAdder.setRoundingMode(RoundingMode.CEILING);
        TextView textView;
        String Name = this.items.getName1();
        Double price = this.items.getPrice1();
        String quant = this.items.getQuantity1();*/

        AlertDialog alertDialog = (AlertDialog) dialog;

        //StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append("Info: ");
        //stringBuilder.append(Name);
        //alertDialog.setTitle(stringBuilder.toString());

        //TextView nametxtvw = (TextView) alertDialog.findViewById(R.id.dlgNameTxt);
        //TextView pricetxtvw = (TextView) alertDialog.findViewById(R.id.dlgPriceTxt);
        //TextView quanttxtvw = (TextView) alertDialog.findViewById(R.id.dlgQuant);
        //pricetxtvw.setText(commaAdder.format(price));
        //nametxtvw.setText(Name);
        //quanttxtvw.setText(quant);
        // LinearLayout linearLayout = (LinearLayout) alertDialog.findViewById(R.id.adlinlay);
//if (mainOptions){
    InitializeOptionsDialog(alertDialog);
   // mainOptions = false;
//}
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                //TinyDB tinyDB2 = new TinyDB(this);
                //Do something here
                //searched = true;
                unSearchedArrayList = arrayList;
                //tinyDB2.putBoolean("searchable", searched);
                return true;

            case R.id.app_bar_settings:
                //Do something here
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;

            default:
                //if we got here, the users action was not recognized
                //invoke theSuperClass
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_app_bar, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Enter List Name");
       final TinyDB tinyDB2 = new TinyDB(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do something here
                searched = true;
                //unSearchedArrayList = arrayList;
                //tinyDB2.putBoolean("searchable", searched);

                listViewAdapter.getFilter().filter(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listViewAdapter.notifyDataSetChanged();
                return false;
            }
        });
        //Configure the search info and add any Listeners

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onResume() {
        super.onResume();
        TinyDB tinyDB21 = new TinyDB(this);
        loadSharedPref(tinyDB21);
        refreshList();
    }
    private void loadSharedPref(TinyDB tdb2) {
        arrayList.clear();
        arrayListString = tdb2.getListString("TTK");
        arrayList = tdb2.getListObject(Mainkey, TitleItems.class);
        unSearchedArrayList = arrayList;
        //titleKeys = tdb2.getListString("1507");
    }
    public  void refreshList(){
        //listViewAdapter = new ListViewAdapter(MainActivity.this, R.layout.main_menu_listview_resource, arrayListTitle);
        //listView.setAdapter(listViewAdapter);
        ReturnObjecttoTitleItems(arrayList);
       listViewAdapter.notifyDataSetChanged();
    }
    private void getItemKey(String listName, int listPosition, TinyDB tinyDB){
       //itemname = this.items.getTitleName();
        tinyDB.putString("MainListName", listName);
        tinyDB.putInt("MainListPosition", listPosition);
    }
    private void InitializeOptionsDialog(AlertDialog alertDialog){
    renameBtn = (TextView) alertDialog.findViewById(R.id.EditMainOptions);
    deleteBtn  = (TextView) alertDialog.findViewById(R.id.deleteMainOptions);
    shareBtn  = (TextView) alertDialog.findViewById(R.id.shareMainOptions);

        final TinyDB tinyDB2 = new TinyDB(this);
    renameBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

            dismissDialog(1);
            final Intent intent2 = new Intent(MainActivity.this, NewListActivity.class);
            editItem = true;
            tinyDB2.putBoolean("editItem?", editItem);
            startActivity(intent2);
            dismissDialog(1);
        }
    });

        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(1);
               showDeleteDialog();
                dismissDialog(1);
            }
        });

        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                TinyDB tinyDB = new TinyDB(MainActivity.this);
                ArrayList ItemOptions = tinyDB.getListObject(itemname, NewListItems.class);
                if (theme == 2){
                    setTheme(R.style.AppTheme2);
                }

                newListView = findViewById(R.id.savedItemListView);

                final WorkMenuListAdapter wlistViewAdapter = new WorkMenuListAdapter(MainActivity.this, R.layout.list_item, ItemOptions);
                newListView.setAdapter(wlistViewAdapter);
                //newListView.setBackgroundColor(Color.WHITE);

               //Bitmap listviewBMP = loadBitmapFromView(listView);
                isExternalStorageWriteable();
                //Uri share = saveImageToExternalStorage(listviewBMP);
                Uri share = saveImageToExternalStorage(getWholeListViewItemsToBitmap());
                shareimageUri(share);
                if (theme == 2){
                setTheme(R.style.AppTheme);}
                listView.setAdapter(listViewAdapter);
                //listView.setBackgroundColor(Color.TRANSPARENT);
                dismissDialog(1);
            }
        });
}
    private void InitializeRenameDialog(AlertDialog alertDialog){
        editTextBox = (EditText) alertDialog.findViewById(R.id.editTextRenameDialog);
        saveBtn = (Button) alertDialog.findViewById(R.id.saveBtnRenameDialog);
        cancelBtn = (Button) alertDialog.findViewById(R.id.cancelBtnRenameDialog);

        editTextBox.setText(items.toString());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rename");
        alertDialog.setTitle(stringBuilder.toString());
    }
    private void ReturnObjecttoTitleItems(ArrayList arrayList){
        arrayListTitle.clear();
        arrayListTitle.addAll(arrayList);
    }
    private static void deleteItem(int pos, TinyDB tdb2, String itemname){
        //itemMainkey = titleKeys.get(pos);
        tdb2.remove(itemname);
        arrayListTitle.remove(pos);
        arrayList.remove(pos);
        unSearchedArrayList = arrayList;
        tdb2.remove(Mainkey);
        tdb2.putListObject(Mainkey, arrayList);
        //titleKeys.remove(pos);

    }
    public static class DeleteItemDialog extends DialogFragment {
    @NonNull
    @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final TinyDB tinyDB2 = new TinyDB(getActivity());

        builder.setMessage("Delete Item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(pos, tinyDB2, itemname);
                        MainActivity mainActivity = (MainActivity)getActivity();
                        mainActivity.refreshList();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //return super.onCreateDialog(savedInstanceState);

        //AlertDialog alrtdialog = builder.create();
        return  builder.create();
    }
}
    public void showDeleteDialog(){
        DialogFragment dialogFragment = new DeleteItemDialog();
        dialogFragment.show(getSupportFragmentManager(), "My Fragment");
 }
    public static class RenameItemDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Rename List")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            //return super.onCreateDialog(savedInstanceState);

            //AlertDialog alrtdialog = builder.create();
            return  builder.create();
        }
    }
    public void showRenameDialog(){
        DialogFragment dialogFragment = new RenameItemDialog();
        dialogFragment.show(getSupportFragmentManager(), "My Fragment");
    }
   /* public  static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }*/
    public static  void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath,fileName);

        try{
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG,85,fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "No App Available", Toast.LENGTH_SHORT).show();
        }
        }
    public static Bitmap loadBitmapFromView(View v){
    Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Drawable drawable = v.getBackground();
        if (drawable != null) {
            drawable.draw(c);
        } else{
            c.drawColor(Color.WHITE);}
            //v.draw(c);
            return b;
        }
    private Uri saveImageToExternalStorage(Bitmap image){
    Uri uri = null;
    try {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), itemname+".png");
        FileOutputStream stream = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.PNG,90,stream);
        stream.close();
        uri = Uri.fromFile(file);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return uri;
}
    public Boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return  true;
        }
        return false;
        }
    private void shareimageUri(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        //startActivity(intent);
        startActivity(Intent.createChooser(intent, "Share List"));
    }
    public static Bitmap getWholeListViewItemsToBitmap() {

        ListView listview = MainActivity.newListView;
        ListAdapter adapter = listview.getAdapter();
        int itemscount = adapter.getCount();
        int allitemsheight = 0;
        List<Bitmap> bmps = new ArrayList<Bitmap>();

        for (int i = 0; i < itemscount; i++) {

            View childView = adapter.getView(i, null, listview);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight += childView.getMeasuredHeight();
        }

        Bitmap bigbitmap = Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bigbitmap);
        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setTextSize(50);
        paint1.setFlags(Paint.ANTI_ALIAS_FLAG);


        String newItemName = "";
        if (itemname.length() > 15) {
           itemname = itemname.substring(0, Math.min(itemname.length(),15));
           itemname = itemname+"...";
        }
            bigcanvas.drawText(itemname, 90, 50, paint1);
        //bigcanvas.drawLine(0,51, listview.getMeasuredWidth(), 51, paint1);
        //bigcanvas.drawPoint(30.0f, 50.0f, paint1);

        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight+=bmp.getHeight();

            bmp.recycle();
            bmp=null;
        }


Bitmap bitmap = Bitmap.createBitmap(bigbitmap.getWidth(), bigbitmap.getHeight(), bigbitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bigbitmap, 0, 0, null);

        bigbitmap = bitmap;
        return bigbitmap;
    }
    public Bitmap bitmapOverlayToCenter(Bitmap bitmap1, Bitmap overlayBitmap) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = overlayBitmap.getWidth();
        int bitmap2Height = overlayBitmap.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);

        Bitmap finalBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(overlayBitmap, marginLeft, marginTop, null);
        return finalBitmap;
    }
    public void updatePreferences(){
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //sortable = defaultSharedPreferences.getBoolean(SORT, false);
        themeOn = defaultSharedPreferences.getBoolean(THEMES_ON, true);
        //sortBy = Integer.parseInt(defaultSharedPreferences.getString(SORTVALUES, "2"));
        //order = Integer.parseInt(defaultSharedPreferences.getString(A_OR_D, "1"));
        theme = Integer.parseInt(defaultSharedPreferences.getString(THEME, "2"));
    }
    public void activateTheme(int theme, boolean themeOn) {
        if (themeOn) {
            if (theme == 1) {
                setTheme(R.style.AppTheme2);
            }
            if (theme == 2) {
                setTheme(R.style.AppTheme);
            }
            if (theme == 3) {
                setTheme(R.style.AppThemeRed);
            }
            if (theme == 4) {
                setTheme(R.style.AppThemeOrange);
            }
            if (theme == 5) {
                setTheme(R.style.AppThemeYellow);
            }
            if (theme == 6) {
                setTheme(R.style.AppThemeGreen);
            }
            if (theme == 7) {
                setTheme(R.style.AppThemeBlue);
            }
            if (theme == 8) {
                setTheme(R.style.AppThemeIndigo);
            }
            if (theme == 9) {
                setTheme(R.style.AppThemeCyan);
            }
            if (theme == 10) {
                setTheme(R.style.AppThemeBrown);
            }
            if (theme == 11) {
                setTheme(R.style.AppThemeGrey);
            }
        }
    }
}//End of MAINClass