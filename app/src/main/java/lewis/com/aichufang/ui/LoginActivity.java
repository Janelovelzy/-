package lewis.com.aichufang.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import lewis.com.aichufang.MainActivity;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseActivity;
import lewis.com.aichufang.bean.User;
import lewis.com.aichufang.utils.ACache;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;

    private EditText et_account;
    private EditText et_pwd;
    private TextView tv_reg;
    private Button bt_Login;
    private String type;


    @Override
    public int intiLayout() {
        return R.layout.act_login;
    }

    @Override
    public void initView() {

        et_account = (EditText) findViewById(R.id.et_account);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_reg = (TextView) findViewById(R.id.tv_reg);
        bt_Login = (Button) findViewById(R.id.bt_Login);

        bt_Login.setOnClickListener(this);
        tv_reg.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Login:
                submit();
                break;
            case R.id.tv_reg:
                startActivity(new Intent(LoginActivity.this, RegActivity.class));
                break;
        }
    }

    private void submit() {
        // validate
        String account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!rb1.isChecked()&&!rb2.isChecked()){
            toast("请选择身份");
            return;
        }
        // TODO validate success, do something
        if (rb1.isChecked()){
            type ="0";
            login(account, pwd,type);

        }
        if (rb2.isChecked()){
            type ="1";
            login(account, pwd,type);
        }





    }


    private void login(final String name, final String pwd, final String type) {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", name);
        bmobQuery.addWhereEqualTo("pwd", pwd);
        bmobQuery.addWhereEqualTo("type", type);

        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT", list.size() + "---");
                if (list.size() == 0) {
                    Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                } else {
                    User user = list.get(0);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    ACache.get(LoginActivity.this).put("userbean", list.get(0));
                    ACache.get(LoginActivity.this).put("islogin", "true");
                    if (type.equals("0")){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }  else {
                        startActivity(new Intent(LoginActivity.this, ShopMainActivity.class));
                    }

                    finish();



                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(LoginActivity.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
