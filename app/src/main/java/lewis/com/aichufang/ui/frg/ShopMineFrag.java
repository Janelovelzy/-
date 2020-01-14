package lewis.com.aichufang.ui.frg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseFragment;
import lewis.com.aichufang.bean.PingLunTz;
import lewis.com.aichufang.bean.User;
import lewis.com.aichufang.ui.LoginActivity;
import lewis.com.aichufang.ui.MyPubTzAct;
import lewis.com.aichufang.ui.MyStoreTzAct;
import lewis.com.aichufang.utils.ACache;

/**
 * Created by Administrator on 2019/12/30.
 */

public class ShopMineFrag extends BaseFragment{



    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_account)
    TextView tvAccount;


    private User userbean;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        return inflater.inflate(R.layout.frg_shop_mine, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("个人中心");
        tvAccount.setText("用户名:"+userbean.account+"("+(userbean.type.equals("0")?"学生":"工作人员")+")");


    }

    @Override
    public void initData() {
        super.initData();


    }




    @OnClick({ R.id.tv_store, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_store:
                jumpAct(MyStoreTzAct.class);
                break;
            case R.id.tv_exit:
                ACache.get(getActivity()).clear();
                jumpAct(LoginActivity.class);
                getActivity().finish();

                break;
        }
    }
}
