package lewis.com.aichufang.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.WindowManager;

import lewis.com.aichufang.MainActivity;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseActivity;
import lewis.com.aichufang.bean.User;
import lewis.com.aichufang.utils.ACache;


public class WelcomAct extends BaseActivity {



    @Override
    public int intiLayout() {
        return R.layout.act_wel;
    }

    @Override
    public void initView() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                String islogin = ACache.get(WelcomAct.this).getAsString("islogin");
                if (TextUtils.isEmpty(islogin)){
                    Intent intent = new Intent(WelcomAct.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    User userbean = (User) ACache.get(WelcomAct.this).getAsObject("userbean");
                    if (userbean!=null&&userbean.type.equals("0")){
                        Intent intent = new Intent(WelcomAct.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(WelcomAct.this,ShopMainActivity.class);
                        startActivity(intent);
                    }

                }

                finish();
            }
        },2000);
    }

    @Override
    public void initData() {

    }
}
