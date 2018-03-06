package com.bagslabs.SR5K3.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bagslabs.SR5K3.R;
import com.bagslabs.SR5K3.models.AddAccidents;

public class AccidentsViewHolder extends RecyclerView.ViewHolder {

    public TextView namaView;
    public TextView nikView;
    public TextView bagianView;
    public TextView kronologiView;
    public TextView penangananView;
    public ImageView post_photo;
    String imageUrl;

    public AccidentsViewHolder(View itemView) {
        super(itemView);

        namaView = itemView.findViewById(R.id.post_nama);
        nikView = itemView.findViewById(R.id.post_nik);
        bagianView = itemView.findViewById(R.id.post_bagian);
        kronologiView = itemView.findViewById(R.id.post_kronologi);
        penangananView = itemView.findViewById(R.id.post_penanganan);
        post_photo = itemView.findViewById(R.id.post_photo);
    }

    public void bindToPost(AddAccidents addAccidents, View.OnClickListener starClickListener) {
        namaView.setText(addAccidents.nama);
        nikView.setText(addAccidents.nik);
        bagianView.setText(addAccidents.bagian);
        kronologiView.setText(addAccidents.kronologi);
        penangananView.setText(addAccidents.penanganan);
        imageUrl = addAccidents.imageUri;
    }
}
