package com.felixkalu.kweekmed;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

//will push this file from the software gurus account to test and be sure i am understanding github better.
//I will use this comment to check and be sure things go file
public class DiagnosisAdapter extends BaseAdapter {

    Activity activity;
    List<DiagnosisModel> diagnosis;
    LayoutInflater inflater;

    public DiagnosisAdapter(Activity activity) {
        this.activity = activity;
    }

    public DiagnosisAdapter(Activity activity, List<DiagnosisModel> diagnosis) {
        this.activity = activity;
        this.diagnosis = diagnosis;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return diagnosis.size();
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

            view = inflater.inflate(R.layout.diagnosis_list_view_item, viewGroup, false);

            holder = new ViewHolder();

            holder.issueTextView = (TextView) view.findViewById(R.id.issueTextView);
            holder.specialistTextView = (TextView) view.findViewById(R.id.specialistTextView);
            holder.accuracyProgressBar = (ProgressBar) view.findViewById(R.id.acuuracyProgressBar);

            view.setTag(holder);

        } else
            holder = (ViewHolder)view.getTag();

        DiagnosisModel model = diagnosis.get(i);

        holder.issueTextView.setText(model.getIssueName());
        holder.specialistTextView.setText(model.getSpecialistType());
        holder.accuracyProgressBar.setMax(100);
        //this line of code changes the progressbar color to red if the OS is above lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.accuracyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }

        //holder.accuracyProgressBar.setProgress((int) Double.parseDouble(model.getAccuracy())); this line of code only shows the progressbar
        //this block of code not only displays the progressbar, but animates it to make it look more apealing..
        ObjectAnimator animation = ObjectAnimator.ofInt(holder.accuracyProgressBar, "progress", (int) Double.parseDouble(model.getAccuracy()));
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

//        if(model.isSelected())
//            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);
//        else
//            holder.ivCheckBox.setBackgroundResource(R.drawable.check);

        return view;
    }

    public void updateRecords(List<DiagnosisModel> users) {
        this.diagnosis = users;
        notifyDataSetChanged();
    }

    class ViewHolder {

        TextView issueTextView;
        TextView specialistTextView;
        ProgressBar accuracyProgressBar;

    }
}
