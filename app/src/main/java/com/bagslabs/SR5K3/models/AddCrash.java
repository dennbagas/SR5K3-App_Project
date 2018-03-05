package com.bagslabs.SR5K3.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class AddCrash {

    public String uid;
    public String reporter;
    public String area;
    public String problem;
    public String cause;
    public String action;
    public String tanggal;
    public String pukul;

    public AddCrash() {
        // Default constructor required for calls to DataSnapshot.getValue(AddCrash.class)
    }

    public AddCrash(String uid, String reporter, String area, String problem, String cause, String action, String tanggal, String pukul) {
        this.uid = uid;
        this.reporter = reporter;
        this.area = area;
        this.problem = problem;
        this.cause = cause;
        this.action = action;
        this.tanggal = tanggal;
        this.pukul = pukul;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("reporter", reporter);
        result.put("area", area);
        result.put("problem", problem);
        result.put("cause", cause);
        result.put("action", action);
        result.put("tanggal", tanggal);
        result.put("pukul", pukul);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
