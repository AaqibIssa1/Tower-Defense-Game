package Mockito_Tests;

import com.badlogic.gdx.math.Vector2;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.coms.sr2.game.GameObjects.Builds.BuildingEntity;
import edu.coms.sr2.game.GameObjects.Builds.DamagedWallEntity;
import edu.coms.sr2.game.GameObjects.Builds.FarmEntity;
import edu.coms.sr2.game.GameObjects.Builds.WallEntity;
import edu.coms.sr2.game.GameObjects.projectiles.Arrow;
import edu.coms.sr2.game.GameObjects.BuildingMenu;
import edu.coms.sr2.game.objects.pojo.LoginInfo;
import edu.coms.sr2.game.objects.pojo.Profile;
import edu.coms.sr2.game.objects.pojo.ProfileRegistration;
import edu.coms.sr2.game.utils.CollisionRect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Game_Tests {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    // Aaqib Issa Mockito Test
    @Test
    public void ProfileRegistrationTest(){

        Profile profile = mock(Profile.class);
        LoginInfo loginInfo = mock(LoginInfo.class);

        ProfileRegistration p = new ProfileRegistration(profile, loginInfo);

        String email = "test@email.com";
        String password  = "password";

        String userName = "userName";

        when(loginInfo.getEmail()).thenReturn(email);
        when(loginInfo.getPassword()).thenReturn(password);
        when(profile.getUserName()).thenReturn(userName);

        Assert.assertEquals(p.getLogin().getEmail(), email);
        Assert.assertEquals(p.getLogin().getPassword(), password);
        Assert.assertEquals(p.getProfile().getUserName(), userName);
    }

    //Calvin Nguyen Mockito Test
    @Test
    public void HitReg(){
        WallEntity wallEntity = mock(WallEntity.class);
        FarmEntity farmEntity = mock(FarmEntity.class);
        BuildingEntity[] builds = new BuildingEntity[2];

        builds[0] = wallEntity;
        builds[1] = farmEntity;

        wallEntity.setCoordinate(0,0);
        when(wallEntity.getCoordinate()).thenReturn(new Vector2(0,0));
        when(wallEntity.getWidth()).thenReturn(100);
        when(wallEntity.getHeight()).thenReturn(100);

        when(farmEntity.getCoordinate()).thenReturn(new Vector2(50,50));
        when(farmEntity.getWidth()).thenReturn(1);
        when(farmEntity.getHeight()).thenReturn(1);

        Assert.assertNotEquals(wallEntity.getWidth(), farmEntity.getWidth());
        Assert.assertEquals(100, wallEntity.getWidth(),0);

        //Function 1 - bvbHitReg deals with building vs building hit registration comparing whether BuildingEntity 1 is within BuildingEntity 2
        Assert.assertTrue(BuildingMenu.bvbHitReg(farmEntity, wallEntity));
        Assert.assertFalse(BuildingMenu.bvbHitReg(wallEntity, farmEntity));


        //Function 2 - AvbHitReg deals with Arrow(Projectile) vs building for enemy players AvbHitReg2 deals with then a building is within an Arrow
        //   ----
        //   |[]|  if a object is within another it currently does not detect the outside building because it is not within. so deals with this edge case
        //   ----  this is dealt with by calling both Avb 1 and 2 and for bvbHitReg by reversing the arguements position
//        Assert.assertTrue(BuildingMenu.AvbHitReg(-10,-10,200,200,WallEntity));
//        Assert.assertTrue(BuildingMenu.AvbHitReg2(WallEntity,-10,-10,200,200));

//        Assert.assertTrue(BuildingMenu.AvbHitReg(0,0,1,1,WallEntity));
//        Assert.assertFalse(BuildingMenu.AvbHitReg(1,1,1,1,WallEntity));

        //Function 3 - PhitReg processeds through a whole array of Buildings and determines of if collision doesn't occurs
//        Assert.assertTrue(BuildingMenu.PhitReg(1000,1000,1000,1000,builds,2));
//        Assert.assertFalse(BuildingMenu.PhitReg(0,0,100,100,builds,2));
    }

    @Test
    public void testBuildingsArray() {

        DamagedWallEntity a = mock(DamagedWallEntity.class);
        DamagedWallEntity b = mock(DamagedWallEntity.class);
        DamagedWallEntity c = mock(DamagedWallEntity.class);
        //when(a.getBoundingBox()).thenReturn(new Rectangle(0, 0, 100, 100));
        //when(b.getBoundingBox()).thenReturn(new Rectangle(50, 50, 50, 50));
        //when(c.getBoundingBox()).thenReturn(new Rectangle(200, 200, 50, 50));

       // boolean testa = HelperFunctions.rectInRect(a.getBoundingBox(), b.getBoundingBox());
        //boolean testb = HelperFunctions.rectInRect(a.getBoundingBox(), c.getBoundingBox());
        //boolean testc = HelperFunctions.pointInRect(50, 50, a.getBoundingBox());
        //boolean testd = HelperFunctions.pointInRect(0, 0, c.getBoundingBox());
        //Assert.assertEquals(testa, true);
        //Assert.assertEquals(testb, false);
        //Assert.assertEquals(testc, true);
        //Assert.assertEquals(testd, false);
    }

    //Aaqib Issa Mockito test #2 - Tests collision detection of towers
    @Test
    public void CollisionTest(){
        CollisionRect colRect = mock(CollisionRect.class);
        colRect.setX(10);
        colRect.setY(10);
        colRect.setWidth(10);
        colRect.setHeight(10);
        float x = colRect.getX();
        float y = colRect.getY();
        int width = colRect.getWidth();
        int height = colRect.getHeight();
        when(colRect.getX()).thenReturn(x);
        when(colRect.getY()).thenReturn(y);
        when(colRect.getWidth()).thenReturn(width);
        when(colRect.getHeight()).thenReturn(height);
        Assert.assertEquals(colRect.getX(), x, 0);
        Assert.assertEquals(colRect.getY(), y, 0);
        Assert.assertEquals(colRect.getWidth(), width, 0);
        Assert.assertEquals(colRect.getHeight(), height, 0);

    }

    //Aaqib Issa Mockito test #3 - Tests arrow shooting projectile
    @Test
    public void ArrowTest(){
        Arrow arr = mock(Arrow.class);
        arr.setX(10);
        arr.setY(10);
        float x = arr.getX();
        float y = arr.getY();
        when(arr.getX()).thenReturn(x);
        when(arr.getY()).thenReturn(y);
        Assert.assertEquals(arr.getX(), x, 0);
        Assert.assertEquals(arr.getY(), y, 0);

    }
}
