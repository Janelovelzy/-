package lewis.com.aichufang.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseActivity;
import lewis.com.aichufang.bean.User;

public class RegActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_account;
    private EditText et_pwd;
    private EditText et_pwd_once;
    private Button bt_Login;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    private String type;


    @Override
    public int intiLayout() {
        return R.layout.act_reg;
    }

    @Override
    public void initView() {

        et_account = (EditText) findViewById(R.id.et_account);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd_once = (EditText) findViewById(R.id.et_pwd_once);
        bt_Login = (Button) findViewById(R.id.bt_Login);

        bt_Login.setOnClickListener(this);
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

        String once = et_pwd_once.getText().toString().trim();
        if (TextUtils.isEmpty(once)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd.equals(once)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!rb1.isChecked()&&!rb2.isChecked()){
            toast("请选择身份");
            return;
        }
        if (rb1.isChecked()){
            type="0";
            reg(account,pwd,type);

        }
        if (rb2.isChecked()){
            type="1";
            reg(account,pwd,type);
        }




    }

    private void reg(final String name, final String pwd, final String type) {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",name);
        bmobQuery.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.e("COUNT",list.size()+"---");
                if (list.size()==0){
                    save(name,pwd,type);
                }else {
                    Toast.makeText(RegActivity.this, "账号已存在", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(RegActivity.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void save(String name,String  pwd,String type) {
        User category = new User();
        category.account=(name);
        category.pwd=(pwd);
        category.type=type;


        category.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegActivity.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
