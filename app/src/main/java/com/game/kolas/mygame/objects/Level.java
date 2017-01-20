package com.game.kolas.mygame.objects;

/**
 * Created by mykola on 25.12.16.
 */

public class Level {
    private int id;

    public Level(int id, boolean open, String description, int image, String bestTime) {
        this.id = id;
        this.open = open;
        this.description = description;
        this.image = image;
        this.bestTime = bestTime;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private boolean open;
    private String description;
    private int image;
    private String bestTime;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getBestTime() {
        return bestTime;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }


}
