package com.iamback.talistpagetwo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewListActivity extends AppCompatActivity {

    public static ListView newListItems;
    public static EditText newName;
    public static EditText newPrice;
    public static EditText newAmount;
    public static TextView title;
    public static Button saveSubItem;
    public static FloatingActionButton saveEList;

    String editListName;
    int position;
    String formerDate;

    public static ArrayList<Object> newItemObj =    new ArrayList<Object>();
    public static ArrayList<Object> mainMenuItemObj = new ArrayList<Object>();
    public static ArrayList<String> titleKeys = new ArrayList<String>();

    public static ArrayList<NewListItems> newItem =    new ArrayList<NewListItems>();
    public static ArrayList<TitleItems> mainMenuItem = new ArrayList<TitleItems>();

    //ArrayList<String> titleKeys =   new ArrayList<>();

    public static String Mainkey = "2240";
    public static String Mainkey2 = "";

    int play = android.R.drawable.ic_media_play;
    int pause = android.R.drawable.ic_media_pause;

    public static String ItemName;
    public static String ItemPrice;
    public static String quantity;

    public static String newListName = "List";
    public static Double Total  = 0.0;

    public static boolean renameTitleblock = false;
    public static boolean noPrice = true;
    public static boolean noAmount = false;

    public static boolean editItem = false;

    public  static EditText nameEdtTxt;
    public  static CheckBox priceInclusive;
    public  static CheckBox amountInclusive;

    public  static Button savebutton;
    public  static Button deletebutton;
    int pos = 0;
    int oldPosition = 0;

    String originalListName = " ";
    NewListItems newListItemsObj;

    EditText nameEdtTxtSavedialog;
    NewItemsListadapter newItemsListadapter;
    TextView tutorialTextView;

    //public  static  final String SORT = "SORT";
    //public static final String SORTVALUES = "SORT_VALUES";
    //public static final String A_OR_D = "A_OR_D";
    public static final String THEMES_ON = "THEMES_ON";
    public static final String THEME = "THEME";

    //boolean sortable = true;
    //int sortBy = 2;
    //int order = 1;
    boolean themeOn = false;
    int theme = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updatePreferences();
        activateTheme(theme, themeOn);
        setContentView(R.layout.activity_new_list);

        initializeNewListactivityItems();

        setButtonBackResource(theme);
        final TinyDB tinyDB = new TinyDB(this);

        editItem = tinyDB.getBoolean("editItem?");
        titleKeys = tinyDB.getListString("TTK");

        if (editItem){
            tutorialTextView.setVisibility(View.GONE);
            loadSharedPref(tinyDB);
            Toast.makeText(NewListActivity.this, "Past session will be cleared!", Toast.LENGTH_SHORT).show();
            //editItem = false;
            //tinyDB.putBoolean("editItem?", editItem);
                     }else{
            newListItems.setVisibility(View.GONE);
        }

        //final NewItemsListadapter newItemsListadapter = new NewItemsListadapter(this, R.layout.new_item_list_resource, newItemObj);
        newItemsListadapter = new NewItemsListadapter(this, R.layout.new_item_list_resource, newItemObj);
        newListItems.setAdapter(newItemsListadapter);

        NewListActivity.this.showDialog(1);
        //showNewItemDialog();

        saveEList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newItemObj.size()>0){
                    storeEntireNewList(tinyDB);
                    newItemObj.clear();
                    mainMenuItemObj.clear();
                    newItemsListadapter.notifyDataSetChanged();
                    NewListActivity.this.finish();
                    overridePendingTransition(R.anim.out_zoom_out, R.anim.out_zoom_in);
                }else {
                    Toast.makeText(NewListActivity.this, "List is empty!", Toast.LENGTH_SHORT).show();
                }

                if(editItem){
                    editItem = false;
                    tinyDB.putBoolean("editItem?", editItem);
                }
}
            });

        saveSubItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

              //titleKeys.add(newListName);
              //tinyDB.putListString("TTK", titleKeys);

                if (TextUtils.isEmpty(NewListActivity.this.newName.getText())) {

                    Toast.makeText(NewListActivity.this, "Name Cannot be Empty!", Toast.LENGTH_SHORT).show();

                } else {
                    if (TextUtils.isEmpty(NewListActivity.this.newPrice.getText())) {
                        newPrice.setText("0");
                    }
                    if (!editItem){
                        tutorialTextView.setVisibility(View.GONE);
                    }
                    newListItems.setVisibility(View.VISIBLE);
                    storeNewSubObject(newItemsListadapter);
                }
                newName.setFocusable(true);
                newName.setFocusableInTouchMode(true);
                newName.requestFocus();
            }});

        newName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!editItem){
                   tutorialTextView.setVisibility(View.GONE);
                   newListItems.setVisibility(View.VISIBLE);
               }
            }});

        newAmount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editItem){
                    tutorialTextView.setVisibility(View.GONE);
                    newListItems.setVisibility(View.VISIBLE);
                }
            }});

        newPrice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editItem){
                    tutorialTextView.setVisibility(View.GONE);
                    newListItems.setVisibility(View.VISIBLE);
                }
            }});

        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                renameTitleblock = true;
                NewListActivity.this.showDialog(1);
                 }});

        this.newListItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                NewListActivity.this.newListItemsObj = newItemsListadapter.getItem(position);
                String itemname = newListItemsObj.getTitleName();
                Double price = newListItemsObj.getPrice();
                String quantity = newListItemsObj.getQuantity();

                Total -= price;

                newName.setText(itemname);
                newPrice.setText(String.valueOf(price));
                newAmount.setText(quantity);

                newItemObj.remove(position);
                newItemsListadapter.notifyDataSetChanged();
                return true;
            }
        });

        this.newListItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewListActivity.this.newListItemsObj = newItemsListadapter.getItem(position);
                oldPosition = position;
                String itemname = newListItemsObj.getTitleName();
                Double price = newListItemsObj.getPrice();
                String quantity = newListItemsObj.getQuantity();

               // newName.setText(itemname);
               // newPrice.setText(String.valueOf(price));
               // newAmount.setText(quantity);

                Total -= price;

                newItemObj.remove(position);
                newItemsListadapter.notifyDataSetChanged();
            }
        });

    }
    public Dialog onCreateDialog (int i){
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_full_details, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create New List")
            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newListName = String.valueOf(nameEdtTxtSavedialog.getText());
                    title.setText(newListName);
                    closeKeyBoard();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!renameTitleblock) {
                        closeKeyBoard();
                        NewListActivity.this.finish();
                    }
                    NewListActivity.this.dismissDialog(1);
                    renameTitleblock = false;
                }
            });
        builder.setView(inflate);
        return builder.create();
    }
    public void onPrepareDialog ( int i, Dialog dialog){
        //DecimalFormat commaAdder:commaAdder = new DecimalFormat("#,###.####");commaAdder.setRoundingMode(RoundingMode.CEILING)
        AlertDialog alertDialog = (AlertDialog) dialog;
        /*StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(Name);
        alertDialog.setTitle(stringBuilder.toString());
        final CheckBox priceInclusive = (CheckBox) alertDialog.findViewById(R.id.priceCheckBox);*/
        InitializeOptionsDialog(alertDialog);
        showKeyBoard();
        /*nameEdtTxtSavedialog.requestFocus();
        //if(nameEdtTxtSavedialog.requestFocus()){
          //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE); }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(nameEdtTxtSavedialog, InputMethodManager.SHOW_FORCED);
        nameEdtTxtSavedialog.setSelectAllOnFocus(true);*/
        }
    private void storeNewSubObject(NewItemsListadapter nxt){
        ItemName = String.valueOf(newName.getText());
        ItemPrice = String.valueOf(newPrice.getText());
        if (ItemPrice == "") {
            ItemPrice = "0";
        }
        quantity = String.valueOf(newAmount.getText());

        Double smh = Double.parseDouble(ItemPrice);
        if (oldPosition > 0) {
            newItemObj.add(oldPosition, new NewListItems(ItemName, quantity, smh));
            nxt.notifyDataSetChanged();
            oldPosition = 0;

        }else{
            newItemObj.add(new NewListItems(ItemName, quantity, smh));
            nxt.notifyDataSetChanged();
        }

        Total+=smh;

        newPrice.setText("");
        newName.setText("");
        newAmount.setText("");
    }
    private void storeEntireNewList(TinyDB tdb){

        mainMenuItemObj = tdb.getListObject(Mainkey, TitleItems.class);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();
       // dateFormat.format(date);

        titleKeys = tdb.getListString("TTK");

        String datestring;

        if (editItem){
            datestring = formerDate;
        }
        else{
            datestring = String.valueOf(dateFormat.format(date));
        }

        Double sessionTotal = 0.0;
        if (editItem){
            tdb.remove(editListName);
            tdb.remove(originalListName+"ongo");
            titleKeys.remove(position);
            TitleItems titleItems = mainMenuItem.get(position);
            sessionTotal = titleItems.getTotalPrice();
            mainMenuItemObj.remove(position);
        }

            if (titleKeys.isEmpty()) { }
            else {
                for (int maxSameNameError = 0; maxSameNameError < 1000; maxSameNameError++) {
                    for (int i = 0; i < titleKeys.size(); i++) {
                        String securityMan = titleKeys.get(i);
                        if (newListName.length() == securityMan.length() && newListName.contains(securityMan))
                        {
                            newListName = newListName + " ";
                        }
                    }
                }
            }

        tdb.putListObject(newListName, newItemObj);

        titleKeys.add(position, newListName);
        tdb.putListString("TTK", titleKeys);

        if (editItem) {
            mainMenuItemObj.add(position, new TitleItems(newListName, sessionTotal, datestring, android.R.drawable.ic_media_play));
        }else{
            mainMenuItemObj.add(new TitleItems(newListName, Total, datestring, android.R.drawable.ic_media_play));
        }
        //R.id.android:drawable/ic_media_pause

        tdb.putListObject(Mainkey, mainMenuItemObj);
        Toast.makeText(NewListActivity.this, newListName+" Saved", Toast.LENGTH_SHORT).show();
    }//end of store entire list method
    public void closeActivity(NewListActivity newListActivity){
        NewListActivity.this.finish();
        newItemObj.clear();
        mainMenuItemObj.clear();
    }
    private void InitializeOptionsDialog(AlertDialog alertDialog){
        nameEdtTxtSavedialog = (EditText) alertDialog.findViewById(R.id.listNameEdtText);
        if (editItem) {
            nameEdtTxtSavedialog.setText(editListName);
        }
        if(renameTitleblock) {
            //priceInclusive.setVisibility(View.GONE);
        }
    }
    private void loadSharedPref(TinyDB tdb2) {
        position = tdb2.getInt("MainListPosition");
        mainMenuItemObj = tdb2.getListObject("2240", TitleItems.class);
        ReturnObjecttoTitleItems(mainMenuItemObj, mainMenuItem);
        TitleItems getTotalObject;
        getTotalObject = mainMenuItem.get(position);
        Total = getTotalObject.getTotalPrice();
        editListName = getTotalObject.getTitleName();
        originalListName=editListName;
        formerDate = getTotalObject.getDate();

       String listNameKey = tdb2.getString("MainListName");
        newItemObj = tdb2.getListObject(listNameKey, NewListItems.class);
        ReturnObjecttoTitleItems(newItemObj, newItem);

        //titleArray.remove(position);

        //statesaveTitleitem.setImageSrc(pause);
        //titleArray.add(position, statesaveTitleitem);

        //titleArrayObj.clear();
        //titleArrayObj.addAll(titleArray);
        //tinyDB.putListObject("2240", titleArrayObj);
    }
    private void ReturnObjecttoTitleItems(ArrayList arrayListObj, ArrayList arrayListMain){
        arrayListMain.clear();
        arrayListMain.addAll(arrayListObj);
    }
    private void initializeNewListactivityItems(){
    newListItems = findViewById(R.id.listItemListView);
    newName = findViewById(R.id.newNameEdtTxt);
        saveSubItem = findViewById(R.id.addButton);
    newPrice = findViewById(R.id.newPriceEdtTxt);
    title = findViewById(R.id.Title);
    saveEList = findViewById(R.id.floatingAddButton);
    newAmount = findViewById(R.id.newAmountEdtTExt);
    tutorialTextView = findViewById(R.id.tutorialTextView);
}
    @Override
    public void onBackPressed() {
        if (newItemObj.size()>0) {
            showLeavepage();

        }else {
            closeKeyBoard();
            super.onBackPressed();
            overridePendingTransition(R.anim.out_zoom_out, R.anim.out_zoom_in);
        }
    }
    public static class LeavePageDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final TinyDB tinyDB2 = new TinyDB(getActivity());

            builder.setMessage("Leave Page? All new items will be deleted!")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NewListActivity mainActivity = (NewListActivity) getActivity();
                            final TinyDB tinyDB = new TinyDB(mainActivity);

                            if (editItem){
                                editItem = false;
                                tinyDB.putBoolean("editItem?", editItem);
                            }
                            newItemObj.clear();
                            newItem.clear();
                            mainActivity.finish();
                            mainActivity.overridePendingTransition(R.anim.out_zoom_out, R.anim.out_zoom_in);
                            //MainActivity mainActivity = (MainActivity)getActivity();
                            //mainActivity.refreshList();
                        }
                    })
                    .setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NewListActivity mainActivity = (NewListActivity) getActivity();
                            mainActivity.dismissDialog(1);
                        }
                    }).setNeutralButton("Save Current Items", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NewListActivity mainActivity = (NewListActivity) getActivity();
                    final TinyDB tinyDB = new TinyDB(mainActivity);

                    mainActivity.storeEntireNewList(tinyDB);
                    newItemObj.clear();
                    mainMenuItemObj.clear();
                    mainActivity.newItemsListadapter.notifyDataSetChanged();
                    mainActivity.finish();
                    mainActivity.overridePendingTransition(R.anim.out_zoom_out, R.anim.out_zoom_in);

                }
            });
            //return super.onCreateDialog(savedInstanceState);

            //AlertDialog alrtdialog = builder.create();
            return  builder.create();
        }
    }
    public static class SessionClearedDialog extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final TinyDB tinyDB2 = new TinyDB(getActivity());

            builder.setMessage("Leave Page? All new items will be deleted!")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NewListActivity mainActivity = (NewListActivity) getActivity();
                            final TinyDB tinyDB = new TinyDB(mainActivity);

                            if (editItem){
                                editItem = false;
                                tinyDB.putBoolean("editItem?", editItem);
                            }
                            newItemObj.clear();
                            newItem.clear();
                            mainActivity.finish();
                            mainActivity.overridePendingTransition(R.anim.out_zoom_out, R.anim.out_zoom_in);
                            //MainActivity mainActivity = (MainActivity)getActivity();
                            //mainActivity.refreshList();
                        }
                    })
                    .setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NewListActivity mainActivity = (NewListActivity) getActivity();
                            mainActivity.dismissDialog(1);
                        }
                    }).setNeutralButton("Save Current Items", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NewListActivity mainActivity = (NewListActivity) getActivity();
                    final TinyDB tinyDB = new TinyDB(mainActivity);

                    mainActivity.storeEntireNewList(tinyDB);
                    newItemObj.clear();
                    mainMenuItemObj.clear();
                    mainActivity.newItemsListadapter.notifyDataSetChanged();
                    mainActivity.finish();
                    mainActivity.overridePendingTransition(R.anim.out_zoom_out, R.anim.out_zoom_in);

                }
            });
            //return super.onCreateDialog(savedInstanceState);

            //AlertDialog alrtdialog = builder.create();
            return  builder.create();
        }
    }
    public void showLeavepage(){
        DialogFragment dialogFragment = new LeavePageDialog();
        dialogFragment.show(getSupportFragmentManager(), "My Fragment");
    }
    public void showKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    public void closeKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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
    public void setButtonBackResource(int theme) {
        if (themeOn) {
            if (theme == 1) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_black);
            }
            if (theme == 2) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle);
            }
            if (theme == 3) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_red);
            }
            if (theme == 4) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_orange);
            }
            if (theme == 5) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_yellow);
            }
            if (theme == 6) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_green);
            }
            if (theme == 7) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_blue);
            }
            if (theme == 8) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_indigo);
            }
            if (theme == 9) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_cyan);
            }
            if (theme == 10) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_brown);
            }
            if (theme == 11) {
                saveSubItem.setBackgroundResource(R.drawable.rectangle_gray);
            }
        }
        }


}//End of Main Class
