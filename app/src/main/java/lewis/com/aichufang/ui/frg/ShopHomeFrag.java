package lewis.com.aichufang.ui.frg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guoxiaoxing.phoenix.compress.picture.internal.PictureCompressor;
import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.listener.ImageLoader;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.guoxiaoxing.phoenix.picker.Phoenix;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import lewis.com.aichufang.R;
import lewis.com.aichufang.base.BaseFragment;
import lewis.com.aichufang.bean.Food;
import lewis.com.aichufang.bean.User;
import lewis.com.aichufang.utils.ACache;

/**
 * Created by Administrator on 2019/12/30.
 */

public class ShopHomeFrag extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    EditText tv_name;
    @BindView(R.id.tv_price)
    EditText tvPrice;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    @BindView(R.id.rb5)
    RadioButton rb5;

    private String name;
    private User userbean;
    private String compressPath;
    private String price;
    private String floor="1楼";
    private String type="早餐";

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frg_shop_home, container, false);
    }

    @Override
    public void initData() {
        super.initData();
        userbean = (User) ACache.get(mActivity).getAsObject("userbean");
        tvTitle.setText("编辑菜品");
        tv_right.setText("发布");
        Phoenix.config()
                .imageLoader(new ImageLoader() {
                    @Override
                    public void loadImage(Context mContext, ImageView imageView
                            , String imagePath, int type) {
                        Glide.with(mContext)
                                .load(imagePath)
                                .into(imageView);
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<MediaEntity> result = Phoenix.result(data);
        MediaEntity entity = result.get(0);
        String localPath = entity.getLocalPath();
        Glide.with(getActivity()).load(localPath).into(ivHead);
        File file = new File(localPath);
        try {
            File compressFIle = PictureCompressor.with(getActivity())
                    .savePath(getActivity().getCacheDir().getAbsolutePath())
                    .load(file)
                    .get();
            if (compressFIle != null) {
                compressPath = compressFIle.getAbsolutePath();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @OnClick({R.id.tv_right, R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                name=tv_name.getText().toString();
                price=tvPrice.getText().toString();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(getActivity(),"请输入名字",Toast.LENGTH_SHORT).show();
                    return;
                } if (TextUtils.isEmpty(price)){
                    Toast.makeText(getActivity(),"请输入价格",Toast.LENGTH_SHORT).show();
                    return;
                }if (TextUtils.isEmpty(compressPath)){
                    Toast.makeText(getActivity(),"请选择图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rb1.isChecked()){
                    type="早餐";
                }   if (rb2.isChecked()){
                    type="午餐";
                }   if (rb3.isChecked()){
                    type="晚餐";
                }   if (rb4.isChecked()){
                    floor="1楼";
                }   if (rb5.isChecked()){
                    type="2楼";
                }
                final BmobFile bmobFile = new BmobFile(new File(compressPath));
                bmobFile.uploadblock(getActivity(), new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        Food food=new Food();
                        food.name=name;
                        food.price=price;
                        food.floor=floor;
                        food.type=type;

                        food.account=userbean.account;
                        food.img=bmobFile;



                        food.save(getActivity(), new SaveListener() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onSuccess() {

                                Toast.makeText(getActivity(),"发布成功",Toast.LENGTH_SHORT).show();
                                tv_name.setText("");
                                tvPrice.setText("");
                                Glide.with(getActivity()).load(getResources().getColor(R.color.colorPrimary)).into(ivHead);
                                compressPath="";
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                break;
            case R.id.iv_head:
                Phoenix.with()
                        .theme(PhoenixOption.THEME_DEFAULT)// 主题
                        .fileType(MimeType.ofImage())//显示的文件类型图片、视频、图片和视频
                        .maxPickNumber(1)// 最大选择数量
                        .minPickNumber(0)// 最小选择数量
                        .spanCount(4)// 每行显示个数
                        .enablePreview(true)// 是否开启预览
                        .enableCamera(true)// 是否开启拍照
                        .enableAnimation(true)// 选择界面图片点击效果
                        .enableCompress(true)// 是否开启压缩
                        .compressPictureFilterSize(1024)//多少kb以下的图片不压缩
                        .compressVideoFilterSize(2018)//多少kb以下的视频不压缩
                        .thumbnailHeight(160)// 选择界面图片高度
                        .thumbnailWidth(160)// 选择界面图片宽度
                        .enableClickSound(false)// 是否开启点击声音

                        .mediaFilterSize(10000)//显示多少kb以下的图片/视频，默认为0，表示不限制
                        //如果是在Activity里使用就传Activity，如果是在Fragment里使用就传Fragment
                        .start(getActivity(), PhoenixOption.TYPE_PICK_MEDIA, 100);
                break;
        }
    }
}
