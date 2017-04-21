package com.eastblue.cisuba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastblue.cisuba.Model.BoardModel;
import com.eastblue.cisuba.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by PJC on 2017-02-28.
 */

public class BoardAdapter extends BaseExpandableListAdapter {

    public ArrayList<BoardModel> mList;
    public Context mContext;

    public BoardAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void addItem(BoardModel item) {
        mList.add(item);
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewBinderGroup viewBinderGroup = null;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_board_group, parent, false);
            viewBinderGroup = new ViewBinderGroup(convertView);
            convertView.setTag(viewBinderGroup);
        } else {
            viewBinderGroup = (ViewBinderGroup) convertView.getTag();
        }

        if(isExpanded) {
            Picasso.with(mContext).load(R.drawable.btn_unfold).fit().into(viewBinderGroup.imvArrow);
        } else {
            Picasso.with(mContext).load(R.drawable.btn_fold).fit().into(viewBinderGroup.imvArrow);
        }

        viewBinderGroup.bindObject(mList.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewBinderChild viewBinderChild = null;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_board_child, parent, false);
            viewBinderChild = new ViewBinderChild(convertView);
            convertView.setTag(viewBinderChild);
        } else {
            viewBinderChild = (ViewBinderChild) convertView.getTag();
        }

        viewBinderChild.bindObject(mList.get(groupPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public class ViewBinderGroup {
        TextView tvType;
        TextView tvTitle;
        ImageView imvArrow;

        public ViewBinderGroup(View v) {
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvType = (TextView) v.findViewById(R.id.tv_type);
            imvArrow = (ImageView) v.findViewById(R.id.imv_arrow);
        }

        public void bindObject(BoardModel boardModel) {
            tvTitle.setText(boardModel.title);
            if(boardModel.divsion == 0) {
                tvType.setText("공지사항");
            } else if(boardModel.divsion == 1) {
                tvType.setText("Q");
            }
        }
    }

    public class ViewBinderChild {
        TextView tvContent;

        public ViewBinderChild(View v) {
            tvContent = (TextView) v.findViewById(R.id.tv_content);
        }

        public void bindObject(BoardModel boardModel) {
            tvContent.setText(boardModel.content);
        }
    }
}
