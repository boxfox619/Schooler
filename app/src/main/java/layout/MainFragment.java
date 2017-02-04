package layout;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schooler.schoolerapplication.R;

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
        addCategory();
        addCategory();
        return fragment;
    }

    public void addCategory(){
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.category_view,null);
        ((TextView)rootView.findViewById(R.id.tv_title)).setText("테스트");
        LinearLayout slot = (LinearLayout)rootView.findViewById(R.id.item_slot);
        for(int i = 0 ; i < 3; i++){
            View card = getActivity().getLayoutInflater().inflate(R.layout.business_card,null);
            ((TextView)card.findViewById(R.id.tv_name)).setText("test");
            slot.addView(card);
        }
        contentLayout.addView(rootView);
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
