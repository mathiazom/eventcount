package com.mzom.eventcount;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/*
    Class to save and restore counter instances
 */

class EventCounterStorage {

    static void storeCounterInstance(@NonNull Context context, @NonNull EventCounter eventCounter){

        // Get a handle to shared preferences
        final SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.event_counter_key),Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store counter values
        editor.putInt(context.getString(R.string.interior_count_key),eventCounter.getInteriorCount());
        editor.putInt(context.getString(R.string.exterior_count_key),eventCounter.getExteriorCount());

        // Apply changes
        editor.apply();

    }

    static EventCounter retrieveStoredCounterInstance(@NonNull Context context,@NonNull EventCounter.EventCounterListener callback){

        // Get a handle to shared preferences
        final SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.event_counter_key),Context.MODE_PRIVATE);

        // Retrieve counter values
        int interiorCount = sharedPreferences.getInt(context.getString(R.string.interior_count_key),0);
        int exteriorCount = sharedPreferences.getInt(context.getString(R.string.exterior_count_key),0);

        // Return counter with retrieved values
        return new EventCounter(interiorCount,exteriorCount,callback);

    }

}
