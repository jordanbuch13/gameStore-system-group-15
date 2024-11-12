package group15.gameStore.dto;

import java.sql.Date;

import group15.gameStore.model.Game;

public class GameDto {

    //Game Attributes
    private int gameID;
    private String title;
    private String description;
    private double price;
    private int stock;
    private String image;
    private Date archivedDate;
    private boolean isApproved;

    private ManagerDto manager;

    @SuppressWarnings("unused")
    public GameDto(){
    }

    //Constructor
    public GameDto(Game game){
        this.gameID = game.getGameID();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.price = game.getPrice();
        this.stock = game.getStock();
        this.image = game.getImage();
        this.archivedDate = game.getArchivedDate();
        this.isApproved = game.isIsApproved();
        this.manager = new ManagerDto(game.getManager()); 
    }

    //Generated Getters and Setters
    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
        this.archivedDate = archivedDate;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public ManagerDto getManager() {
        return manager;
    }

    public void setManager(ManagerDto manager) {
        this.manager = manager;
    }    

    
}
