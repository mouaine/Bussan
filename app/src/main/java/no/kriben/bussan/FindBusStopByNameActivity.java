package no.kriben.bussan;

import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import no.kriben.busstopstrondheim.model.BusStop;

public class FindBusStopByNameActivity extends BusStopListActivity {

    private EditText filterText_ = null;

    private BusStopAdapter adapter_ = null;

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {}

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            synchronized(adapter_) {
                if (adapter_ != null) {
                    adapter_.getFilter().filter(s);
                    adapter_.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_stop_by_name_list);
        registerOnClickListener();

        adapter_ = new BusStopAdapter(getBaseContext(), R.layout.bus_stop_list_item, R.id.busstop_name);
        setListAdapter(adapter_);

        filterText_ = (EditText) findViewById(R.id.search_box);
        filterText_.addTextChangedListener(filterTextWatcher);

        startDownloadBusStopTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterText_.removeTextChangedListener(filterTextWatcher);
    }


    @Override
    protected void refreshBusStopListView() {}

    @Override
    protected void refreshBusStopListView(List<BusStop> busStops) {
        synchronized(adapter_) {
            if (busStops != null) {
                adapter_.setNotifyOnChange(false);
                adapter_.clear();
                for (BusStop b : busStops) {
                    adapter_.add(b);
                }
                adapter_.notifyDataSetChanged();
            }
        }
    }
}