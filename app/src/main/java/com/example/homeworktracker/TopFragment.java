package com.example.homeworktracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;
    private Cursor cursor1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_top, container, false);
        setHasOptionsMenu(true);
        ListView listClasses = (ListView) rootView.findViewById(R.id.list_all_classes);
        ListView listHomework = (ListView) rootView.findViewById(R.id.list_all_homework);
        SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(getActivity());
        try {
            db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
            cursor = db.query("CLASSES",
                    new String[]{"_id", "NAME"},
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            listClasses.setAdapter(listAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Create a listener to listen for clicks in the list view
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> listClasses,
                                            View itemView,
                                            int position,
                                            long id) {
                        //Pass the class the user clicks on to ClassActivity
                        Intent intent = new Intent(getActivity(), ClassActivity.class);
                        intent.putExtra(ClassActivity.EXTRA_CLASSID, (int) id);
                        startActivity(intent);
                    }
                };
        //Assign the listener to the list view
        listClasses.setOnItemClickListener(itemClickListener);

        try {
            db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
            cursor1 = db.query("HOMEWORK",
                    new String[]{"_id", "DESCRIPTION"},
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter1 = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1,
                    cursor1,
                    new String[]{"DESCRIPTION"},
                    new int[]{android.R.id.text1},
                    0);
            listHomework.setAdapter(listAdapter1);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Create a listener to listen for clicks in the list view
        AdapterView.OnItemClickListener itemClickListener1 =
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> listHomework,
                                            View itemView,
                                            int position,
                                            long id) {
                        //Pass the class the user clicks on to HomeworkActivity
                        Intent intent = new Intent(getActivity(), HomeworkActivity.class);
                        intent.putExtra(HomeworkActivity.EXTRA_HOMEWORKID, (int) id);
                        startActivity(intent);
                    }
                };
        //Assign the listener to the list view
        listHomework.setOnItemClickListener(itemClickListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor newCursor = db.query("CLASSES",
                new String[]{"_id", "NAME"},
                null, null, null, null, null);
        ListView listClasses = (ListView) getView().findViewById(R.id.list_all_classes);
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listClasses.getAdapter();
        adapter.changeCursor(newCursor);
        cursor = newCursor;

        Cursor newCursor1 = db.query("HOMEWORK",
                new String[]{"_id", "DESCRIPTION"},
                null, null, null, null, null);
        ListView listHomework = (ListView) getView().findViewById(R.id.list_all_homework);
        SimpleCursorAdapter adapter1 = (SimpleCursorAdapter) listHomework.getAdapter();
        adapter1.changeCursor(newCursor1);
        cursor1 = newCursor1;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }
}