package edu.niu.cs.z1726972.speech;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    public static final int CODE_A = 1;
    Button btnSpeak,btnListen;
    EditText etSpeak;
    TextView tvListen;
    TextToSpeech ttsObj;
    int result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etSpeak = (EditText) findViewById(R.id.etSpeak);
        tvListen = (TextView) findViewById(R.id.tvListen);
        btnSpeak = (Button) findViewById(R.id.buttonSpeak);
        btnListen = (Button) findViewById(R.id.buttonListen);
        ttsObj = new TextToSpeech(this,new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    result = ttsObj.setLanguage(Locale.US);
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.message),Toast.LENGTH_LONG).show();
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                    Toast.makeText(getApplicationContext(), getString(R.string.message),Toast.LENGTH_LONG).show();
                else
                    ttsObj.speak(etSpeak.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnListen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //Use Free from speech recognition
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.prompt));
                try{
                    startActivityForResult(i,CODE_A);
                }
                catch(ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),getString(R.string.message),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if((requestCode == CODE_A) && (resultCode == RESULT_OK) && (data != null))
        {
            ArrayList<String> spokenText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            tvListen.setText(spokenText.get(0));
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (ttsObj != null)
        {
            ttsObj.stop();
            ttsObj.shutdown();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
