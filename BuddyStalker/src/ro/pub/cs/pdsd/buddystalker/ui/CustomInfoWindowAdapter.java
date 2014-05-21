package ro.pub.cs.pdsd.buddystalker.ui;

import java.util.regex.Pattern;

import ro.pub.cs.pdsd.buddystalker.R;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
	public static final String SNIPPET_DELIMITER = "!__!";

	private final View mInfoWindowView;

	public CustomInfoWindowAdapter(Activity activity) {
		mInfoWindowView = activity.getLayoutInflater().inflate(R.layout.info_window_view, null);
	}

	@Override
	public View getInfoContents(Marker marker) {
		TextView titleTv = (TextView) mInfoWindowView.findViewById(R.id.title_tv);
		titleTv.setText(marker.getTitle());

		String snippet = marker.getSnippet();
		String[] tokens = snippet.split(Pattern.quote(SNIPPET_DELIMITER));

		TextView statusTv = (TextView) mInfoWindowView.findViewById(R.id.status_tv);
		statusTv.setText(tokens[0]);

		TextView seenAtTv = (TextView) mInfoWindowView.findViewById(R.id.seen_at_tv);
		seenAtTv.setText(tokens[1]);

		return mInfoWindowView;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// getInfoContents is called first
		return null;
	}
}
