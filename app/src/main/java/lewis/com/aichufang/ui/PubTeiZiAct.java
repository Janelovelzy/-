package lewis.com.aichufang.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.listener.SaveListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseActivity;
import lewis.com.aichufang.bean.TieZi;


public class PubTeiZiAct extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_content)
    EditText etContent;

    @Override
    public int intiLayout() {
        return R.layout.act_pub_tz;
    }

    @Override
    public void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("发帖");
        tvRight.setText("发布");
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                String s = etContent.getText().toString();
                if (TextUtils.isEmpty(s)){
                    toast("请输入");
                    return;
                }
                pub(s);
                break;
        }
    }

    private void pub(String content){
        TieZi tieZi=new TieZi();
        tieZi.account=userbean.account;
        tieZi.content=content;
        tieZi.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("发布成功");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
