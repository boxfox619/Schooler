package layout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.schooler.schoolerapplication.EditInfoActivity;
import com.schooler.schoolerapplication.R;
import com.schooler.schoolerapplication.datamodel.MyInfo;

public class MyInfoFragment extends Fragment {
    private TextView tv_birthday, tv_school, tv_phone, tv_subject;
    private ImageView iv_profileImage, iv_birthday, iv_school, iv_phone, iv_subject, iv_sns;

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
        tv_birthday = (TextView) fragment.findViewById(R.id.tv_birthday);
        iv_birthday = (ImageView) fragment.findViewById(R.id.iv_birthday);
        iv_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("Type", MyInfo.BIRTHDAY);
                intent.putExtra("Info", tv_birthday.getText());
                startActivity(intent);
            }
        });
        tv_phone = (TextView) fragment.findViewById(R.id.tv_phone);
        iv_phone = (ImageView) fragment.findViewById(R.id.iv_phone);
        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("Type", MyInfo.PHONE);
                intent.putExtra("Info", tv_phone.getText());
                startActivity(intent);
            }
        });
        tv_subject = (TextView) fragment.findViewById(R.id.tv_subject);
        iv_subject = (ImageView) fragment.findViewById(R.id.iv_subject);
        iv_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("Type", MyInfo.SUBJECT);
                intent.putExtra("Info", tv_subject.getText());
                startActivity(intent);
            }
        });
        tv_school = (TextView) fragment.findViewById(R.id.tv_school);
        iv_school = (ImageView) fragment.findViewById(R.id.iv_school);
        iv_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("Type", MyInfo.SCHOOL);
                intent.putExtra("Info", tv_school.getText());
                startActivity(intent);
            }
        });

        iv_profileImage = (ImageView) fragment.findViewById(R.id.iv_profile_image);
        iv_sns = (ImageView) fragment.findViewById(R.id.iv_sns);

        return fragment;
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
