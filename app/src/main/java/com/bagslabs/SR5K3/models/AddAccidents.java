package com.bagslabs.SR5K3.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class AddAccidents {

    public String nama;
    public String nik;
    public String bagian;
    public String kronologi;
    public String penanganan;
    public String tanggal;
    public String pukul;
    public String imageEncoded;
    public String atasan;

    public AddAccidents() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public AddAccidents(String nama, String nik, String bagian, String kronologi, String penanganan, String tanggal, String pukul, String imageEncoded, String atasan) {
        this.nama = nama;
        this.nik = nik;
        this.bagian = bagian;
        this.kronologi = kronologi;
        this.penanganan = penanganan;
        this.tanggal = tanggal;
        this.pukul = pukul;
        this.imageEncoded = imageEncoded;
        this.atasan = atasan;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nama", nama);
        result.put("nik", nik);
        result.put("bagian", bagian);
        result.put("kronologi", kronologi);
        result.put("penanganan", penanganan);
        result.put("tanggal", tanggal);
        result.put("pukul", pukul);
        result.put("imageUrl", imageEncoded);
        result.put("atasan", atasan);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
