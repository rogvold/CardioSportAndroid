package com.cardiomood.sport.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cardiomood.sport.android.client.*;
import com.cardiomood.sport.android.client.json.JsonError;
import com.cardiomood.sport.android.client.json.JsonResponse;
import com.cardiomood.sport.android.client.json.JsonTrainee;
import com.cardiomood.sport.android.client.json.ResponseConstants;
import com.cardiomood.sport.android.tools.config.ConfigurationConstants;
import com.cardiomood.sport.android.tools.config.PreferenceHelper;

/**
 * Project: CardioSport
 * User: danon
 * Date: 15.06.13
 * Time: 14:16
 */
public class LoginActivity extends Activity implements ConfigurationConstants {

    /**
     * The default email to populate the email field with.
     */
    public static final String EXTRA_EMAIL = "com.cardiomood.android.extra.EMAIL";

    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;

    private boolean loginInProgress = false;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;
    private PreferenceHelper prefHelper;

    private Toast toast;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper = new PreferenceHelper(getApplicationContext());
        prefHelper.setPersistent(true);

        //loadConfigs();

        if (isLoggedIn()) {
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
        if (TextUtils.isEmpty(mEmail)) {
            mEmail = prefHelper.getString(ConfigurationConstants.USER_EMAIL_KEY);
        }
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText(mEmail);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptLogin();
                    }
                });
//        findViewById(R.id.register_button).setOnClickListener(
//                new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
//                    }
//                });
    }

    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void attemptLogin() {
        if (loginInProgress) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);

            // TODO: it is strongly recommended to send SHA2 hash of the password
            CardioSportServiceHelper service = new CardioSportServiceHelper(CardioSportService.getInstance());
            service.setEmail(mEmail);
            service.setPassword(mPassword);
            service.checkUserAuthorisationData(new CardioSportServiceHelper.Callback<JsonTrainee>() {
                @Override
                public void onResult(JsonTrainee result) {
                    if (result !=null) {
                        performLogIn(result);
                        startMainActivity();
                    } else {
                        mPasswordView.setError("Incorrect email and/or password.");
                        mPasswordView.requestFocus();
                    }
                    showProgress(false);
                    loginInProgress = false;
                }

                @Override
                public void onError(JsonError error) {
                    mPasswordView.setError(error == null ? "Unexpected error." : error.getMessage());
                    mPasswordView.requestFocus();
                    showProgress(false);
                    loginInProgress = false;
                }
            });
            loginInProgress = true;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    protected void performLogIn(JsonTrainee t) {
        prefHelper.putBoolean(USER_LOGGED_IN, true);
        prefHelper.putString(USER_EMAIL_KEY, mEmail);
        prefHelper.putString(USER_FIRST_NAME_KEY, t.getFirstName());
        prefHelper.putString(USER_LAST_NAME_KEY, t.getLastName());
        prefHelper.putString(USER_PHONE_NUMBER_KEY, t.getPhone());
        prefHelper.putString(USER_PASSWORD_KEY, mPassword);
        prefHelper.putLong(USER_EXTERNAL_ID, t.getId());
    }

    public boolean isLoggedIn() {
        return prefHelper.getBoolean(ConfigurationConstants.USER_LOGGED_IN);
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, getString(R.string.press_back_to_close_app), Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        }
        else
        {
            if (toast != null)
            {
                toast.cancel();
            }
            loginInProgress = false;
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_service_settings:
                startActivity(new Intent(this, ServiceSettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
