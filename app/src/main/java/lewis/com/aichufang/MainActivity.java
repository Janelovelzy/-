package lewis.com.aichufang;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import lewis.com.aichufang.base.BaseActivity;
import lewis.com.aichufang.ui.frg.HomeFrag;
import lewis.com.aichufang.ui.frg.MineFrag;
import lewis.com.aichufang.ui.frg.SheQuFrag;

public class MainActivity extends BaseActivity {
    @BindView(R.id.bnv_bottom_activity)
    BottomNavigationView bnv;
    private HomeFrag homeFrag;
    private MineFrag mineFrag;

    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment


    private MenuItem lastItem;
    private SheQuFrag sheQuFrag;


    @Override
    public int intiLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        initFragment();
    }

    @Override
    public void initData() {

    }

    private void initFragment() {

        homeFrag = new HomeFrag();
        sheQuFrag = new SheQuFrag();
        mineFrag = new MineFrag();

        fragments = new Fragment[]{homeFrag, sheQuFrag,mineFrag};
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.continer, homeFrag).show(homeFrag).commit();


        bnv.setOnNavigationItemSelectedListener(changeFragment);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.home: {
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;

                    }

                    return true;
                }

                case R.id.shequ: {

                        if (lastfragment != 1) {
                            switchFragment(lastfragment, 1);
                            lastfragment = 1;


                    }


                    return true;
                }
                case R.id.mine: {

                        if (lastfragment != 2) {
                            switchFragment(lastfragment, 2);
                            lastfragment = 2;


                    }


                    return true;
                }


            }


            return false;
        }
    };

    //切换Fragment
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.continer, fragments[index]);


        }
        transaction.show(fragments[index]).commitAllowingStateLoss();


    }


}
