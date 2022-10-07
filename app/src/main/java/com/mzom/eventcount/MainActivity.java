package com.mzom.eventcount;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements EventCounter.EventCounterListener {

    private EventCounter mEventCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use savedInstanceState to retrieve any saved EventCounter instances
        if(savedInstanceState != null){
            handleSavedInstanceState(savedInstanceState);
        }

        // If no valid EventCounter state could be found in savedInstanceState, check storage in SharedPreferences
        if(mEventCounter == null){
            mEventCounter = EventCounterStorage.retrieveStoredCounterInstance(this,this);
        }

        // Display EventCounter counts in UI views
        updateCounterViews();

        // Initialize counter button listeners
        addCounterListeners();
    }

    // Save EventCounter state for later retrieval from savedInstanceState
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mEventCounter != null){

            outState.putInt(getString(R.string.interior_count_key),mEventCounter.getInteriorCount());
            outState.putInt(getString(R.string.exterior_count_key),mEventCounter.getExteriorCount());

        }
    }

    // Retrieve EventCounter state from savedInstanceState
    private void handleSavedInstanceState(Bundle savedInstanceState) {

        // Retrieve saved EventCounter
        int interiorCount = savedInstanceState.getInt(getString(R.string.interior_count_key));
        int exteriorCount = savedInstanceState.getInt(getString(R.string.exterior_count_key));

        mEventCounter = new EventCounter(interiorCount,exteriorCount,this);

    }

    // Initiate all button listeners
    private void addCounterListeners(){

        final ImageButton registerReturnButton = findViewById(R.id.counter_return_button);
        registerReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventCounter.registerReturn();
            }
        });


        final ImageButton registerExitButton = findViewById(R.id.counter_exit_button);
        registerExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventCounter.registerExit();
            }
        });

        final LinearLayout incrementInteriorCount = findViewById(R.id.interior_counter_adjust_add);
        incrementInteriorCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventCounter.incrementInteriorCount(1);
            }
        });

        final LinearLayout reduceInteriorCount = findViewById(R.id.interior_counter_adjust_subtract);
        reduceInteriorCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventCounter.incrementInteriorCount(-1);
            }
        });

        final LinearLayout incrementExteriorCount = findViewById(R.id.exterior_counter_adjust_add);
        incrementExteriorCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventCounter.incrementExteriorCount(1);
            }
        });

        final LinearLayout reduceExteriorCount = findViewById(R.id.exterior_counter_adjust_subtract);
        reduceExteriorCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventCounter.incrementExteriorCount(-1);
            }
        });

        incrementInteriorCount.setOnLongClickListener(getInteriorCounterCountOnClickListener(this));
        reduceInteriorCount.setOnLongClickListener(getInteriorCounterCountOnClickListener(this));

        incrementExteriorCount.setOnLongClickListener(getExteriorCounterCountOnClickListener(this));
        reduceExteriorCount.setOnLongClickListener(getExteriorCounterCountOnClickListener(this));

    }

    private View.OnLongClickListener getInteriorCounterCountOnClickListener(final Context context){

        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.InteriorDialog);

                final LayoutInflater layoutInflater = getLayoutInflater();
                final ConstraintLayout inputContainer = (ConstraintLayout) layoutInflater.inflate(R.layout.dialog_content_edit_counter_count,null);
                final EditText counterCountEdit = inputContainer.findViewById(R.id.dialog_edit_counter_count_edit);
                counterCountEdit.setText(String.valueOf(mEventCounter.getInteriorCount()));

                builder.setView(inputContainer);
                builder.setMessage(R.string.dialog_title_interior_counter_count)
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final String editedCountString = counterCountEdit.getEditableText().toString();

                                // Check if user input is NOT a valid integer
                                if(!editedCountString.matches("-?[1-9]\\d*|0")){
                                    // Invalid input, not an integer
                                    return;
                                }

                                try{

                                    int editedCount = Integer.parseInt(editedCountString);

                                    mEventCounter.setInteriorCount(editedCount);


                                } catch (NumberFormatException e) {
                                    // Invalid input
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User canceled the dialog, no actions required
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        };

    }

    private View.OnLongClickListener getExteriorCounterCountOnClickListener(final Context context){

        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.ExteriorDialog);

                final LayoutInflater layoutInflater = getLayoutInflater();
                final ConstraintLayout inputContainer = (ConstraintLayout) layoutInflater.inflate(R.layout.dialog_content_edit_counter_count,null);
                final EditText counterCountEdit = inputContainer.findViewById(R.id.dialog_edit_counter_count_edit);
                counterCountEdit.setText(String.valueOf(mEventCounter.getExteriorCount()));

                builder.setView(inputContainer);
                builder.setMessage(R.string.dialog_title_exterior_counter_count)
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final String editedCountString = counterCountEdit.getEditableText().toString();

                                // Check if user input is NOT a valid integer
                                if(!editedCountString.matches("-?[1-9]\\d*|0")){
                                    // Invalid input, not an integer
                                    return;
                                }

                                try{

                                    int editedCount = Integer.parseInt(editedCountString);

                                    mEventCounter.setExteriorCount(editedCount);


                                } catch (NumberFormatException e) {
                                    // Invalid input
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User canceled the dialog, no actions required
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        };

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        // Enable the use of volume buttons to increment/reduce counter count

        if((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){

            // Increment interior counter by 1 when volume up button is pressed
            mEventCounter.incrementInteriorCount(1);

        }else if((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){

            // Reduce interior counter by 1 when volume down button is pressed
            mEventCounter.incrementInteriorCount(-1);

        }

        return true;
    }

    @Override
    public void onEventCounterChanged() {

        updateCounterViews();

        EventCounterStorage.storeCounterInstance(this, mEventCounter);

    }

    // Get EventCounter counts (interior and exterior) and display it to user
    private void updateCounterViews() {

        // Update interior count text
        final TextView mInteriorCounterText = findViewById(R.id.interior_counter_count);
        mInteriorCounterText.setText(String.valueOf(mEventCounter.getInteriorCount()));

        // Update exterior count text
        final  TextView mExteriorCounterText = findViewById(R.id.exterior_counter_count);
        mExteriorCounterText.setText(String.valueOf(mEventCounter.getExteriorCount()));

    }
}
