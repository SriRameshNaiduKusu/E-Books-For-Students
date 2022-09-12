package in.cwt.ebooksforstudents.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.tapadoo.alerter.Alerter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import in.cwt.ebooksforstudents.R;
import in.cwt.ebooksforstudents.utilities.AppUtilities;

public class PdfActivity extends AppCompatActivity implements  OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener{

    private static final String TAG = "PdfActivity";
    PDFView pdfView;
    CircularProgressIndicator progress_circular;
    String book_url,name,cname;
    private RelativeLayout relativeLayout;
    private ImageView gohome;
    private TextView book_title;
    private ImageButton book_download;
    private TextView title,progresstxt,progress_textv;
    private CardView ProgressCard;
    private URL url;
    //private ProgressBar progressBar;
    String formattedDate;
    private int lenghtOfFile = 0;
    File directory;
    //String fname;
    // LinearProgressIndicator linearProgressIndicator;

    private NumberProgressBar bnp;
    private static final long GAME_LENGTH_MILLISECONDS = 8000;
    private CountDownTimer countDownTimer;

    private boolean download_running = false;
   /* @Override
    public void onProgressChange(int current, int max) {
        if (current == max){
            progress_textv.setText("Processing Downloaded Pdf");
        }

    }*/

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
        setContentView(R.layout.activity_pdf);

        //Get the Intent Value
        Intent intent = getIntent();
        if (intent != null) {
            book_url = intent.getStringExtra("book_url");
            name = intent.getStringExtra( "book_name" );
            cname = intent.getStringExtra( "cname" );
        };
       // ProgressCard = findViewById(R.id.ProgressCard);
       // progress_textv = findViewById(R.id.progress_textv);


        book_download = findViewById(R.id.book_download);
        gohome = findViewById( R.id.imBack );
        book_title = findViewById(R.id.TVTitle);
        book_title.setText(name);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Thi Avoid back press while downloading Book
                if (download_running) {
                    Alerter.create(PdfActivity.this)
                            .setText("Downloading is in Progress. Please Wait....")
                            .setIcon(R.drawable.ic_twotone_error_24)
                            .setBackgroundColorRes( R.color.red)
                            .enableSwipeToDismiss()
                            .show();
                }else {
                    PdfActivity.super.onBackPressed();
                }

            }
        });

        book_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code that download Books
                try {
                    url = new URL(book_url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url + ""));
                request.setTitle(name);
                request.setMimeType("application/pdf");
                request.allowScanningByMediaScanner();
                request.setAllowedOverMetered(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "E-book For Students/" + name+".pdf");
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

            }
        });

        //bnp = (NumberProgressBar)findViewById(R.id.number_progress_bar);
       // bnp.setOnProgressBarListener(this);

        pdfView = findViewById(R.id.pdfView);
        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);
        new load_book_fromUrl().execute(book_url);


        //Grab the internal storage directory and create a folder if it doesn't exist
    /*    File folder = new File(AppUtilities.RootDirectoryShow+"/"+cname+"/");


        boolean isDirectoryCreated = folder.exists();

        //See if the file exists
        if (!isDirectoryCreated) {
            isDirectoryCreated= folder.mkdirs();
        }
        if(isDirectoryCreated) {
            directory = new File(folder, name);


            //See if file already exists (reduces wait time)
            boolean empty = directory.length() == 0;
            if (empty) {
                /**Call class to create parameter container
                AsyncParameters param = new AsyncParameters(book_url, directory);
                DownloadAsync Downloader = new DownloadAsync();
                Downloader.execute(param);
            }
            showPdf();
        } */

    }



    @Override
    public void loadComplete(int nbPages) {
        //linearProgressIndicator.setVisibility( View.GONE );
        //ProgressCard.setVisibility(View.GONE);
        //bnp.setVisibility(View.GONE);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }


   /* public void showPdf() {
        pdfView.fromFile(directory)
                .enableSwipe(true)
                .enableAnnotationRendering(false)
                .enableAntialiasing(true)
                .spacing(10)
                .defaultPage(0)
                .onLoad(this)
                .onPageChange( this )
                .enableDoubletap( true )
                .load();
    }*/
   /* public class DownloadAsync extends AsyncTask<AsyncParameters, Integer, Void> {

        // Container for all parameters of DownloadAsync
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            //Create a progress bar that details the program downloading
            super.onPreExecute();

            download_running = true;
            //linearProgressIndicator.setVisibility( View.VISIBLE );
            progress_textv.setText("Starting Download");
            ProgressCard.setVisibility(View.VISIBLE);
            bnp.setVisibility(View.VISIBLE);


        }



        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(AsyncParameters... params) {
            /**grab the container AsyncParameters and the values within it**/
         /*   String fileURL = params[0].URL;
            File directory = params[0].directory;
            int count;
            progress_textv.setText("Downloading");
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
            progress_textv.setText("Download Finished");
            ProgressCard.setVisibility(View.GONE);
            bnp.setVisibility(View.GONE);

            showPdf();

            download_running = false;


        }

    }*/

    //create an async task to load pdf from URL.
    class load_book_fromUrl extends AsyncTask<String, Void, InputStream> implements OnLoadCompleteListener, OnErrorListener, OnPageChangeListener {
        @Override
        protected InputStream doInBackground(String... strings) {
            //We use InputStream to get PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // if response is success. we are getting input stream from url and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                //method to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            //after the executing async task we load pdf in to pdfview.
            pdfView.fromStream(inputStream)
                    .onLoad(this)
                    .onError(this)
                    .enableSwipe(true)
                    .nightMode(true)
                    .enableAnnotationRendering(false)
                    .enableAntialiasing(true)
                    .onPageChange(this)
                    .load();
        }
        @Override
        public void loadComplete(int nbPages) {
            progress_circular.setVisibility(View.GONE);
        }
        @Override
        public void onError(Throwable t) {
            progress_circular.setVisibility(View.GONE);
            Toast.makeText(PdfActivity.this,"Error:" +t.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageChanged(int page, int pageCount) {

        }
    }


}