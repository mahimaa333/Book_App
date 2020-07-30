package android.example.booklisting;

import android.content.Context;
import android.util.Log;
import android.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Books>> {
    private static final String LOG_TAG = BookLoader.class.getName();
    private String mUrl;

    public BookLoader(Context context, String url){
        super(context);
        Log.d(LOG_TAG, "Loader initialized mUrl "+mUrl);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "Loading started");
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {
        Log.d(LOG_TAG, "Loading completed");
        if(mUrl == null){
            return null;
        }
        List<Books> books = QueryUtils.fetchBooksData(mUrl);
        return books;
    }
}
