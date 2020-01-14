package lewis.com.aichufang.ui.frg;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import lewis.com.aichufang.bean.StoreTz;
import lewis.com.aichufang.bean.TieZi;
import lewis.com.aichufang.ui.TieZiInfo;
import lewis.com.aichufang.utils.SimpleAdapter;

/**
 * Created by Administrator on 2019/12/30.
 */

public class SheQuFrag extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.et)
    EditText et;


    private List<TieZi> list = new ArrayList<>();
    private List<TieZi> allList = new ArrayList<>();
    private SimpleAdapter<TieZi> adapter;



    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frg_sq, container, false);
    }

    @Override
    public void initData() {
        super.initData();
        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new SimpleAdapter<>(R.layout.item_tz, list, new SimpleAdapter.ConVert<TieZi>() {

            @Override
            public void convert(BaseViewHolder helper, TieZi tieZi) {
                helper.setText(R.id.tv_content, tieZi.content);
                helper.setText(R.id.tv_account, "发帖人："+tieZi.account);

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
        bmobQuery.findObjects(mActivity, new FindListener<StoreTz>() {
            @Override
            public void onSuccess(List<StoreTz> list1) {
                textView.setText("收藏数"+list1.size());
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(mActivity, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @OnClick(R.id.tv_search)
    public void onViewClicked() {
        String s = et.getText().toString();
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(mActivity, "请输入关键词", Toast.LENGTH_LONG).show();
            return;
        }
        list.clear();
        for (TieZi tieZi : allList
                ) {
            if (tieZi.content.contains(s)) {
                list.add(tieZi);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        list.clear();
        allList.clear();
        BmobQuery<TieZi> bmobQuery = new BmobQuery<>();

        bmobQuery.findObjects(mActivity, new FindListener<TieZi>() {
            @Override
            public void onSuccess(List<TieZi> list1) {
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

}
