package lewis.com.aichufang.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseActivity;
import lewis.com.aichufang.bean.PingLunFood;
import lewis.com.aichufang.bean.PingLunTz;
import lewis.com.aichufang.bean.StoreTz;
import lewis.com.aichufang.bean.TieZi;
import lewis.com.aichufang.bean.StoreTz;
import lewis.com.aichufang.utils.SimpleAdapter;



public class TieZiInfo extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime; @BindView(R.id.tv_store)
    TextView tv_store;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.et_content)
    EditText etContent;
    private TieZi bean;
    private SimpleAdapter<PingLunTz> adapter;
    private List<PingLunTz> list=new ArrayList<>();

    @Override
    public int intiLayout() {
        return R.layout.act_tz_info;
    }

    @Override
    public void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("帖子详情");
        bean = (TieZi) getIntent().getExtras().getSerializable("bean");
        tvAccount.setText("发帖人:"+bean.account);
        tvContent.setText(bean.content);
        tvTime.setText(bean.getCreatedAt());

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter<>(R.layout.item_tz_pl, list, new SimpleAdapter.ConVert<PingLunTz>() {

            @Override
            public void convert(BaseViewHolder helper, PingLunTz tieZi) {
                helper.setText(R.id.tv_content, tieZi.account+":"+tieZi.content);
                if (tieZi.type.equals("1")){
                    TextView view = helper.getView(R.id.tv_content);
                    view.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        recycler.setAdapter(adapter);

    }

    @Override
    public void initData() {
        getData();
        isZan();
    }
    private void getData() {
        list.clear();

        BmobQuery<PingLunTz> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("tz_id",bean.getObjectId());
        bmobQuery.findObjects(this, new FindListener<PingLunTz>() {
            @Override
            public void onSuccess(List<PingLunTz> list1) {
                list.addAll(list1);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(TieZiInfo.this, "服务器异常稍后再试", Toast.LENGTH_SHORT).show();
            }
        });



    }



    @OnClick({R.id.iv_back, R.id.tv_com, R.id.tv_store})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_com:
                String s = etContent.getText().toString();
                if (TextUtils.isEmpty(s)){
                    toast("请输入");
                    return;
                }
                commitPl(s);
                etContent.setText("");
                break;
            case R.id.tv_store:
                if (iszan){
                    cancleZan();
                }else
                {zan();}
                break;
        }
    }

    private void cancleZan(){
        BmobQuery<StoreTz> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("tz_id", bean.getObjectId());
        bmobQuery.addWhereEqualTo("account", userbean.account);
        bmobQuery.findObjects(this, new FindListener<StoreTz>() {
            @Override
            public void onSuccess(List<StoreTz> list) {
                if (list.size()>0){
                    StoreTz StoreTz = list.get(0);
                    StoreTz.delete(TieZiInfo.this, new DeleteListener() {
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
        StoreTz StoreTz=new StoreTz();
        StoreTz.account=userbean.account;
        StoreTz.tz_id=bean.getObjectId();
        StoreTz.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                isZan();
                toast("收藏成功");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
    
    
    private boolean iszan=false;
    private void isZan(){
        BmobQuery<StoreTz> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("tz_id", bean.getObjectId());
        bmobQuery.addWhereEqualTo("account", userbean.account);
        bmobQuery.findObjects(this, new FindListener<StoreTz>() {
            @Override
            public void onSuccess(List<StoreTz> list) {
                if (list.size()>0){
                    tv_store.setText("已收藏");
                    iszan=true;
                }else {
                    tv_store.setText("收藏");
                    iszan=false;
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    private void commitPl(String conetnt) {
        PingLunTz pingLunTz = new PingLunTz();
        pingLunTz.account=userbean.account;
        pingLunTz.tz_id=bean.getObjectId();
        pingLunTz.content=conetnt;
        pingLunTz.pub_account=bean.account;
        pingLunTz.type=userbean.type;
        pingLunTz.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("评论成功");
                getData();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }
}
