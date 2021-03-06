package com.felixkalu.kweekmed;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SymptomsAdapter extends BaseAdapter{

    Activity activity;
    List<SymptomModel> symptoms;
    List<SymptomModel> symptomsCopy;
    LayoutInflater inflater;

    public SymptomsAdapter(Activity activity) {
        this.activity = activity;
    }

    public SymptomsAdapter(Activity activity, List<SymptomModel> symptoms) {
        this.activity = activity;
        this.symptoms = symptoms;
        this.symptomsCopy = new ArrayList<SymptomModel>();
        this.symptomsCopy.addAll(symptoms);

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return symptoms.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if(view == null) {

            view = inflater.inflate(R.layout.symptoms_list_view_item, viewGroup, false);

            holder = new ViewHolder();

            holder.tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);

            view.setTag(holder);

        } else
            holder = (ViewHolder)view.getTag();

        SymptomModel model = symptoms.get(i);

        holder.tvUserName.setText(model.getName());

        if(model.isSelected())
            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);
        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.check);

        return view;

    }

    public void updateRecords(List<SymptomModel> users) {
        this.symptoms = users;
        notifyDataSetChanged();
    }

    class ViewHolder {

        TextView tvUserName;
        ImageView ivCheckBox;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        symptoms.clear();
        if (charText.length()==0){
            symptoms.addAll(symptomsCopy);
        }
        else {
            for (SymptomModel symptom : symptomsCopy){
                //This tells the searchView to search with book title or book Author
                if (symptom.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    symptoms.add(symptom);
                }
            }
        }
        notifyDataSetChanged();
    }
}
