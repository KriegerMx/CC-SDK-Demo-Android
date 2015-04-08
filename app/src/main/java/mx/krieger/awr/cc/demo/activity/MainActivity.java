package mx.krieger.awr.cc.demo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import mx.krieger.awrbeacons.demo.R;
import mx.krieger.awr.cc.demo.fragment.HomeFragment;
import mx.krieger.awr.cc.demo.fragment.InitializationFragment;
import mx.krieger.awr.cc.demo.listener.OnFragmentInteractionListener;
import mx.krieger.awr.cc.demo.util.Properties;

public class MainActivity extends ActionBarActivity implements OnFragmentInteractionListener{

    private String currentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null) {
            //CHECK IF THE SDK HAS BEEN CONFIGURED, AND SKIP THE PROCESS IF NEEDED
            SharedPreferences prefs = getSharedPreferences(Properties.PREFS_FILENAME, MODE_PRIVATE);
            boolean sdkConfigured = prefs.getBoolean(Properties.PREFS_VAR_HAS_CONFIGURED_SDK, false);
            if(sdkConfigured)
                switchToFragment(Properties.FRAG_NAME_HOME, new HomeFragment());
            else
                switchToFragment(Properties.FRAG_NAME_INIT, new InitializationFragment());
        }
    }

    @Override
    public void onFragmentInteraction(int fragmentID, int result, Object data) {
        switch (fragmentID){
            case Properties.FRAG_INIT:
                if(result==InitializationFragment.RESULT_SUCCESS) {
                    //Configure app so that the SDK initialization happens only once
                    SharedPreferences prefs = getSharedPreferences(Properties.PREFS_FILENAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Properties.PREFS_VAR_HAS_CONFIGURED_SDK, true);
                    editor.commit();
                    //Switch to new screen
                    switchToFragment(Properties.FRAG_NAME_HOME, new HomeFragment());
                }
                break;
        }
    }

    private void switchToFragment(String name, Fragment frag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(currentFragmentTag!=null)
            ft.remove(fm.findFragmentByTag(currentFragmentTag));
        currentFragmentTag = name;
        ft.add(R.id.act_main_container_root, frag, currentFragmentTag);
        ft.commit();
    }
}
