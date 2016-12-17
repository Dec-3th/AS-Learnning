
package com.jirdy.listview.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jirdy.listview.R;
import com.jirdy.listview.model.Book;
import com.jirdy.listview.model.Table;

import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class TableAdapter extends BaseAdapter {
    public static String TAG = "Jirdy.Read.Table_Adapter";
    private Context context;
    private List<Table.TableRow> table;


    public
    TableAdapter(Context context, List<Table.TableRow> table) {
        this.context = context;
        this.table = table;
    }

    @Override
    public int getCount() {
        return table.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Table.TableRow getItem(int position) {
        return table.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Table.TableRow tableRow = getItem(position);

//        Log.i(TAG, "更新tableView：" + tableRow.getCellValue(1).value);

        if (position == 0)
            return new TableRowView(this.context, tableRow, context.getResources().getColor(R.color.tabletitle));
        else if (position % 2 == 0)
            return new TableRowView(this.context, tableRow, context.getResources().getColor(R.color.tablerow0));
        else
            return new TableRowView(this.context, tableRow, context.getResources().getColor(R.color.tablerow1));
    }


    /**
     * TableRowView 实现表格行的样式
     *
     * @author hellogv
     */
    public class TableRowView extends LinearLayout {
        public TableRowView(Context context, Table.TableRow tableRow, int color) {
            super(context);

            this.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 0; i < tableRow.getSize(); i++) {//逐个格单元添加到行
                Table.TableCell tableCell = tableRow.getCellValue(i);
                LayoutParams layoutParams = new LayoutParams(
                        tableCell.width, tableCell.height);//按照格单元指定的大小设置空间
                if (i == tableRow.getSize() - 1)
                    layoutParams.setMargins(3, 3, 3, 3);//预留空隙制造边框
                else
                    layoutParams.setMargins(3, 3, 1, 3);//预留空隙制造边框
                if (tableCell.type == Table.TableCell.STRING) {//如果格单元是文本内容
                    TextView textCell = new TextView(context);
                    if (color == R.color.tabletitle)
                        textCell.getPaint().setFakeBoldText(true);//加粗
                    textCell.setLines(1);
                    textCell.setGravity(Gravity.CENTER);
                    textCell.setBackgroundColor(color);//背景黑色
                    textCell.setText(String.valueOf(tableCell.value));
                    textCell.setTextColor(Color.BLACK);

                    textCell.setTextSize(15);
                    addView(textCell, layoutParams);
                } else if (tableCell.type == Table.TableCell.IMAGE) {//如果格单元是图像内容
                    ImageView imgCell = new ImageView(context);
                    imgCell.setBackgroundColor(color);//背景黑色
                    imgCell.setImageResource((Integer) tableCell.value);
                    addView(imgCell, layoutParams);
                }
            }
            this.setBackgroundColor(Color.BLACK);//背景白色，利用空隙来实现边框
        }
    }
}