package lewis.com.aichufang.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseActivity;
import lewis.com.aichufang.bean.Food;
import lewis.com.aichufang.bean.PingLunFood;
import lewis.com.aichufang.bean.ZanFood;
import lewis.com.aichufang.utils.SimpleAdapter;

/**
 * Created by Administrator on 2019/3/22.
 */

public class FoodInfoAct extends BaseActivity {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_floor)
    TextView tvFloor;
    @BindView(R.id.tv_zw_num)
    TextView tvZwNum;
    @BindView(R.id.iv_zan)
    ImageView iv_zan;
    @BindView(R.id.et_content)
    EditText etContent;
    private Bundle bundle;
    private Food bean;
    private SimpleAdapter<PingLunFood> adapter;
    private List<PingLunFood> foods = new ArrayList<>();

    private AlertDialog alertDialog;

    @Override
    public int intiLayout() {
        return R.layout.act_food_info;
    }

    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        bean = (Food) bundle.getSerializable("bean");
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_food_pl, foods, new SimpleAdapter.ConVert<PingLunFood>() {

            @Override
            public void convert(BaseViewHolder helper, final PingLunFood food) {
                    helper.setText(R.id.tv_account,food.account);
                    helper.setText(R.id.tv_content,food.content);
                    helper.setText(R.id.tv_time,food.getCreatedAt());

            }
        });
        recycler.setAdapter(adapter);
        Glide.with(this).load(bean.img.getUrl()).into(iv);
        tvFloor.setText(bean.floor);
        tvPrice.setText("价格￥"+bean.price);
        tvName.setText(bean.name);

        isZan();
    }

    private void getZnNum(final TextView textView){
        BmobQuery<ZanFood> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("food_id",bean.getObjectId());
        bmobQuery.findObjects(this, new FindListener<ZanFood>() {
            @Override
            public void onSuccess(List<ZanFood> list1) {
                textView.setText("点赞"+list1.size());
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    @Override
    public void initData() {
        getFoodPl();
    }

    private void getFoodPl() {
        foods.clear();
        BmobQuery<PingLunFood> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("food_id", bean.getObjectId());
        bmobQuery.findObjects(this, new FindListener<PingLunFood>() {
            @Override
            public void onSuccess(List<PingLunFood> list) {
                foods.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }




    private void commitPl(String conetnt) {
        PingLunFood pingLunFood = new PingLunFood();
        pingLunFood.account=userbean.account;
        pingLunFood.food_id=bean.getObjectId();
        pingLunFood.content=conetnt;
        pingLunFood.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("评论成功");
                getFoodPl();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }


    @OnClick({R.id.iv_back, R.id.iv_zan, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_zan:
                if (iszan){
                    cancleZan();
                }else
                    {zan();}

                break;
            case R.id.tv_send:
                String s = etContent.getText().toString();
                if (TextUtils.isEmpty(s)){
                    toast("请输入");
                    return;
                }
                commitPl(s);
                etContent.setText("");
                break;
        }
    }
    private boolean iszan=false;
    private void isZan(){
        BmobQuery<ZanFood> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("food_id", bean.getObjectId());
        bmobQuery.addWhereEqualTo("account", userbean.account);
        bmobQuery.findObjects(this, new FindListener<ZanFood>() {
            @Override
            public void onSuccess(List<ZanFood> list) {
                    if (list.size()>0){
                        iv_zan.setImageResource(R.mipmap.menu_collect_true);
                        iszan=true;
                    }else {
                        iv_zan.setImageResource(R.mipmap.menu_collect_false);
                        iszan=false;
                    }
                    getZnNum(tvZwNum);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void cancleZan(){
        BmobQuery<ZanFood> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("food_id", bean.getObjectId());
        bmobQuery.addWhereEqualTo("account", userbean.account);
        bmobQuery.findObjects(this, new FindListener<ZanFood>() {
            @Override
            public void onSuccess(List<ZanFood> list) {
                if (list.size()>0){
                    ZanFood zanFood = list.get(0);
                    zanFood.delete(FoodInfoAct.this, new DeleteListener() {
                        @Override
                        public void onSuccess() {
                            isZan();
                            toast("取消成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    private void zan(){
        ZanFood zanFood=new ZanFood();
        zanFood.account=userbean.account;
        zanFood.food_id=bean.getObjectId();
        zanFood.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                isZan();
                toast("点赞成功");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
