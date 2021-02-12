package com.example.homeworktracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final int N = 7; // total number of textviews to add

    TextView[] myTextViews = new TextView[N]; // create an empty array;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        CalendarView calendarView=(CalendarView)rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month = month + 1;
                String curDate = String.valueOf(year + "-" + month + "-" + dayOfMonth);
                SQLiteOpenHelper HomeworkTrackerDatabaseHelper = new HomeworkTrackerDatabaseHelper(getActivity());
                try {
                    SQLiteDatabase db = HomeworkTrackerDatabaseHelper.getReadableDatabase();
                    Cursor cursor = db.query ("HOMEWORK",
                            new String[] {"DESCRIPTION", "CLASS_NAME", "TYPE", "DUE_DATE", "DUE_TIME", "PRIORITY", "REMINDER"},
                            null, null,
                            null, null, null);
                    View linearLayout = (View)rootView.findViewById(R.id.info);
                    int i = 0;
                    //Move to the first record in the Cursor
                    while (cursor.moveToNext()) {
                        String due_dateText = cursor.getString(3);
                        if(due_dateText.equals(curDate)){
                            String descriptionText = cursor.getString(0);
                            String class_nameText = cursor.getString(1);
                            String due_timeText = cursor.getString(4);
                            final TextView rowTextView = new TextView(getActivity());
                            int my_tag = View.generateViewId();
                            rowTextView.setId(my_tag);
                            rowTextView.setText(descriptionText + ", " + class_nameText + ", due at: " + due_timeText);
                            ((LinearLayout) linearLayout).addView(rowTextView);
                            myTextViews[i] = rowTextView;
                            i++;
                        }
                        else {
                            ((LinearLayout) linearLayout).removeView(myTextViews[i]);
                            for (int j = 1; j < N; j++){
                                ((LinearLayout) linearLayout).removeView(myTextViews[j]);
                            }
                        }
                    }
                } catch(SQLiteException e) {
                    Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return rootView;
    }
}