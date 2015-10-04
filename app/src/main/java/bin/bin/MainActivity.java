package bin.bin;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText enteredBinNumber;
    Button SubmitButton;
    TextView Httpstatus;
    AQuery aq;
    int pointer=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        aq = new AQuery(getApplicationContext());

        enteredBinNumber=(EditText) findViewById(R.id.editText);
        SubmitButton=(Button) findViewById(R.id.button);
        SubmitButton.setOnClickListener(this);

        //Enter BIN

        Httpstatus=(TextView) findViewById(R.id.status);
    }

    @Override
    public void onClick(View view) {
        if(enteredBinNumber.getText().toString().length()>3){

            Search();

        }else{
            Toast.makeText(getApplicationContext(), "Enter more then 3 character", Toast.LENGTH_LONG).show();
        }
    }

    void Search()
    {
        pointer=0;
        Httpstatus.setText("Loading....");
        Map<String, String> params = new HashMap<String, String>();
        params.put("txtSearch", enteredBinNumber.getText().toString());
        params.put("btnSubmit", "Search");

        aq.ajax("http://www.nbr.gov.bd/getbinfield.php", params, String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String json, AjaxStatus status) {



                System.out.println("-------------"+url);
                System.out.println("-------------"+status.getCode());
                System.out.println("-------------"+status.getMessage());
                //System.out.println(status.getHeaders());
               // System.out.println(doc.getElementsByClass("links_text"));

                if(status.getMessage()!="network error"){
                    Document doc = Jsoup.parse(json);
                    Elements links = doc.getElementsByTag("p");

                    for (Element link : links) {

                        if(pointer==4){
                            Httpstatus.setText(Html.fromHtml(link.html()));
                        }
                        pointer=pointer+1;
                    }
                }else{
                    Httpstatus.setText("Please check your net connection");
                }
            }
        });
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
