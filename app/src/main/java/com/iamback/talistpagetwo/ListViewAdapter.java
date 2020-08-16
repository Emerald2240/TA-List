package com.iamback.talistpagetwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<TitleItems> {
    private Context mContext;
    int mResource;
    ArrayList<Object> Obj;

    public ListViewAdapter(Context context, int resource, ArrayList objects) {
   super(context, resource, objects);
   this.mContext = context;
   this.mResource = resource;
   this.Obj = objects;
}
    public TitleItems getItem(int position2) { return  (TitleItems) super.getItem(position2); }
    public View getView(final int position, View convertView, ViewGroup parent){

    DecimalFormat commaAdder;
    commaAdder = new DecimalFormat("#,###.####");
    commaAdder.setRoundingMode(RoundingMode.CEILING);

    String name = ((TitleItems) getItem(position)).getTitleName();
    Double price = ((TitleItems) getItem(position)).getTotalPrice();
    String date = ((TitleItems) getItem(position)).getDate();
    int imageRes = ((TitleItems) getItem(position)).getImageSrc();

    TitleItems itemsClassObject = new TitleItems(name, price, date, imageRes);


        convertView = LayoutInflater.from(this.mContext).inflate(this.mResource, parent, false);
        TextView itemname = convertView.findViewById(R.id.titleName);
        TextView pricetextview = convertView.findViewById(R.id.titleTotalAmount);
        ImageView close = convertView.findViewById(R.id.titleFileBitmap);
        TextView datetxtvw = convertView.findViewById(R.id.titleDate);

        itemname.setText(name);
        if (price == 0) {
            pricetextview.setText("");
        } else {
            pricetextview.setText("$" + commaAdder.format(price));
        }
        datetxtvw.setText(date);
        close.setImageResource(imageRes);

       // Animation animation = AnimationUtils
        //        .loadAnimation(getContext(), R.anim.entry_anim);
        //convertView.startAnimation(animation);

  return convertView;
  }

  }

