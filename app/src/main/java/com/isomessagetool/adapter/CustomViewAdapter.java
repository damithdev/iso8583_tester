package com.isomessagetool.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;

import com.isomessagetool.R;
import com.isomessagetool.pojo.ViewItem;

import java.util.ArrayList;

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
        holder.setTxtTitle(item.getName());
        holder.setTxtValue(item.getValue());
        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public
        holder.getButtonDelete().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String itemLabel = viewItemsList.get(position).getName();
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
            EditText editBit = customLayout.findViewById(R.id.editBit);
            EditText editValue = customLayout.findViewById(R.id.editValue);
            editBit.setText(item.getName());
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
                        sendDialogDataToActivity(new ViewItem(editBit.getText().toString(),editValue.getText().toString()),-1);
                    }else{
                        int posidx = Integer.parseInt(pos);
                        sendDialogDataToActivity(new ViewItem(editBit.getText().toString(),editValue.getText().toString()),posidx);
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
        if(!data.getName().isEmpty() && !data.getValue().isEmpty()){
            Toast.makeText(mContext, "New Record Added", Toast.LENGTH_SHORT).show();

            if(position >= 0){
                viewItemsList.set(position,data);
            }else{
                viewItemsList.add(data);

            }

            this.notifyDataSetChanged();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtValue;
        private Button btnDelete;
        private LinearLayout layout;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtValue = itemView.findViewById(R.id.txt_value);
            btnDelete = itemView.findViewById(R.id.delete_btn);
            layout = itemView.findViewById(R.id.view_item_main);

        }

        public void setTxtTitle(String name) {
            txtTitle.setText(name);
        }

        public void setTxtValue(String number) {
            txtValue.setText(number);
        }

        public Button getButtonDelete(){
            return btnDelete;
        }

        public LinearLayout getLayout(){
            return layout;
        }
    }
}
