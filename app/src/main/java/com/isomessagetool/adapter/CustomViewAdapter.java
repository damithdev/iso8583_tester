package com.isomessagetool.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.isomessagetool.R;
import com.isomessagetool.pojo.ViewItem;

import java.util.ArrayList;
import java.util.Collections;

public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.CustomViewHolder>{

    private ArrayList<ViewItem> viewItemsList;
    private Context mContext;

    public CustomViewAdapter(ArrayList<ViewItem> list, Context _ctx) {
        this.viewItemsList = list;
        this.mContext = _ctx;
    }


    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // inflate the layout view created for the list view
        View view = layoutInflater.inflate(R.layout.view_item, parent, false);
        return new CustomViewHolder(view);
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        final ViewItem item = viewItemsList.get(position);

        //set the data to the view here

        holder.setItem(item);
        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public
        holder.getButtonDelete().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String itemLabel = String.valueOf( viewItemsList.get(position).getBit());
                viewItemsList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,viewItemsList.size());
                Toast.makeText(mContext, "Item "+ itemLabel + " Removed!",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        holder.getLayout().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showRecordItemDialog(viewItemsList.get(position),position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return viewItemsList == null ? 0 : viewItemsList.size();
    }

    public void showRecordItemDialog(){
        showAlertDialogButtonClicked(null,-1);
    }
    private void showRecordItemDialog(ViewItem item, int position){
        showAlertDialogButtonClicked(item,position);
    }


    private void showAlertDialogButtonClicked(ViewItem item,int position) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Insert ISO Record");
        // set the custom layout
        final View customLayout = getMyLayoutInflator().inflate(R.layout.record_input_dialog, null);
        builder.setView(customLayout);



        if(item != null){
            builder.setTitle("Update ISO Record");
            EditText editBit = customLayout.findViewById(R.id.editBit);
            EditText editValue = customLayout.findViewById(R.id.editValue);
            editBit.setText(String.valueOf(item.getBit()));
            editValue.setText(item.getValue());
            TextView indexView = customLayout.findViewById(R.id.record_index);
            indexView.setText(String.valueOf(position));
        }



        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText editBit = customLayout.findViewById(R.id.editBit);
                EditText editValue = customLayout.findViewById(R.id.editValue);
                TextView indexView = customLayout.findViewById(R.id.record_index);

                String pos = indexView.getText().toString();

                try {

                    if(pos.isEmpty()){
                        sendDialogDataToActivity(new ViewItem(Integer.parseInt(editBit.getText().toString()),editValue.getText().toString()),-1);
                    }else{
                        int posidx = Integer.parseInt(pos);
                        sendDialogDataToActivity(new ViewItem(Integer.parseInt(editBit.getText().toString()),editValue.getText().toString()),posidx);
                    }
                }catch (Exception e){
                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG);
                }

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private LayoutInflater getMyLayoutInflator(){
        return (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(ViewItem data,int position) {
        if(data.getBit() > 0 && !data.getValue().isEmpty()){

            if(position >= 0){
                viewItemsList.set(position,data);
                Toast.makeText(mContext, "Record Updated", Toast.LENGTH_SHORT).show();

            }else{
                viewItemsList.add(data);
                Toast.makeText(mContext, "New Record Added", Toast.LENGTH_SHORT).show();

            }
            Collections.sort(viewItemsList);
            this.notifyDataSetChanged();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtValue;
        private TextView txtField;
        private Button btnDelete;
        private LinearLayout layout;
        private CardView card;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            txtField = itemView.findViewById(R.id.txt_field);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtValue = itemView.findViewById(R.id.txt_value);
            btnDelete = itemView.findViewById(R.id.delete_btn);
            layout = itemView.findViewById(R.id.view_item_main);
            card = itemView.findViewById(R.id.card_view);

        }

        public void setTxtField(String field){
            txtField.setText(field);
        }

        public void setTxtTitle(String name) {
            txtTitle.setText(name);
        }

        public void setTxtValue(String number) {
            txtValue.setText(number);
        }

        public void setFieldError(boolean hasError){
            if(hasError){
                card.setCardBackgroundColor(Color.YELLOW);
            }else{
                card.setCardBackgroundColor(Color.LTGRAY);
            }
        }

        public Button getButtonDelete(){
            return btnDelete;
        }

        public LinearLayout getLayout(){
            return layout;
        }

        public void setItem(ViewItem item) {
            setTxtTitle(String.valueOf(item.getBit()));
            setTxtValue(item.getValue());
            setTxtField(item.getField().toString());

            if(item.getValue().length() > item.getField().getLength()){
                setFieldError(true);
            }else{
                setFieldError(false);
            }
        }
    }
}
