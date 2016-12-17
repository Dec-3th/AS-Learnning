package com.jirdy.listview.model;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jirdy.listview.R;

/**
 * Created by Administrator on 2016/7/14.
 */
public class Table
{
    /**
     * TableRow 实现表格的行
     *
     * @author hellogv
     */
    static public class TableRow {
        private long bookId;
        private TableCell[] cell;

        public TableRow(TableCell[] cell) {
            this.cell = cell;
        }

        public int getSize() {
            return cell.length;
        }

        public TableCell getCellValue(int index) {
            if (index >= cell.length)
                return null;
            return cell[index];
        }

        public long getBookId() {
            return bookId;
        }

        public void setBookId(long bookId) {
            this.bookId = bookId;
        }

        public String toString(){
            String tableRow = "{";
            for(int i=0;i<getSize();i++) {
                tableRow += getCellValue(i).value + ", ";
                if(i==getSize()-1)
                    tableRow += getCellValue(i).value + "}";
            }
            return tableRow;
        }
    }

    /**
     * TableCell 实现表格的格单元
     *
     * @author hellogv
     */
    static public class TableCell {
        static public final int STRING = 0;
        static public final int IMAGE = 1;
        public Object value;
        public int width;
        public int height;
        public int type;

        public TableCell(Object value, int width, int height, int type) {
            this.value = value;
            this.width = width;
            this.height = height;
            this.type = type;
        }
    }
}
