package com.iamback.talistpagetwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class NewItemsListadapter  extends ArrayAdapter<NewListItems> {
        private Context mContext;
        int mResource;
        ArrayList<NewListItems> Obj;

        public NewItemsListadapter(Context context, int resource, ArrayList objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.mResource = resource;
            this.Obj = objects;
        }

        public NewListItems getItem(int position2) {
            return  (NewListItems) super.getItem(position2);
        }

        public View getView(final int position, View convertView, ViewGroup parent){

            DecimalFormat commaAdder;
            commaAdder = new DecimalFormat("#,###.####");
            commaAdder.setRoundingMode(RoundingMode.CEILING);

            String name = ((NewListItems) getItem(position)).getTitleName();
            Double price = ((NewListItems) getItem(position)).getPrice();
            String quant = ((NewListItems) getItem(position)).getQuantity();

            //NewListItems newItemsClassObject = new NewListItems(name, quant, price);

            convertView = LayoutInflater.from(this.mContext).inflate(this.mResource, parent, false);
            TextView itemname =  convertView.findViewById(R.id.newItemName);
            TextView pricetextview = convertView.findViewById(R.id.newItemPrice);
            TextView quantTExtView = convertView.findViewById(R.id.quantTextView);
            //ImageView close = convertView.findViewById(R.id.titleFileBitmap);
            //TextView datetxtvw = convertView.findViewById(R.id.titleDate);

            itemname.setText(name);
            if (price == 0){
                pricetextview.setText("");
            }else {
                pricetextview.setText("$" + commaAdder.format(price));
            }
            quantTExtView.setText(quant);
            //datetxtvw.setText(date);

            return convertView;
        }
    }
