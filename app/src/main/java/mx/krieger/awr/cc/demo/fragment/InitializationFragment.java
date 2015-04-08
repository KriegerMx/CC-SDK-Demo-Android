package mx.krieger.awr.cc.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

import mx.krieger.awr.cc.sdk.library.SDKManager;
import mx.krieger.awr.cc.sdk.library.beans.Gender;
import mx.krieger.awr.cc.sdk.library.beans.UserInfoHolder;
import mx.krieger.awr.cc.sdk.library.listener.RegistrationListener;
import mx.krieger.awrbeacons.demo.R;
import mx.krieger.awr.cc.demo.listener.OnFragmentInteractionListener;
import mx.krieger.awr.cc.demo.util.Properties;

public class InitializationFragment extends Fragment implements View.OnClickListener,
        RegistrationListener {
    public final static int RESULT_SUCCESS = 0;

    private EditText etBDay;
    private RadioGroup rgGenre;
    private View formContainer;
    private View loader;
    private View btnSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_initialization, container, false);
        etBDay = (EditText) v.findViewById(R.id.frag_init_et_bday);
        rgGenre = (RadioGroup) v.findViewById(R.id.frag_init_genre);
        btnSend = v.findViewById(R.id.frag_init_btn_send);
        formContainer = v.findViewById(R.id.frag_init_container_form);
        loader = v.findViewById(R.id.frag_init_loader);

        btnSend.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frag_init_btn_send:
                UserInfoHolder userInfoHolder = new UserInfoHolder();
                try {
                    String bDay = etBDay.getText().toString();
                    Calendar a = Calendar.getInstance();
                    a.set(Calendar.MONTH, Integer.parseInt(bDay.substring(0, 2)) - 1);
                    a.set(Calendar.DAY_OF_MONTH, Integer.parseInt(bDay.substring(3, 5)));
                    a.set(Calendar.YEAR, Integer.parseInt(bDay.substring(6, bDay.length())));

                    userInfoHolder.setDateOfBirth(a);

                    Gender gender = Gender.MALE;
                    if (rgGenre.getCheckedRadioButtonId() == R.id.frag_init_radio_female)
                        gender = Gender.FEMALE;
                    userInfoHolder.setGender(gender);
                    Log.d(InitializationFragment.class.getName(), "Initializing registration process");

                    SDKManager manager = SDKManager.getInstance(getActivity().getApplicationContext());
                    manager.registerAppInBackground(Properties.APP_ID, userInfoHolder, this, true);
                    formContainer.setVisibility(View.GONE);
                    loader.setVisibility(View.VISIBLE);
                    v.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error. Please review your input", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onSDKRegistrationComplete(int statusCode, Object data) {
        switch(statusCode){
            case SDKManager.SUCCESS:
                //THE APP REGISTERED SUCCESSFULLY, TIME TO MOVE TO THE NEXT SCREEN
                OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                listener.onFragmentInteraction(Properties.FRAG_INIT, RESULT_SUCCESS, null);
                break;
            default:
                //SOME ERROR HAPPENED, DECIDE WHAT TO DO NEXT BASED ON THE STATUS CODE
                formContainer.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
