package lewis.com.aichufang.ui.frg;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseFragment;
import lewis.com.aichufang.bean.Food;

import lewis.com.aichufang.bean.PingLunFood;
import lewis.com.aichufang.bean.ZanFood;
import lewis.com.aichufang.ui.FoodInfoAct;
import lewis.com.aichufang.utils.SimpleAdapter;

/**
 * Created by Administrator on 2019/3/22.
 */

public class HomeFrag extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.et)
    EditText et;


    private List<Food> list = new ArrayList<>();
    private List<Food> allList = new ArrayList<>();
    private SimpleAdapter<Food> adapter;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frg_home, container, false);
    }

    @Override
    public void initData() {
        super.initData();
        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new SimpleAdapter<>(R.layout.item_food, list, new SimpleAdapter.ConVert<Food>() {

            @Override
            public void convert(BaseViewHolder helper, Food food) {
                helper.setText(R.id.tv_name, food.name);
                helper.setText(R.id.tv_floor, food.floor);
                helper.setText(R.id.tv_price,"￥"+ food.price);
                TextView tv_zan_num = helper.getView(R.id.tv_zan_num);
                TextView tv_pinglun_num = helper.getView(R.id.tv_pinglun_num);
                getPlNum(tv_pinglun_num,food.getObjectId());
                getZnNum(tv_zan_num,food.getObjectId());
                ImageView view = helper.getView(R.id.iv);
                Glide.with(mActivity).load(food.img.getUrl()).into(view);
            }
        });
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Bundle是用来传递数据的容器，通过它在Activity之间传递数据
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", list.get(position));
                jumpAct(FoodInfoAct.class, bundle);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        list.clear();
        allList.clear();
        BmobQuery<Food> bmobQuery = new BmobQuery<>();
       
        bmobQuery.findObjects(mActivity, new FindListener<Food>() {
            @Override
            public void onSuccess(List<Food> list1) {
                list.addAll(list1);
                allList.addAll(list1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mActivity, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getPlNum(final TextView textView,String food_id){
        BmobQuery<PingLunFood> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("food_id",food_id);
        bmobQuery.findObjects(mActivity, new FindListener<PingLunFood>() {
            @Override
            public void onSuccess(List<PingLunFood> list1) {
                textView.setText("评论"+list1.size());
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mActivity, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getZnNum(final TextView textView,String food_id){
        BmobQuery<ZanFood> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("food_id",food_id);
        bmobQuery.findObjects(mActivity, new FindListener<ZanFood>() {
            @Override
            public void onSuccess(List<ZanFood> list1) {
                textView.setText("点赞"+list1.size());
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mActivity, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

  

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5,R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                list.clear();
                for (Food Food : allList
                        ) {
                    if (Food.type.contains("早")) {
                        list.add(Food);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_2:
                list.clear();
                for (Food Food : allList
                        ) {
                    if (Food.type.contains("午")) {
                        list.add(Food);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_3:
                list.clear();
                for (Food Food : allList
                        ) {
                    if (Food.type.contains("晚")) {
                        list.add(Food);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_4:
                list.clear();
                for (Food Food : allList
                        ) {
                    if (Food.floor.contains("1")) {
                        list.add(Food);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_5:
                list.clear();
                for (Food Food : allList
                        ) {
                    if (Food.floor.contains("2")) {
                        list.add(Food);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
                  case R.id.tv_search:
                      String s = et.getText().toString();
                      if (TextUtils.isEmpty(s)) {
                          Toast.makeText(mActivity, "请输入菜品", Toast.LENGTH_LONG).show();
                          return;
                      }
                      list.clear();
                      for (Food Food : allList
                              ) {
                          if (Food.name.contains(s)) {
                              list.add(Food);
                          }
                      }
                      adapter.notifyDataSetChanged();
                break;
                
        }
    }
}
