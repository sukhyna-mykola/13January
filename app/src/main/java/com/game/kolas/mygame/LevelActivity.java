package com.game.kolas.mygame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.game.kolas.mygame.objects.Level;

import java.util.List;

import static com.game.kolas.mygame.DataGame.levels;
import static com.game.kolas.mygame.GameActivity.LEVEL;


public class LevelActivity extends Activity {
    private RecyclerView horizontal_recycler_view;
    private HorizontalAdapter horizontalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        horizontal_recycler_view = (RecyclerView) findViewById(R.id.level_list);
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

            holder.bestTimeOnLevel.setText("Кращий час - " + String.valueOf(horizontalList.get(position).getBestTime()));
            holder.descriptionLevel.setText(horizontalList.get(position).getDescription());
            holder.imageLevel.setImageResource(horizontalList.get(position).getImage());

            holder.layoutLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (levels.get(position).isOpen()) {
                        Intent intent = new Intent(LevelActivity.this, GameActivity.class);
                        intent.putExtra(LEVEL, position);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(LevelActivity.this, "Рівень закритий", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }


}
