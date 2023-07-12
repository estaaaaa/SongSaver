package sg.edu.rp.c346.id22017139.songsaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class Results extends AppCompatActivity {

    ListView lv;
    Button btnBack;
    ToggleButton btnShowSong;
    Spinner spinner1;
    ArrayList<Song> songList;
    ArrayList<String> yearList;
    ArrayAdapter<Song> aaSongs;
    ArrayAdapter<String> aaSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        lv = findViewById(R.id.lv);
        btnBack = findViewById(R.id.btnBack);
        btnShowSong = findViewById(R.id.btnShow);
        spinner1 = findViewById(R.id.spinner1);
        songList = new ArrayList<>();
        yearList = new ArrayList<>();

        DBHelper db = new DBHelper(getApplicationContext());
        songList = db.getSongContent();
        db.close();

        aaSongs = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songList);
        lv.setAdapter(aaSongs);

        for (Song song : songList) {
            String year = Integer.toString(song.getYear());
            if (!yearList.contains(year)) {
                yearList.add(year);
            }
        }

        aaSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearList);
        spinner1.setAdapter(aaSpinner);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Results.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnShowSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = btnShowSong.isChecked();
                if (isChecked) {
                    ArrayList<Song> filteredSongs = db.getFilteredSongs();
                    aaSongs.clear();
                    aaSongs.addAll(filteredSongs);
                } else {
                    aaSongs.clear();
                    aaSongs.addAll(songList);
                }
                aaSongs.notifyDataSetChanged();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song selectedSong = songList.get(position);
                Intent intent = new Intent(Results.this, ActivityList.class);
                intent.putExtra("song", selectedSong);
                startActivity(intent);
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                if (selectedYear.equals("All")) {
                    aaSongs.clear();
                    aaSongs.addAll(songList);
                } else {
                    int year = Integer.parseInt(selectedYear);
                    ArrayList<Song> songsByYear = db.getSongsByYear(year);
                    aaSongs.clear();
                    aaSongs.addAll(songsByYear);
                }
                aaSongs.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}
