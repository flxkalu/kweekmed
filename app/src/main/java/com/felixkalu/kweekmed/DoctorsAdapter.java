package com.felixkalu.kweekmed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class DoctorsAdapter extends ArrayAdapter<DoctorsModel> {

    private Context bContext;
    private ArrayList<DoctorsModel> doctorsList = new ArrayList<>(); //Stores the actual booklist
    private ArrayList<DoctorsModel> doctorsListCopy; //this one is useful for the searchView process.

    public DoctorsAdapter(@NonNull Context context, ArrayList<DoctorsModel> list) {
        super(context, 0, list);
        bContext = context;
        this.doctorsList = list;
        this.doctorsListCopy = new ArrayList<DoctorsModel>();
        this.doctorsListCopy.addAll(doctorsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(bContext).inflate(R.layout.doctors_list_view_item, parent, false);

        DoctorsModel currentDoctor = doctorsList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.doctorImageView);
        Picasso.get().load(currentDoctor.getPhotoLink()).resize(100, 85).into(image);

        Log.i("Img link", currentDoctor.getPhotoLink());
        Log.i("name: ", currentDoctor.getName());

        TextView name = (TextView) listItem.findViewById(R.id.doctorNameTextView);
        name.setText(currentDoctor.getName());



        TextView specialty = (TextView) listItem.findViewById(R.id.doctorSpecialtyTextView);
        specialty.setText(currentDoctor.getSpecialty());

        TextView location = (TextView) listItem.findViewById(R.id.doctorLocationTextView);
        location.setText(currentDoctor.getLocation());

        return listItem;
    }

    //filter
    //this filter is what does the magic with the searchView
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        doctorsList.clear();
        if (charText.length()==0){
            doctorsList.addAll(doctorsListCopy);
        }
        else {
            for (DoctorsModel doctor : doctorsListCopy){
                //This tells the searchView to search with book title or book Author
                if (doctor.getName().toLowerCase(Locale.getDefault()).contains(charText)
                        || doctor.getSpecialty().toLowerCase(Locale.getDefault()).contains(charText)){
                    doctorsList.add(doctor);
                }
            }
        }
        notifyDataSetChanged();
    }
}
