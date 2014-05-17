package ro.pub.cs.pdsd.buddystalker.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.R;
import ro.pub.cs.pdsd.buddystalker.http.client.UserClient;
import ro.pub.cs.pdsd.buddystalker.model.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUserActivity extends Activity {
	private EditText usernameEt;
	private EditText passwordEt;
	private EditText firstNameEt;
	private EditText lastNameEt;

	private Button registerBtn;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);

		initViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initViews() {
		usernameEt = (EditText) findViewById(R.id.username_et);
		passwordEt = (EditText) findViewById(R.id.password_et);
		firstNameEt = (EditText) findViewById(R.id.first_name_et);
		lastNameEt = (EditText) findViewById(R.id.last_name_et);

		registerBtn = (Button) findViewById(R.id.register_btn);
		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean incompleteProfile = false;
				boolean anotherFieldRequestedFocus = false;

				usernameEt.setError(null);
				passwordEt.setError(null);
				firstNameEt.setError(null);
				lastNameEt.setError(null);

				if (TextUtils.isEmpty(usernameEt.getText().toString())) {
					incompleteProfile = true;
					usernameEt.setError(getString(R.string.error_field_required));
					usernameEt.requestFocus();
					anotherFieldRequestedFocus = true;
				}
				if (TextUtils.isEmpty(passwordEt.getText().toString())) {
					incompleteProfile = true;
					passwordEt.setError(getString(R.string.error_field_required));
					if (!anotherFieldRequestedFocus) {
						passwordEt.requestFocus();
						anotherFieldRequestedFocus = true;
					}
				}
				if (TextUtils.isEmpty(firstNameEt.getText().toString())) {
					incompleteProfile = true;
					firstNameEt.setError(getString(R.string.error_field_required));
					if (!anotherFieldRequestedFocus) {
						firstNameEt.requestFocus();
						anotherFieldRequestedFocus = true;
					}
				}
				if (TextUtils.isEmpty(lastNameEt.getText().toString())) {
					incompleteProfile = true;
					lastNameEt.setError(getString(R.string.error_field_required));
					if (!anotherFieldRequestedFocus) {
						lastNameEt.requestFocus();
						anotherFieldRequestedFocus = true;
					}
				}

				if (incompleteProfile) {
					return;
				}

				progressDialog = ProgressDialog.show(RegisterUserActivity.this, null,
						"Creating your account, please wait...");
				new RegisterUserTask().execute();
			}
		});
	}

	private User buildUser() {
		User user = new User();
		user.setUsername(usernameEt.getText().toString());
		user.setPassword(passwordEt.getText().toString());
		user.setFirstName(firstNameEt.getText().toString());
		user.setLastName(lastNameEt.getText().toString());
		user.setStatus(getString(R.string.initial_status));

		return user;
	}

	private class RegisterUserTask extends AsyncTask<Void, Void, Boolean> {
		private String failureMessage;

		@Override
		protected Boolean doInBackground(Void... params) {
			UserClient userClient = UserClient.getInstance();
			boolean successful;

			try {
				successful = userClient.createUser(buildUser());

				if (!successful) {
					// this means there's another user with the same user name
					failureMessage = getString(R.string.create_failed_duplicate_username);
				}
			} catch (ClientProtocolException e) {
				failureMessage = getString(R.string.create_failed_connection_problems);
				return false;
			} catch (UnsupportedEncodingException e) {
				failureMessage = getString(R.string.create_failed_connection_problems);
				return false;
			} catch (IOException e) {
				failureMessage = getString(R.string.create_failed_connection_problems);
				return false;
			} finally {
				userClient.close();
			}

			return successful;
		}

		@Override
		protected void onPostExecute(Boolean successful) {
			progressDialog.dismiss();

			if (successful) {
				goToRegisterUserActivity();
			} else {
				Toast.makeText(RegisterUserActivity.this, failureMessage, Toast.LENGTH_LONG).show();
			}
		}

		private void goToRegisterUserActivity() {
			Intent intent = new Intent(RegisterUserActivity.this, MapActivity.class);
			startActivity(intent);
		}
	}
}