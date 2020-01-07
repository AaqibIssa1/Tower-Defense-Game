package edu.coms.sr2.game.screens.social;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Consumer;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.http.RestAPI;
import edu.coms.sr2.game.interfacing.GenericScreen;
import edu.coms.sr2.game.interfacing.views.TextEntry;
import edu.coms.sr2.game.interfacing.views.TextView;
import edu.coms.sr2.game.objects.pojo.Profile;

public class ProfileEditScreen extends GenericScreen {

    private TextView userName,realName;
    private TextEntry userNameEditor, realNameEditor;

    private Profile profile = new Profile();

    private HashMap<TextEntry, Field> editorFieldMap = new HashMap<>();

    protected ProfileEditScreen() { }
    private static ProfileEditScreen instance = new ProfileEditScreen();

    public static ProfileEditScreen getInstance() {
        return instance;
    }

    @Override
    public void onStart() {

        getProfileFromServer();
        try {
            for(Field f: Profile.class.getDeclaredFields()) {
                Application.log(TAG, "Profile field: " + f.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onResume() {

    }

    private void mapEditors() throws NoSuchFieldException, IllegalAccessException {
        for(Field f : getClass().getDeclaredFields())
            if(f.getType() == TextEntry.class) {
                TextEntry val = (TextEntry) f.get(this);
                editorFieldMap.put(val, Profile.class.getField(f.getName().replace("Editor", "")));
            }
    }

    @ButtonMethod
    public void backButton() {
        stop();
    }

    private void setFieldFromString(Field field, Object thiz, String val) {
        try {
            if (field.getType() == String.class)
                field.set(thiz, val);
            else if (field.getType() == int.class)
                field.set(thiz, Integer.parseInt(val));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setStringIfNotEmpty(TextEntry editor, Consumer<String> setter) {
        String entry = editor.getText();
        if(!entry.isEmpty())
            setter.accept(entry);
    }

    private void setIntIfNotEmpty(TextEntry editor, Consumer<Integer> setter) {
        String entry = editor.getText();
        if(!entry.isEmpty())
            setter.accept(Integer.parseInt(entry));
    }
    @ButtonMethod
    public void confirmButton() {
        setStringIfNotEmpty(userNameEditor, profile::setUserName);
        setStringIfNotEmpty(realNameEditor, profile::setRealName);
        showProfile(profile);
        RestAPI.updateProfile(profile);
    }

    public void getProfileFromServer()
    {
        RestAPI.getProfile(Application.getProfileId(), serverProfile -> {
            profile = serverProfile;
            showProfile(profile);
        });
    }

    public void showProfile(Profile profile) {
        userName.setText("Username: " + profile.getUserName());
        realName.setText("Name: " + profile.getRealName());
    }
}
