package com.bagslabs.SR5K3.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class AccidentsPostsFragment extends AccidentsListFragment {

    public AccidentsPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        // [END recent_posts_query]

        return databaseReference.child("accidents")
                .limitToFirst(100);
    }
}
