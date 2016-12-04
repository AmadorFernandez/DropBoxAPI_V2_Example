
package com.amador.androidbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dropbox.core.android.Auth;

/**
 * @author Amador Fernandez Gonzalez
 *         <p>
 *         Clase para el manejo de la activida que lanza la ventana para autentificar el cliente
 *         en Dropbox, asociarlo a la cuenta y obtener el token
 */
public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.startOAuth2Authentication(getApplicationContext(), getString(R.string.app_key));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessToken();
    }

    public void getAccessToken() {
        String accessToken = Auth.getOAuth2Token();
        if (accessToken != null) {
            Preferences.getInstance(FilterActivity.this).setToken(Auth.getOAuth2Token());
            Intent intent = new Intent(FilterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
