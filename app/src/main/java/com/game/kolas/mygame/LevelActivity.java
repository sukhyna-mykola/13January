package com.game.kolas.mygame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.kolas.mygame.level.Level;

import java.util.List;

import static com.game.kolas.mygame.DBHelper.BEST_TIME_KEY;
import static com.game.kolas.mygame.DBHelper.ID_KEY;
import static com.game.kolas.mygame.DBHelper.LEVEL_TABLE;
import static com.game.kolas.mygame.DBHelper.OPEN_KEY;
import static com.game.kolas.mygame.DataGame.levels;

public class LevelActivity extends Activity {
    private RecyclerView horizontal_recycler_view;
    private HorizontalAdapter horizontalAdapter;
    private DBHelper dbHelper;

    public static int LEVEL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        dbHelper = new DBHelper(this);
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.level_list);
        readFromBD();
        horizontalAdapter = new HorizontalAdapter(levels);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(LevelActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view.setAdapter(horizontalAdapter);


    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private List<Level> horizontalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView bestTimeOnLevel;
            public TextView descriptionLevel;
            public ImageView imageLevel;
            public CardView blockedLevel;
            public RelativeLayout layoutLevel;

            public MyViewHolder(View view) {
                super(view);

                bestTimeOnLevel = (TextView) view.findViewById(R.id.best_time_on_level);
                descriptionLevel = (TextView) view.findViewById(R.id.desc_level);
                imageLevel = (ImageView) view.findViewById(R.id.image_level);
                blockedLevel = (CardView) view.findViewById(R.id.blocked_level);
                layoutLevel = (RelativeLayout) view.findViewById(R.id.layout_level);

            }
        }


        public HorizontalAdapter(List<Level> horizontalList) {
            this.horizontalList = horizontalList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_levels, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            if (!horizontalList.get(position).isOpen())
                holder.blockedLevel.setVisibility(View.VISIBLE);

            holder.bestTimeOnLevel.setText(String.valueOf(horizontalList.get(position).getBestTime()));
            holder.descriptionLevel.setText(horizontalList.get(position).getDescription());
            holder.imageLevel.setImageResource(horizontalList.get(position).getImage());

            holder.layoutLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LEVEL = position;
                    Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }


    void readFromBD() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(LEVEL_TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex(ID_KEY);
            int openColIndex = c.getColumnIndex(OPEN_KEY);
            int bestColIndex = c.getColumnIndex(BEST_TIME_KEY);

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                int id = c.getInt(idColIndex);

                levels.get(id).setId(id);
                levels.get(id).setBestTime(c.getString(bestColIndex));
                levels.get(id).setOpen(c.getInt(openColIndex) == 0 ? true : false);

            } while (c.moveToNext());
        }
        c.close();
    }


}
