package lewis.com.aichufang.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseActivity;
import lewis.com.aichufang.bean.StoreTz;
import lewis.com.aichufang.bean.TieZi;
import lewis.com.aichufang.utils.SimpleAdapter;



public class MyStoreTzAct extends BaseActivity{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;


    private List<TieZi> list = new ArrayList<>();

    private SimpleAdapter<TieZi> adapter;
    @Override
    public int intiLayout() {
        return R.layout.act_list;
    }

    @Override
    public void initView() {
        tvTitle.setText("我的收藏");
        ivBack.setVisibility(View.VISIBLE);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_tz, list, new SimpleAdapter.ConVert<TieZi>() {

            @Override
            public void convert(BaseViewHolder helper, TieZi tieZi) {
                helper.setText(R.id.tv_content, tieZi.content);
                helper.setText(R.id.tv_account, tieZi.account);

                TextView tv_store_num = helper.getView(R.id.tv_store_num);


                getStoreNum(tv_store_num,tieZi.getObjectId());
            }
        });
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", list.get(position));
                jumpAct(TieZiInfo.class, bundle);
            }
        });
    }
    private void getStoreNum(final TextView textView,String tz_id){
        BmobQuery<StoreTz> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("tz_id",tz_id);
        bmobQuery.findObjects(this, new FindListener<StoreTz>() {
            @Override
            public void onSuccess(List<StoreTz> list1) {
                textView.setText("收藏数"+list1.size());
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyStoreTzAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        list.clear();

        BmobQuery<StoreTz> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account",userbean.account);
        bmobQuery.findObjects(this, new FindListener<StoreTz>() {
            @Override
            public void onSuccess(List<StoreTz> list1) {
                for (int i = 0; i < list1.size(); i++) {
                    BmobQuery<TieZi> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("objectId",list1.get(i).tz_id);
                    bmobQuery.findObjects(MyStoreTzAct.this, new FindListener<TieZi>() {
                        @Override
                        public void onSuccess(List<TieZi> list2) {
                                list.addAll(list2);
                                adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyStoreTzAct.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
