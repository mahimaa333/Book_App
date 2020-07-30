package android.example.booklisting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class BooksAdapter extends ArrayAdapter<Books> {

    public BooksAdapter( Activity context, ArrayList<Books> books) {
        super(context,0, books);
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View listIteView = convertView;
        if(listIteView == null){
            listIteView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Books currentBook = getItem(position);

        TextView titleTextView = (TextView) listIteView.findViewById(R.id.title);
        titleTextView.setText(currentBook.getTitle());

        TextView authorTextView = (TextView) listIteView.findViewById(R.id.author );
        authorTextView.setText(currentBook.getAuthor());

        TextView descriptionTextView = (TextView) listIteView.findViewById(R.id.description);
        descriptionTextView.setText(currentBook.getDescription());

        return listIteView;
    }

}
