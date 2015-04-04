package com.youlongnet.lulu.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.youlongnet.lulu.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通用的使用shape实现的Tab
 * Created by lyao.
 */
public class AndroidSegmentedControlView extends RadioGroup {

    private int mSdk;
    private Context mCtx;

    private OnSelectionChangedListener mListener;

    /*item背景 shape颜色*/
    private int selectedColor = Color.parseColor("#0099CC");
    private int unselectedColor = Color.TRANSPARENT;
    /*item文字 shape颜色*/
    private int selectedTextColor = Color.WHITE;
    private int unselectedTextColor = Color.parseColor("#0099CC");

    private int defaultSelection = -1;
    private boolean stretch = false;/*是否填充父View*/
    private boolean equalWidth = true;/*所有item宽度一致*/
    private String identifier = "";
    private ColorStateList textColorStateList;

    private LinkedHashMap<String, String> itemMap = new LinkedHashMap<String, String>();
    private ArrayList<RadioButton> options;
    private boolean mNeedNotify = true;
    /*默认每个item都有事件，外部需要实现OnSelectionChangedListener*/
    private OnCheckedChangeListener selectionChangedlistener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
			/*重复点击不回调*/
            if (mListener != null && mNeedNotify) {
                mListener.newSelection(identifier, itemMap.get(((RadioButton) group.findViewById(checkedId)).getText().toString()));
            }

        }
    };

    public AndroidSegmentedControlView(Context context) {
        super(context, null);
        init(context);
        update();
    }

    public AndroidSegmentedControlView(Context context, AttributeSet attrs) throws Exception {
        super(context, attrs);

        init(context);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultipleSelectionButton, 0, 0);

        try {

            selectedColor = attributes.getColor(R.styleable.MultipleSelectionButton_ascv_selectedColor, selectedColor);
            unselectedColor = attributes.getColor(R.styleable.MultipleSelectionButton_ascv_unselectedColor, unselectedColor);
            selectedTextColor = attributes.getColor(R.styleable.MultipleSelectionButton_ascv_selectedTextColor, selectedTextColor);
            unselectedTextColor = attributes.getColor(R.styleable.MultipleSelectionButton_ascv_unselectedTextColor, selectedColor);

			/*代码编写，根据check状态来切换文字shape*/
            textColorStateList = new ColorStateList(new int[][]{{-android.R.attr.state_checked}, {android.R.attr.state_checked}}, new int[]{unselectedTextColor, selectedTextColor});

            defaultSelection = attributes.getInt(R.styleable.MultipleSelectionButton_ascv_defaultSelection, defaultSelection);
            equalWidth = attributes.getBoolean(R.styleable.MultipleSelectionButton_ascv_equalWidth, equalWidth);
            stretch = attributes.getBoolean(R.styleable.MultipleSelectionButton_ascv_stretch, stretch);
            identifier = attributes.getString(R.styleable.MultipleSelectionButton_ascv_identifier);

            CharSequence[] itemArray = attributes.getTextArray(R.styleable.MultipleSelectionButton_ascv_items);
            CharSequence[] valueArray = attributes.getTextArray(R.styleable.MultipleSelectionButton_ascv_values);

            // TODO: 预览文本
            if (this.isInEditMode()) {
                itemArray = new CharSequence[]{"YES", "NO", "MAYBE", "DON'T KNOW"};
            }

            // 长度与文本数组一致
            if (itemArray != null && valueArray != null) {
                if (itemArray.length != valueArray.length) {
                    throw new Exception("Item labels and value arrays must be the same size");
                }
            }

            if (itemArray != null) {

                if (valueArray != null) {
                    for (int i = 0; i < itemArray.length; i++) {
                        itemMap.put(itemArray[i].toString(), valueArray[i].toString());
                    }

                } else {
                    /*item标识valueArray为空时，默认给予下标为标识*/
                    for (int i = 0; i < itemArray.length; i++) {
                        itemMap.put(itemArray[i].toString(), i + "");
                    }
                }

            }

        } finally {
            attributes.recycle();
        }

        update();
    }

    private void init(Context context) {
        mCtx = context;
        mSdk = android.os.Build.VERSION.SDK_INT;
        /*全局内边距*/
        this.setPadding(10, 10, 10, 10);
    }

    @TargetApi(16)
	/*编译不报错*/
    private void update() {
        this.removeAllViews();
		/*线条宽度*/
        int twoDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        this.setOrientation(RadioGroup.HORIZONTAL);

        float textWidth = 0;
        options = new ArrayList<RadioButton>();

        Iterator itemIterator = itemMap.entrySet().iterator();
        int i = 0;
        while (itemIterator.hasNext()) {

            Map.Entry<String, String> item = (Map.Entry) itemIterator.next();
            RadioButton rb = new RadioButton(mCtx);
            rb.setTextColor(textColorStateList);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (stretch) {
                params.weight = 1.0f;
            }
            if (i > 0) {
                params.setMargins(-twoDP, 0, 0, 0);
            }

            rb.setLayoutParams(params);

			/*刷新之后重置样式*/
            rb.setButtonDrawable(new StateListDrawable());

            if (i == 0) {
				/*第一个：以下是代码方式编写shape*/
                GradientDrawable leftUnselected = (GradientDrawable) mCtx.getResources().getDrawable(R.drawable.tv_left_option).mutate();
                leftUnselected.setStroke(twoDP, selectedColor);
                leftUnselected.setColor(unselectedColor);

                GradientDrawable leftSelected = (GradientDrawable) mCtx.getResources().getDrawable(R.drawable.tv_left_option_selected).mutate();
                leftSelected.setColor(selectedColor);
                leftSelected.setStroke(twoDP, selectedColor);

                StateListDrawable leftStateListDrawable = new StateListDrawable();
                leftStateListDrawable.addState(new int[]{-android.R.attr.state_checked}, leftUnselected);
                leftStateListDrawable.addState(new int[]{android.R.attr.state_checked}, leftSelected);

                if (mSdk < Build.VERSION_CODES.JELLY_BEAN) {
                    rb.setBackgroundDrawable(leftStateListDrawable);
                } else {
                    rb.setBackground(leftStateListDrawable);
                }

            } else if (i == (itemMap.size() - 1)) {
				/*最后一个*/
                GradientDrawable rightUnselected = (GradientDrawable) mCtx.getResources().getDrawable(R.drawable.tv_right_option).mutate();
                rightUnselected.setStroke(twoDP, selectedColor);
                rightUnselected.setColor(unselectedColor);

                GradientDrawable rightSelected = (GradientDrawable) mCtx.getResources().getDrawable(R.drawable.tv_right_option_selected).mutate();
                rightSelected.setColor(selectedColor);
                rightSelected.setStroke(twoDP, selectedColor);

                StateListDrawable rightStateListDrawable = new StateListDrawable();
                rightStateListDrawable.addState(new int[]{-android.R.attr.state_checked}, rightUnselected);
                rightStateListDrawable.addState(new int[]{android.R.attr.state_checked}, rightSelected);

                if (mSdk < Build.VERSION_CODES.JELLY_BEAN) {
                    rb.setBackgroundDrawable(rightStateListDrawable);
                } else {
                    rb.setBackground(rightStateListDrawable);
                }

            } else {
				/*中间样式*/
                GradientDrawable middleUnselected = (GradientDrawable) mCtx.getResources().getDrawable(R.drawable.tv_middle_option).mutate();
                middleUnselected.setStroke(twoDP, selectedColor);
                middleUnselected.setDither(true);
                middleUnselected.setColor(unselectedColor);

                GradientDrawable middleSelected = (GradientDrawable) mCtx.getResources().getDrawable(R.drawable.tv_middle_option_selected).mutate();
                middleSelected.setColor(selectedColor);
                middleSelected.setStroke(twoDP, selectedColor);

                StateListDrawable middleStateListDrawable = new StateListDrawable();
                middleStateListDrawable.addState(new int[]{-android.R.attr.state_checked}, middleUnselected);
                middleStateListDrawable.addState(new int[]{android.R.attr.state_checked}, middleSelected);

                if (mSdk < Build.VERSION_CODES.JELLY_BEAN) {
                    rb.setBackgroundDrawable(middleStateListDrawable);
                } else {
                    rb.setBackground(middleStateListDrawable);
                }

            }

            rb.setLayoutParams(params);
			/*最小宽度*/
            rb.setMinWidth(twoDP * 10);
            rb.setGravity(Gravity.CENTER);
			/*字体样式*/
            rb.setTypeface(null, Typeface.NORMAL);
            rb.setText(item.getKey());
			/*默认item宽度根据文字生成(测量radioButton的文本宽度)：可设置setEqualWidth(true)*/
            textWidth = Math.max(rb.getPaint().measureText(item.getKey()), textWidth);
            options.add(rb);
            i++;
        }

		/*每个RadioButton递增宽度并添加到容器中*/
        for (RadioButton option : options) {
            if (equalWidth) {
                option.setWidth((int) (textWidth + (twoDP * 20)));
            }
            this.addView(option);
        }

		/*事件处理*/
        this.setOnCheckedChangeListener(selectionChangedlistener);

		/*默认选择按钮*/
        if (defaultSelection > -1) {
            this.check(((RadioButton) getChildAt(defaultSelection)).getId());
        }
    }

    /*返回已选择的item的id字符串数组*/
    public String[] getCheckedWithIdentifier() {
        return new String[]{identifier, itemMap.get(((RadioButton) this.findViewById(this.getCheckedRadioButtonId())).getText().toString())};
    }

    /*返回已选择的item的id*/
    public String getChecked() {
        return itemMap.get(((RadioButton) this.findViewById(this.getCheckedRadioButtonId())).getText().toString());
    }

    /*代码编写时，传入条目文本key数组、条目文本value数组(可选)*/
    public void setItems(String[] itemArray, String[] valueArray) throws Exception {

        itemMap.clear();

        if (itemArray != null && valueArray != null) {
            if (itemArray.length != valueArray.length) {
                throw new Exception("Item labels and value arrays must be the same size");
            }
        }

        if (itemArray != null) {

            if (valueArray != null) {
                for (int i = 0; i < itemArray.length; i++) {
                    itemMap.put(itemArray[i].toString(), valueArray[i].toString());
                }

            } else {

                for (CharSequence item : itemArray) {
                    itemMap.put(item.toString(), item.toString());
                }

            }

        }

        update();
    }

    /*代码编写时，传入条目文本key数组、条目文本value数组(可选)、默认选择条目角标*/
    public void setItems(String[] items, String[] values, int defaultSelection) throws Exception {

        if (defaultSelection > (items.length - 1)) {
            throw new Exception("Default selection cannot be greater than the number of items");
        } else {
            this.defaultSelection = defaultSelection;
            setItems(items, values);
        }
    }

    /*设置默认选择角标*/
    public void setDefaultSelection(int defaultSelection) throws Exception {
        if (defaultSelection > (itemMap.size() - 1)) {
            throw new Exception("Default selection cannot be greater than the number of items");
        } else {
            this.defaultSelection = defaultSelection;
            update();
        }
    }

    /*item背景颜色=未选择的文字颜色，item未选择的背景颜色=已选择的文字颜色*/
    public void setColors(int primaryColor, int secondaryColor) {
        this.selectedColor = primaryColor;
        this.selectedTextColor = secondaryColor;
        this.unselectedColor = secondaryColor;
        this.unselectedTextColor = primaryColor;

        textColorStateList = new ColorStateList(new int[][]{{-android.R.attr.state_checked}, {android.R.attr.state_checked}}, new int[]{unselectedTextColor, selectedTextColor});
        update();
    }

    /*(建议)item背景颜色=未选择的文字颜色，item未选择的背景颜色=已选择的文字颜色*/
    public void setColors(int selectedColor, int selectedTextColor, int unselectedColor, int unselectedTextColor) {
        this.selectedColor = selectedColor;
        this.selectedTextColor = selectedTextColor;
        this.unselectedColor = unselectedColor;
        this.unselectedTextColor = unselectedTextColor;

        // Set text selectedColor state list
        textColorStateList = new ColorStateList(new int[][]{{-android.R.attr.state_checked}, {android.R.attr.state_checked}}, new int[]{unselectedTextColor, selectedTextColor});

        update();
    }

    /*根据value来选择item*/
    public void setByValue(boolean needNotifyCheckChange, String value) {
        String buttonText = "";
		/*暂时不执行接口回调*/
        mNeedNotify = needNotifyCheckChange;
        if (this.itemMap.containsValue(value)) {
            for (String entry : itemMap.keySet()) {
                if (itemMap.get(entry).equalsIgnoreCase(value)) {
                    buttonText = entry;
                }
            }
        }

        for (RadioButton option : options) {
            if (option.getText().toString().equalsIgnoreCase(buttonText)) {
                this.check(option.getId());
            }
        }
        mNeedNotify = true;
    }

    /*监听事件：只要item状态改变即回调*/
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.mListener = listener;
    }

    /*设置标识，多个此控件使用同一个监听时使用*/
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /*item宽度一致*/
    public void setEqualWidth(boolean equalWidth) {
        this.equalWidth = equalWidth;
        update();
    }

    /*容器是否填充父View*/
    public void setStretch(boolean stretch) {
        this.stretch = stretch;
        update();
    }

    /*事件回调*/
    public interface OnSelectionChangedListener {
        public void newSelection(String identifier, String value);
    }

}
