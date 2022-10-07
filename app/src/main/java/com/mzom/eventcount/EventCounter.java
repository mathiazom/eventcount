package com.mzom.eventcount;

/*

    Counter to keep track of both people inside and outside (interior and exterior)

*/

import android.support.annotation.NonNull;

class EventCounter {

    private int mInteriorCount;
    private int mExteriorCount;

    private final EventCounterListener mCallback;

    interface EventCounterListener{

        void onEventCounterChanged();

    }

    EventCounter(int interiorCount, int exteriorCount, @NonNull EventCounter.EventCounterListener callback){

        this.mInteriorCount = interiorCount;
        this.mExteriorCount = exteriorCount;

        this.mCallback = callback;
    }

    void setInteriorCount(int interiorCount){
        this.mInteriorCount = interiorCount;
        onEventCounterChanged();
    }

    void setExteriorCount(int exteriorCount){
        this.mExteriorCount = exteriorCount;
        onEventCounterChanged();
    }

    int getInteriorCount(){
        return this.mInteriorCount;
    }

    int getExteriorCount(){
        return this.mExteriorCount;
    }

    void incrementInteriorCount(int increment){

        this.mInteriorCount += increment;

        onEventCounterChanged();

    }

    void incrementExteriorCount(int increment){

        this.mExteriorCount += increment;

        onEventCounterChanged();

    }

    // Register that a person has left the event area (but might return)
    void registerExit(){

        // Reduce interior count by 1
        this.mInteriorCount -= 1;

        // Increment exterior count by 1
        this.mExteriorCount += 1;

        onEventCounterChanged();

    }

    // Register that a person, that previously left, has now returned
    void registerReturn(){

        // Increment interior count by 1
        this.mInteriorCount += 1;

        // Reduce exterior count by 1
        this.mExteriorCount -= 1;

        onEventCounterChanged();

    }

    private void onEventCounterChanged(){

        if(this.mCallback != null){
            this.mCallback.onEventCounterChanged();
        }

    }
}
