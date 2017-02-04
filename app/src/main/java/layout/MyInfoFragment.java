package layout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.schooler.schoolerapplication.EditInfoActivity;
import com.schooler.schoolerapplication.MakeCardActivity;
import com.schooler.schoolerapplication.R;
import com.schooler.schoolerapplication.datamodel.MyInfo;
import com.schooler.schoolerapplication.datamodel.OtherInfo;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyInfoFragment extends Fragment {
    private TextView tv_birthday, tv_school, tv_phone, tv_subject, tv_name;
    private ImageView iv_profileImage, iv_birthday, iv_school, iv_phone, iv_subject, iv_sns, iv_card;

    public MyInfoFragment() {
    }

    public static MyInfoFragment newInstance() {
        MyInfoFragment fragment = new MyInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_my_info, container, false);
        iv_card = (ImageView) fragment.findViewById(R.id.iv_card);
        iv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MakeCardActivity.class));
            }
        });
        tv_name = (TextView) fragment.findViewById(R.id.tv_name);
        tv_birthday = (TextView) fragment.findViewById(R.id.tv_birthday);
        iv_birthday = (ImageView) fragment.findViewById(R.id.iv_birthday);
        iv_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RealmConfiguration realmConfig = new RealmConfiguration
                        .Builder(getActivity().getApplicationContext())
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.setDefaultConfiguration(realmConfig);
                Realm realm = Realm.getDefaultInstance();
                MyInfo myInfo = realm.where(MyInfo.class).findFirst();
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("Type", MyInfo.BIRTHDAY);
                intent.putExtra("Info", myInfo.getBirthday());
                startActivity(intent);
            }
        });
        tv_phone = (TextView) fragment.findViewById(R.id.tv_phone);
        iv_phone = (ImageView) fragment.findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RealmConfiguration realmConfig = new RealmConfiguration
                        .Builder(getActivity().getApplicationContext())
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.setDefaultConfiguration(realmConfig);
                Realm realm = Realm.getDefaultInstance();
                MyInfo myInfo = realm.where(MyInfo.class).findFirst();
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("Type", MyInfo.PHONE);
                intent.putExtra("Info", myInfo.getPhone());
                startActivity(intent);
            }
        });
        tv_subject = (TextView) fragment.findViewById(R.id.tv_subject);
        iv_subject = (ImageView) fragment.findViewById(R.id.iv_subject);
        iv_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RealmConfiguration realmConfig = new RealmConfiguration
                        .Builder(getActivity().getApplicationContext())
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.setDefaultConfiguration(realmConfig);
                Realm realm = Realm.getDefaultInstance();
                MyInfo myInfo = realm.where(MyInfo.class).findFirst();
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("Type", MyInfo.SUBJECT);
                intent.putExtra("Info", myInfo.getSubject());
                startActivity(intent);
            }
        });
        tv_school = (TextView) fragment.findViewById(R.id.tv_school);
        iv_school = (ImageView) fragment.findViewById(R.id.iv_school);
        iv_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RealmConfiguration realmConfig = new RealmConfiguration
                        .Builder(getActivity().getApplicationContext())
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.setDefaultConfiguration(realmConfig);
                Realm realm = Realm.getDefaultInstance();
                MyInfo myInfo = realm.where(MyInfo.class).findFirst();
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("Type", MyInfo.SCHOOL);
                intent.putExtra("Info", myInfo.getSchool());
                startActivity(intent);
            }
        });
        iv_profileImage = (ImageView) fragment.findViewById(R.id.iv_profile_image);
        iv_sns = (ImageView) fragment.findViewById(R.id.iv_sns);
        setInfo();
        return fragment;
    }

    private void setInfo() {
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(getActivity().getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        MyInfo myInfo = realm.where(MyInfo.class).findFirst();
        tv_name.setText(myInfo.getName());
        tv_birthday.setText(myInfo.getBirthday());
        tv_phone.setText(myInfo.getPhone());
        tv_subject.setText(myInfo.getSubject());
        tv_school.setText(myInfo.getSchool());
        if (myInfo.getProfileImage() != null) {
            AQuery aq = new AQuery(getActivity().getApplicationContext());
            Log.d("dudco", myInfo.getProfileImage());
            aq.id(iv_card).image(myInfo.getProfileImage());
        }
        realm.commitTransaction();
    }

    @Override
    public void onResume() {
        super.onResume();
        setInfo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
