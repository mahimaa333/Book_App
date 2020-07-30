package android.example.booklisting;

public class Books {
    private String mTitle;
    private String mAuthor;
    private String mUrl;
    private String mDescription;

    public Books(String title, String author, String description,String url){
        mTitle = title;
        mAuthor = author;
        mUrl = url;
        mDescription = description;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getDescription() { return mDescription; }

    public String getUrl(){
        return mUrl;
    }
}
