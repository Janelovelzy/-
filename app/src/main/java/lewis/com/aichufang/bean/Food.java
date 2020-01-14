package lewis.com.aichufang.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;



public class Food extends BmobObject {

    public String name;
    public String price;
    public String floor;
    public String type;
    public String account;
    public BmobFile img;
}
