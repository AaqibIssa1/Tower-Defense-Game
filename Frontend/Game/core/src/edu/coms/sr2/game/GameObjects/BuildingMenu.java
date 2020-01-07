package edu.coms.sr2.game.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import edu.coms.sr2.game.GameObjects.Builds.BuildingEntity;
import edu.coms.sr2.game.GameObjects.Builds.FarmEntity;
import edu.coms.sr2.game.GameObjects.Builds.WallEntity;

import static com.badlogic.gdx.Gdx.input;

public class BuildingMenu {
    private BuildingEntity[] menuOptions = new BuildingEntity[]{
            new WallEntity(),
            new FarmEntity()
    };

    private boolean menuDisplay = false;
    private boolean buildMode = false;
    private int buildSelection = -1;

    public BuildingMenu() {
    }

    public boolean isDisplaying() {
        return menuDisplay;
    }

    public BuildingEntity menuAndBuildingTouch(Vector2 touch, Iterable<BuildingEntity> currentBuildings) {
        if (touch.x >= 50 && touch.x <= 200 && touch.y >= 50 && touch.y <= 200) {
            menuDisplay = true;
            buildMode = false;
        } else if (menuDisplay) {
            int x = 50;
            int y = 50;
            for (int i = 0; i < menuOptions.length; i++) {
                x += 200;
                if (x + 150 >= 1080) {
                    x = 50;
                    y += 200;
                }
                if (touch.x > x && touch.x < x + 150 &&
                        touch.y > y && touch.y < y + 150) {
                    buildSelection = i;
                    buildMode = true;
                    menuDisplay = false;
                }
            }
        } else if (buildMode) {
            if (input.justTouched() && touch.y < 1920 / 2) {
                BuildingEntity newBuilding = BuildingEntity.Factory.make(buildSelection);
                newBuilding.setCoordinate((int) touch.x, (int) touch.y);
                newBuilding.setCoordinate((int) touch.x, (int) touch.y);

                if (hitReg(newBuilding, currentBuildings)) {
                    buildMode = false;
                    return newBuilding;
                }
            }
        }
        return null;
    }

    public static boolean hitReg(BuildingEntity newBuilding, Iterable<BuildingEntity> currentBuildings) {
        for (BuildingEntity building : currentBuildings)
            if (bvbHitReg(newBuilding, building) || bvbHitReg(building, newBuilding))
                return false;

        return true;
    }

    public static boolean bvbHitReg(BuildingEntity b, BuildingEntity b2) {
        boolean x0, y0, x1, y1;
        x0 = x1 = y0 = y1 = false;

        if (b.getCoordinate().x >= b2.getCoordinate().x && b.getCoordinate().x <= b2.getCoordinate().x + b2.getWidth()) {
            x0 = true;
        }
        if (b.getCoordinate().x + b.getWidth() >= b2.getCoordinate().x && b.getCoordinate().x + b.getWidth() <= b2.getCoordinate().x + b2.getWidth()) {
            x1 = true;
        }
        if (b.getCoordinate().y >= b2.getCoordinate().y && b.getCoordinate().y <= b2.getCoordinate().y + b2.getHeight()) {
            y0 = true;
        }
        if (b.getCoordinate().y + b.getHeight() >= b2.getCoordinate().y && b.getCoordinate().y + b.getHeight() <= b2.getCoordinate().y + b2.getHeight()) {
            y1 = true;
        }


        if ((x0 && y0) || (x1 && y1) || (x0 && y1) || (x1 && y0)) {
            return true;
        }
        return false;
    }

    public void displayMenu(SpriteBatch batch) {
        //Menu Block
        batch.draw(CommonSprites.blackSquareTexture.get(), 50, 50, 150, 150);

        //Menu Items
        if (menuDisplay) {
            int x = 50;
            int y = 50;
            for(BuildingEntity option : menuOptions) {
                x += 200;
                if (x + 150 >= 1080) {
                    x = 50;
                    y += 200;
                }
                batch.draw(option.getSprite(), x, y, 150, 150);
            }
        }
    }
}
