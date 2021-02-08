package com.example.homeworktracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import android.util.Log;

import com.example.homeworktracker.model.Homework;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeworkFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;
    private Cursor cursor1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeworkFragment newInstance(String param1, String param2) {
        HomeworkFragment fragment = new HomeworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_homework, container, false);
        setHasOptionsMenu(true);
        ListView listHomework = (ListView) rootView.findViewById(R.id.list_homework_upcoming);
        ListView listHomeworkCompleted = (ListView) rootView.findViewById(R.id.list_homework_completed);
        SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(getActivity());
        try {
            db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
            cursor = db.query("HOMEWORK",
                    new String[] {"_id","DESCRIPTION", "CLASS_NAME", "TYPE", "DUE_DATE", "DUE_TIME", "PRIORITY", "REMINDER"},
                    null,
                    null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"DESCRIPTION"},
                    new int[]{android.R.id.text1},
                    0);
            listHomework.setAdapter(listAdapter);
            if (cursor.moveToFirst()) {
                String due_dateText = cursor.getString(4);
                String due_timeText = cursor.getString(5);
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d HH:mm");
                Date due_date = sdf.parse(due_dateText + " " + due_timeText);
                Date current_date = sdf.parse(formatter.format(currentDate));
                View view = inflater.inflate(android.R.layout.simple_list_item_1, container, false);
                View row = listHomework.getAdapter().getView(cursor.getPosition(), view, container);
                if (current_date.compareTo(due_date) > 0) {
                    row.setBackgroundColor(getResources().getColor(R.color.colorForLate));
                    row.setBackgroundColor(Color.RED);
                    Log.d("myTag", "Deadline has passed!");
                }
            }
            listAdapter.notifyDataSetChanged();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        } catch(ParseException e) {
            Toast toast = Toast.makeText(getActivity(), "Parse Error!", Toast.LENGTH_SHORT);
            toast.show();
        }
        try {
            db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
            cursor1 = db.query("HOMEWORK_COMPLETED",
                    new String[]{"_id", "DESCRIPTION"},
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter1 = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_1,
                    cursor1,
                    new String[]{"DESCRIPTION"},
                    new int[]{android.R.id.text1},
                    0);
            listHomeworkCompleted.setAdapter(listAdapter1);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Create a listener to listen for clicks in the list view
        AdapterView.OnItemClickListener itemClickListener =
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

        //Create a listener to listen for clicks in the list view
        AdapterView.OnItemClickListener itemClickListener1 =
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> listHomeworkCompleted,
                                            View itemView,
                                            int position,
                                            long id) {
                        //Pass the class the user clicks on to CompletedActivity
                        Intent intent = new Intent(getActivity(), CompletedActivity.class);
                        intent.putExtra(CompletedActivity.EXTRA_COMPLETED_HOMEWORKID, (int) id);
                        startActivity(intent);
                    }
                };
        //Assign the listener to the list view
        listHomeworkCompleted.setOnItemClickListener(itemClickListener1);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor newCursor = db.query("HOMEWORK",
                new String[]{"_id", "DESCRIPTION", "CLASS_NAME", "TYPE", "DUE_DATE", "DUE_TIME", "PRIORITY", "REMINDER"},
                null, null, null, null, null);
        ListView listHomework = (ListView) getView().findViewById(R.id.list_homework_upcoming);
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) listHomework.getAdapter();
        adapter.changeCursor(newCursor);
        cursor = newCursor;
        Cursor newCursor1 = db.query("HOMEWORK_COMPLETED",
                new String[]{"_id", "DESCRIPTION"},
                null, null, null, null, null);
        ListView listHomeworkCompleted = (ListView) getView().findViewById(R.id.list_homework_completed);
        SimpleCursorAdapter adapter1 = (SimpleCursorAdapter) listHomeworkCompleted.getAdapter();
        adapter1.changeCursor(newCursor1);
        cursor1 = newCursor1;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_order:
                Intent intent = new Intent(this.getActivity(), OrderActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}