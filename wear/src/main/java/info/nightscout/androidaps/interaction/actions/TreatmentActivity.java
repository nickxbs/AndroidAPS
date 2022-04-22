package info.nightscout.androidaps.interaction.actions;

import android.os.Bundle;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.DecimalFormat;

import info.nightscout.androidaps.R;
import info.nightscout.androidaps.events.EventWearToMobile;
import info.nightscout.androidaps.interaction.utils.PlusMinusEditText;
import info.nightscout.shared.SafeParse;
import info.nightscout.shared.weardata.EventData;

/**
 * Created by adrian on 09/02/17.
 */

public class TreatmentActivity extends ViewSelectorActivity {

    PlusMinusEditText editCarbs;
    PlusMinusEditText editInsulin;
    int maxCarbs;
    double maxBolus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAdapter(new MyGridViewPagerAdapter());
        maxCarbs = sp.getInt(getString(R.string.key_treatmentssafety_maxcarbs), 48);
        maxBolus = sp.getDouble(getString(R.string.key_treatmentssafety_maxbolus), 3.0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    @SuppressWarnings("deprecation")
    private class MyGridViewPagerAdapter extends GridPagerAdapter {
        @Override
        public int getColumnCount(int arg0) {
            return 3;
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int row, int col) {

            if (col == 0) {
                final View view = getInflatedPlusMinusView(container);
                double def = 0;
                if (editInsulin != null) {
                    def = SafeParse.stringToDouble(editInsulin.editText.getText().toString());
                }
                editInsulin = new PlusMinusEditText(view, R.id.amountfield, R.id.plusbutton, R.id.minusbutton, def, 0d, (double) maxBolus, 0.1d, new DecimalFormat("#0.0"), false);
                setLabelToPlusMinusView(view, getString(R.string.action_insulin));
                container.addView(view);
                view.requestFocus();
                return view;
            } else if (col == 1) {
                final View view = getInflatedPlusMinusView(container);
                double def = 0;
                if (editCarbs != null) {
                    def = SafeParse.stringToDouble(editCarbs.editText.getText().toString());
                }
                editCarbs = new PlusMinusEditText(view, R.id.amountfield, R.id.plusbutton, R.id.minusbutton, def, 0d, (double) maxCarbs, 1d, new DecimalFormat("0"), false);
                setLabelToPlusMinusView(view, getString(R.string.action_carbs));
                container.addView(view);
                return view;
            } else {

                final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.action_send_item, container, false);
                final ImageView confirmButton = view.findViewById(R.id.confirmbutton);
                confirmButton.setOnClickListener((View v) -> {
                    //check if it can happen that the fragment is never created that hold data?
                    // (you have to swipe past them anyways - but still)
                    EventData.ActionBolusPreCheck bolus = new EventData.ActionBolusPreCheck(SafeParse.stringToDouble(editInsulin.editText.getText().toString()), SafeParse.stringToInt(editCarbs.editText.getText().toString()));
                    rxBus.send(new EventWearToMobile(bolus));
                    showToast(TreatmentActivity.this, R.string.action_treatment_confirmation);
                    finishAffinity();
                });
                container.addView(view);
                return view;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int row, int col, Object view) {
            // Handle this to get the data before the view is destroyed?
            // Object should still be kept by this, just setup for re-init?
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
