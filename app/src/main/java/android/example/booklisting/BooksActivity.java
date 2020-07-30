package android.example.booklisting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Loader;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>>{
    TextView emptyTextView;
    ProgressBar progressBar;
    String query;
    private BooksAdapter mAdapter;
    boolean isConnected;
    private static final String LOG_TAG = ResultActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private static final String book_request_url = "https://www.googleapis.com/books/v1/volumes?q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        query = intent.getStringExtra("search");

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        progressBar.setIndeterminate(true);
        ListView bookListView = (ListView) findViewById(R.id.list);
        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        bookListView.setEmptyView(emptyTextView);

        mAdapter = new BooksAdapter(this,new ArrayList<Books>());
        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books book = mAdapter.getItem(position);
                Uri bookUrl = Uri.parse(book.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW,bookUrl);

                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        if(isConnected){
            loaderManager.initLoader(BOOK_LOADER_ID,null,this);
        }
        else {
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(LOG_TAG, "Loader created");
        return new BookLoader(ResultActivity.this,book_request_url.concat(query).concat("&key=AIzaSyC_HpBlqlA0Ni6v6zAGVucSWL6xch7tFno"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Books>> loader, List<Books> books) {
        progressBar.setVisibility(View.GONE);
        Log.d(LOG_TAG, "Loader finished");
        mAdapter.clear();
        if(books != null && !books.isEmpty()){
            Log.d(LOG_TAG, "books size "+books.size());
            mAdapter.addAll(books);
        }
        emptyTextView.setText(R.string.empty);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Books>> loader) {
        mAdapter.clear();
    }
}
