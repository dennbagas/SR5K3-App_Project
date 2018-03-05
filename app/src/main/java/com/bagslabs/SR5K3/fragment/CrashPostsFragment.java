package com.bagslabs.SR5K3.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class CrashPostsFragment extends CrashListFragment {

    public CrashPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("crash")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}
