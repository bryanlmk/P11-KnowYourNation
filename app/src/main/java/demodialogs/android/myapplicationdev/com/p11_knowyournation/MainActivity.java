    package demodialogs.android.myapplicationdev.com.p11_knowyournation;

    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.net.Uri;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.widget.ArrayAdapter;
    import android.widget.EditText;
    import android.widget.LinearLayout;
    import android.widget.ListView;
    import android.widget.Toast;

    import java.util.ArrayList;

    import static android.R.id.message;

    public class MainActivity extends AppCompatActivity {
        ArrayList<String> alNational;
        ArrayAdapter<String> aaNational;
        ListView lv;
        String code = "738964";
        String userCode;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            lv = (ListView) findViewById(R.id.lv);
            alNational = new ArrayList<String>();
            aaNational = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alNational);
            lv.setAdapter(aaNational);
            alNational.add("Singapore National Day is on 9 August");
            alNational.add("Singapore is 52 years old");
            alNational.add("Theme is '#OneNationTogether'");

            SharedPreferences prefs = getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE);
            final SharedPreferences.Editor prefEdit = prefs.edit();

            userCode = prefs.getString("userCode", "");
            if (userCode.equals(code)) {
                Toast.makeText(MainActivity.this, "Welcome Back", Toast.LENGTH_LONG).show();
            } else {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout passPhrase =
                        (LinearLayout) inflater.inflate(R.layout.passphrase, null);
                final EditText etPassphrase = (EditText) passPhrase
                        .findViewById(R.id.editTextPassPhrase);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Please Login");
                builder.setView(passPhrase);
                builder.setCancelable(false);
                builder.setPositiveButton("Ok ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (etPassphrase.getText().toString().equals(code)) {
                            prefEdit.putString("userCode", etPassphrase.getText().toString());
                            prefEdit.commit();
                            Toast.makeText(MainActivity.this, "Correct access code", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect access code",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
                builder.setNegativeButton("NO ACCESS CODE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "You clicked no",
                                Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Tally against the respective action item clicked
            //  and implement the appropriate action
            if (item.getItemId() == R.id.exit) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Quit?")
                        // Set text for the positive button and the corresponding
                        //  OnClickListener when it is clicked
                        .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences prefs = getApplicationContext().getSharedPreferences("pref",MODE_PRIVATE);
                                SharedPreferences.Editor prefEdit = prefs.edit();
                                prefEdit.clear();
                                prefEdit.commit();
                                finish();
                                Toast.makeText(MainActivity.this,"You chose to quit the application",Toast.LENGTH_LONG).show();
                            }
                        })
                        // Set text for the negative button and the corresponding
                        //  OnClickListener when it is clicked
                        .setNegativeButton("NOT REALLY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {Toast.makeText(MainActivity.this, "Go back to the application",
                                    Toast.LENGTH_LONG).show();
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else if (item.getItemId() == R.id.send) {
                String [] list = new String[] { "Email" ,"SMS"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select a way to enrich your friend")
                        // Set the list of items easily by just supplying an
                        //  array of the items
                        .setItems(list, new DialogInterface.OnClickListener() {
                            // The parameter "which" is the item index
                            // clicked, starting from 0
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent email = new Intent(Intent.ACTION_SEND);
                                    // Put essentials like email address, subject & body text
                                    email.putExtra(Intent.EXTRA_EMAIL,
                                            new String[]{"jason_lim@rp.edu.sg"});
                                    email.putExtra(Intent.EXTRA_SUBJECT,
                                            "Test Email from C347");
                                    email.putExtra(Intent.EXTRA_TEXT,
                                            alNational.toString());
                                    // This MIME type indicates email
                                    email.setType("message/rfc822");
                                    // createChooser shows user a list of app that can handle
                                    // this MIME type, which is, email
                                    startActivity(Intent.createChooser(email,
                                            "Choose an Email client :"));

                                } else{
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("sms:"));
                                    intent.putExtra(Intent.EXTRA_TEXT,alNational.toString());
                                    startActivity(intent);
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.options, menu);
            return true;
        }



    }

