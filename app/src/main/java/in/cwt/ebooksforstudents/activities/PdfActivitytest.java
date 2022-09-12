package in.cwt.ebooksforstudents.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.tapadoo.alerter.Alerter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.cwt.ebooksforstudents.R;
import in.cwt.ebooksforstudents.utilities.AppUtilities;

public class PdfActivitytest extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener {

    private static final String TAG = "PdfActivity";
    PDFView pdfView_test;
    CircularProgressIndicator progress_circular;
    String book_url,name,cname;
    private RelativeLayout relativeLayout;
    private ImageView gohome_test;
    private TextView book_title_test;
    private ImageButton book_download_test;
    private TextView title_test,progresstxt_test,progress_textv_test;
    private CardView ProgressCard;
    private URL url_test;
    //private ProgressBar progressBar_test;
    String formattedDate;
    private int lenghtOfFile = 0;
    File directory;
    //String fname;
    // LinearProgressIndicator linearProgressIndicator;

    private NumberProgressBar bnp;
    private static final long GAME_LENGTH_MILLISECONDS = 8000;
    private CountDownTimer countDownTimer;

    private boolean download_running = false;

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    private static class AsyncParameters {
        String URL;
        File directory;
        AsyncParameters(String URL, File directory) {
            this.URL = URL;
            this.directory = directory;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_activitytest);


        Intent intent = getIntent();
        if (intent != null) {
            book_url = intent.getStringExtra("book_url");
            name = intent.getStringExtra( "book_name" );
            cname = intent.getStringExtra( "cname" );
        }

        book_download_test = findViewById(R.id.book_download_test);
        gohome_test = findViewById( R.id.imBack_test );
        book_title_test = findViewById(R.id.TVTitle_test);
        book_title_test.setText(name);
        gohome_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Thi Avoid back press while downloading Book
                if (download_running) {
                    Alerter.create(PdfActivitytest.this)
                            .setText("Downloading is in Progress. Please Wait....")
                            .setIcon(R.drawable.ic_twotone_error_24)
                            .setBackgroundColorRes( R.color.red)
                            .enableSwipeToDismiss()
                            .show();
                }else {
                    PdfActivitytest.super.onBackPressed();
                }

            }
        });

        pdfView_test = findViewById(R.id.pdfViewTest);
        progress_circular = findViewById(R.id.progress_circular_test);
        progress_circular.setVisibility(View.VISIBLE);


        //Grab the internal storage directory and create a folder if it doesn't exist
        File folder = new File(AppUtilities.RootDirectoryShow+"/" );


        boolean isDirectoryCreated = folder.exists();

        //See if the file exists
        if (!isDirectoryCreated) {
            isDirectoryCreated= folder.mkdirs();
            Toast.makeText(PdfActivitytest.this, "Creating", Toast.LENGTH_SHORT).show();
        }
        if(isDirectoryCreated) {
            directory = new File(folder, name);

            Toast.makeText(PdfActivitytest.this, "Created", Toast.LENGTH_SHORT).show();
            //See if file already exists (reduces wait time)
            boolean empty = directory.length() == 0;
            if (empty) {
                /**Call class to create parameter container **/
                AsyncParameters param = new AsyncParameters(book_url, directory);
                DownloadAsync Downloader = new DownloadAsync();
                Downloader.execute(param);
            }
            showPdf();
        }


    }
    public void showPdf() {
        pdfView_test.fromFile(directory)
                .enableSwipe(true)
                .enableAnnotationRendering(false)
                .enableAntialiasing(true)
                .spacing(10)
                .defaultPage(0)
                .onLoad(this)
                .onPageChange( this )
                .enableDoubletap( true )
                .load();
    }
    public class DownloadAsync extends AsyncTask<AsyncParameters, Integer, Void> {

        // Container for all parameters of DownloadAsync
        //ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            //Create a progress bar that details the program downloading
            super.onPreExecute();

            download_running = true;
            Toast.makeText(PdfActivitytest.this, "Pre", Toast.LENGTH_SHORT).show();
            //linearProgressIndicator.setVisibility( View.VISIBLE );
            //progress_textv.setText("Starting Download");
           // ProgressCard.setVisibility(View.VISIBLE);
           // bnp.setVisibility(View.VISIBLE);


        }



        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(AsyncParameters... params) {
            /**grab the container AsyncParameters and the values within it**/
           String fileURL = params[0].URL;
            File directory = params[0].directory;
            int count;
            //progress_textv.setText("Downloading");
            try {
                download_running = true;
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OutputStream f = new FileOutputStream(directory);
                java.net.URL u = new URL( fileURL );
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestProperty("Accept-Encoding", "identity");
                c.connect();
                lenghtOfFile = c.getContentLength();
                InputStream in = new BufferedInputStream(u.openStream());


                byte[] buffer = new byte[1024];
                long total = 0;
                int len1 = 0;
                while ((count = in.read(buffer)) > 0) {
                    total += count;

                    //publishProgress((int) ((total * 100) / lenghtOfFile));
                    publishProgress((int) ((total / (float) lenghtOfFile) * 100));
                    f.write(buffer, 0, count);
                }

                f.flush();
                f.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;

        }



        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // progress_value = progress[0];
            Toast.makeText(PdfActivitytest.this, "Post", Toast.LENGTH_SHORT).show();

            //if (Integer.parseInt(progress[0]) <= 100)
            //double progresss =  (((float) progress[0] / Integer.parseInt(booksize)) * 100);

            //linearProgressIndicator.setProgress(progress[0]);
            //linearProgressIndicator.setProgressCompat(Integer.parseInt(progress_value),true);

            //percent_txt.setText(String.valueOf((progress[0] / Integer.parseInt(booksize)) * 100));

            bnp.incrementProgressBy(1);
            bnp.setProgress(progress[0]);


        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //linearProgressIndicator.setVisibility( View.GONE );
            //progress_textv.setText("Download Finished");
            ProgressCard.setVisibility(View.GONE);
            bnp.setVisibility(View.GONE);

            showPdf();

            download_running = false;


        }

    }
}