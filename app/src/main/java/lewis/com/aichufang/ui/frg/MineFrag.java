package lewis.com.aichufang.ui.frg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseFragment;
import lewis.com.aichufang.bean.PingLunTz;
import lewis.com.aichufang.bean.User;
import lewis.com.aichufang.bean.ZanFood;
import lewis.com.aichufang.ui.LoginActivity;
import lewis.com.aichufang.ui.MyPubTzAct;
import lewis.com.aichufang.ui.MyStoreTzAct;
import lewis.com.aichufang.utils.ACache;

/**
 * Created by Administrator on 2019/3/22.
 */

public class MineFrag extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_tz_num)
    TextView tvTzNum;
    Unbinder unbinder;
    private User userbean;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {

        return inflater.inflate(R.layout.frg_mine, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("个人中心");
        tvAccount.setText("用户名:"+userbean.account+"("+(userbean.type.equals("0")?"学生":"工作人员")+")");

        getTzNum();
    }

    @Override
    public void initData() {
        super.initData();


    }

    private void getTzNum(){

            BmobQuery<PingLunTz> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("pub_account",userbean.account);
            bmobQuery.findObjects(getActivity(), new FindListener<PingLunTz>() {
                @Override
                public void onSuccess(List<PingLunTz> list1) {
                    tvTzNum.setText("回帖数："+list1.size());
                }

                @Override
                public void onError(int i, String s) {

                }
            });

    }


    @OnClick({R.id.tv_pub_tz, R.id.tv_store, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pub_tz:
                jumpAct(MyPubTzAct.class);
                break;
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
