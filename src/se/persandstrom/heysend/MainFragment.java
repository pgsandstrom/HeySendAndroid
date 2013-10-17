package se.persandstrom.heysend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment implements View.OnClickListener{

	private GcmHandler gcmHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gcmHandler = ((ItemListActivity)getActivity()).getGcmHandler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.main, container, false);

		rootView.findViewById(R.id.register).setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.register:
			gcmHandler.register();
		}
	}
}
