package edu.coms.sr2.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Constants
{
    public static final boolean useRealServer = true;
    public static final boolean allowLoginBypass = false;
    public static final String sr2Server = useRealServer? "coms-309-sr-2.misc.iastate.edu" : "10.0.2.2";
    public static final int sr2HttpPort = 8080;
    public static final int sr2TcpPort = 9128;
    //public static final Skin skin = new Skin(Gdx.files.internal("skins/gdx-holo/skin/uiskin.json"));
    public static final BitmapFont defaultFont = new BitmapFont();
}
