package layout;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.schooler.schoolerapplication.R;
import com.schooler.schoolerapplication.datamodel.MyInfo;
import com.schooler.schoolerapplication.datamodel.OtherInfo;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainFragment extends Fragment {
    private LinearLayout contentLayout;


    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);
        fragment.findViewById(R.id.card_main).setMinimumHeight(fragment.findViewById(R.id.card_main).getWidth());
        contentLayout = (LinearLayout)fragment.findViewById(R.id.content_layout);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(getActivity().getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        OtherInfo other = realm.where(OtherInfo.class).findFirst();
        if(other!=null){
            ((TextView)fragment.findViewById(R.id.tv_name)).setText(other.getName());
            AQuery aq = new AQuery(getActivity().getApplicationContext());
            aq.id(((ImageView) fragment.findViewById(R.id.iv_card))).image(other.getProfileImage());

        }
        addCategory("대덕");
        addCategory("대구");
        addCategory("선린");
        addCategory("디지털");
        return fragment;
    }

    public void addCategory(String school){
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(getActivity().getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<OtherInfo> list = realm.where(OtherInfo.class).contains("school", school).findAll();
        if(list.size()>0) {
            View rootView = getActivity().getLayoutInflater().inflate(R.layout.category_view, null);
            ((TextView) rootView.findViewById(R.id.tv_title)).setText(list.get(0).getSchool());
            LinearLayout slot = (LinearLayout) rootView.findViewById(R.id.item_slot);
            for (OtherInfo other : list) {
                View card = getActivity().getLayoutInflater().inflate(R.layout.business_card, null);
                ((TextView) card.findViewById(R.id.tv_name)).setText(other.getName());
                if(other.getProfileImage()!=null) {
                    AQuery aq = new AQuery(getActivity().getApplicationContext());
                    aq.id(((ImageView) card.findViewById(R.id.iv_card))).image(other.getProfileImage());
                }
                slot.addView(card);
            }
            contentLayout.addView(rootView);
        }
        realm.commitTransaction();
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
