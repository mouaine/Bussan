package net.kristian.TrondheimBussesLive;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FindBusStopByNameActivity extends ListActivity {
	
	private EditText filterText = null;
	BusStopAdapter adapter = null;
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
	    public void afterTextChanged(Editable s) {}

	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	        adapter.getFilter().filter(s);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.list_filter_by_name);
				
		filterText = (EditText) findViewById(R.id.search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		
		List<BusStop> busStops = new BusStopRepository().getAll();
		adapter = new BusStopAdapter(getBaseContext(), R.id.busstop_list, R.id.busstop_name, busStops);
		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BusStop busStop = (BusStop) parent.getAdapter().getItem(position);
				Intent intent = new Intent(view.getContext(), RealTimeActivity.class);
				intent.putExtra("code", busStop.getCode());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    filterText.removeTextChangedListener(filterTextWatcher);
	}
}