package com.abbott.libcircle.wheel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.abbott.libcircle.LoopScrollListener;
import com.abbott.libcircle.LoopView;
import com.abbott.libcircle.R;
import com.abbott.libcircle.base.BasePopWindow;
import com.abbott.libcircle.entity.PickerItem;
import com.abbott.libcircle.utils.AbPreconditions;

import java.util.ArrayList;
import java.util.List;


/**
 * 地址选择popwindow
 * <p>
 * 可以控制loop
 */
public class CommonPickPopWinLoop<T extends PickerItem> extends BasePopWindow implements OnClickListener, LoopView.OnPickedListener {


    private Button cancelBtn;
    private Button confirmBtn;
    private LoopView comPicker;
    private View pickerContainerV;
    private View contentView;


    private String id; // id
    private String name; // name

    private Context mContext; // 上下文
    private OnPickCompletedListener mListener;// 地址选择完成事件监听器
    private CancelListener mCancelListener;
    private List<T> commonModels = null; // 省份信息列表

    /**
     * g构造函数
     *
     * @param cxt
     * @param l   选中监听
     */
    public CommonPickPopWinLoop(Context cxt, List<T> commonModels, OnPickCompletedListener l) {
        super(cxt);
        this.mContext = cxt;
        this.mListener = l;
        this.commonModels = commonModels;

        init();
    }

    /**
     * g构造函数
     *
     * @param cxt
     * @param l   选中监听
     */
    public CommonPickPopWinLoop(Context cxt, List<T> commonModels, OnPickCompletedListener l, String defId) {
        super(cxt);
        this.mContext = cxt;
        this.mListener = l;
        this.commonModels = commonModels;
        this.id=defId;
        init();
    }

    public CommonPickPopWinLoop(Context cxt, String def, List<T> commonModels, OnPickCompletedListener l) {
        super(cxt);
        this.mContext = cxt;
        this.mListener = l;
        this.commonModels = commonModels;
        this.name = def;


        if (!AbPreconditions.checkNotEmptyList(commonModels)) {

            Toast.makeText(mContext,"初始化数据失败",Toast.LENGTH_SHORT).show();
            return;
        }


        this.id = getIdByName(def, commonModels);

        init();
    }


    private String getIdByName(String def, List<T> commonModels) {
        for (T commonModel : commonModels) {
            if (commonModel.getText() != null && commonModel.getText().equals(def)) {
                return commonModel.getId();
            }
        }
        return "";
    }

    @Override
    protected View getConView(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_common_picker_loop, null);
        return contentView;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InflateParams")
    private void init() {

        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        comPicker = (LoopView) contentView.findViewById(R.id.picker_common);
//        comPicker.set

        pickerContainerV = contentView.findViewById(R.id.container_picker);

        if (null == commonModels) {

            Toast.makeText(mContext,"pop的model不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);
        comPicker.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {

                T commonModel = commonModels.get(item);
                name = commonModel.getText();
                id = commonModel.getId();

//                name = comPicker.get
            }
        });
        initPickerViews();
    }

    /**
     * 初始化选择器试图
     */
    private void initPickerViews() {


        List<PickerItem> pickerItems = new ArrayList<>();

        List<String> datas = new ArrayList<>();
        for (T commonModel : commonModels) {
            pickerItems.add(commonModel);

            datas.add(commonModel.getText());
        }


        comPicker.setDataList(datas);

        int select = getSelectById(pickerItems, id);
        comPicker.setInitPosition(select);

        //如果没有初始值，则默认给一个初始值。
        if (select == 0) {
            name = commonModels.get(0).getText();
            id = commonModels.get(0).getId();
        }
    }

    private int getSelectById(List<PickerItem> pickerItems, String id) {
        if(pickerItems==null || id==null){
            return 0;
        }
        for (int i = 0; i < pickerItems.size(); i++) {
            if (id.equals(pickerItems.get(i).getId())) {
                return i;
            }
        }
        return 0;
    }


    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {
            if(mCancelListener!=null){
                mCancelListener.onCancel();
            }
            dismiss();
        } else if (v == confirmBtn) {

            if (null != mListener) {
                int position=comPicker.getSelectedItem();
                if(position>=0 && position<commonModels.size()) {
                    T commonModel = commonModels.get(comPicker.getSelectedItem());
                    String name = commonModel.getText();
                    String id = commonModel.getId();
                    mListener.onPickCompleted(id, name,commonModel);
                }
            }

            dismiss();
        }
    }

    @Override
    public void onPicked(int requestId, PickerItem item) {

        name = item.getText();
        id = item.getId();
    }

    /**
     * 地址选择完成事件监听器接口
     */
    public interface OnPickCompletedListener<T extends PickerItem> {

        /**
         * 2
         */
        void onPickCompleted(String id, String name,T t);
    }

    public CancelListener getCancelListener() {
        return mCancelListener;
    }

    public void setCancelListener(CancelListener cancelListener) {
        this.mCancelListener = cancelListener;
    }

    public interface CancelListener {

        void onCancel();
    }


}
