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

import java.util.ArrayList;

public class ActivityList extends AppCompatActivity {

    Button back;
    Button showAllSongsBtn;
    Spinner yearSpinner;
    ListView lv;
    ArrayAdapter<Song> aa;
    ArrayList<Song> songList;
    ArrayList<Song> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        back = findViewById(R.id.btnBack);
        showAllSongsBtn = findViewById(R.id.btnShow);
        yearSpinner = findViewById(R.id.spinner1);
        lv = findViewById(R.id.lv);

        DBHelper db = new DBHelper(ActivityList.this);
        songList = db.getSongContent();
        db.close();

        aa = new ArrayAdapter<>(ActivityList.this, android.R.layout.simple_list_item_1, songList);
        lv.setAdapter(aa);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityList.this, MainActivity.class);
                startActivity(intent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song clickedSong = songList.get(position);
                Intent intent = new Intent(ActivityList.this, Results.class);
                intent.putExtra("song", clickedSong);
                startActivity(intent);
            }
        });

        showAllSongsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSongsByRating(5);
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = yearSpinner.getSelectedItem().toString();
                if (selectedYear.equals("All")) {
                    aa.clear();
                    aa.addAll(songList);
                    aa.notifyDataSetChanged();
                } else {
                    filterSongsByYear(Integer.parseInt(selectedYear));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void filterSongsByRating(int rating) {
        filteredList = new ArrayList<>();
        for (Song song : songList) {
            if (Integer.parseInt(song.getStars()) == rating) {
                filteredList.add(song);
            }
        }
        aa.clear();
        aa.addAll(filteredList);
        aa.notifyDataSetChanged();
    }

    private void filterSongsByYear(int year) {
        filteredList = new ArrayList<>();
        for (Song song : songList) {
            if (song.getYear() == year) {
                filteredList.add(song);
            }
        }
        aa.clear();
        aa.addAll(filteredList);
        aa.notifyDataSetChanged();
    }
}
