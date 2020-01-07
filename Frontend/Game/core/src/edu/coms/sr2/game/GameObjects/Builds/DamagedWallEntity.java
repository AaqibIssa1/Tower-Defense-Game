package edu.coms.sr2.game.GameObjects.Builds;

public class DamagedWallEntity extends BuildingEntity {

    public DamagedWallEntity() {
        super(null);
        hp = 1000;
        width = 100;
        height = 100;
    }


    @Override
    public int getType() {
        return -1;
    }

    @Override
    public int getCost() {
        return 0;
    }
}
