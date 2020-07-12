/*
 * Copyright (C)  2017-2018 Tran Le Duy
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.duy.text.converter.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.duy.text.converter.R;
import com.duy.text.converter.clipboard.ClipboardUtil;
import com.duy.text.converter.pro.menu.fragments.OnTextSelectedListener;
import com.duy.text.converter.utils.ShareManager;

import java.util.ArrayList;
import java.util.Locale;

public class DecodeResultAdapter extends RecyclerView.Adapter<DecodeResultAdapter.ViewHolder> {
    private static final String TAG = "DecodeResultAdapter";
    private final Context context;
    private boolean processText;
    private ArrayList<DecodeItem> mDecodeItems = new ArrayList<>();
    private OnTextSelectedListener listener;

    public DecodeResultAdapter(Context context, boolean processText) {
        this.context = context;
        this.processText = processText;
    }

    public void add(DecodeItem decodeItem) {
        mDecodeItems.add(decodeItem);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_decode_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DecodeItem item = mDecodeItems.get(position);
        holder.txtResult.setText(item.getResult());
        holder.txtTitle.setText(item.getName());
        if (item.getMax() > 0) {
            Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
            holder.progressBar.setMax(item.getMax());
            holder.progressBar.setProgress(item.getConfident());

            int percent = (int) ((float) item.getConfident() / (float) item.getMax() * 100f);
            holder.txtTitle.append(String.format(Locale.US, " %d%%", percent));
        }

        final String str = item.getResult();
        if (processText) {
            if (holder.imgCopy != null) holder.imgCopy.setVisibility(View.GONE);
            if (holder.imgShare != null) holder.imgShare.setVisibility(View.GONE);
            if (holder.shareMsg != null) holder.shareMsg.setVisibility(View.GONE);
            holder.rootView.setOnClickListener(v -> {
                if (listener != null) listener.onTextSelected(str);
            });
        } else {
            if (holder.imgCopy != null) holder.imgCopy.setVisibility(View.VISIBLE);
            if (holder.imgShare != null) holder.imgShare.setVisibility(View.VISIBLE);
            if (holder.shareMsg != null) holder.shareMsg.setVisibility(View.VISIBLE);
            if (holder.imgShare != null) {
                holder.imgShare.setOnClickListener(v ->
                        ShareManager.share(str, context));
            }
            if (holder.imgCopy != null) {
                holder.imgCopy.setOnClickListener(v ->
                        ClipboardUtil.setClipboard(context, str));
            }
            if (holder.shareMsg != null) {
                holder.shareMsg.setOnClickListener(v ->
                        ShareManager.shareMessenger(str, context));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDecodeItems.size();
    }

    public void setListener(OnTextSelectedListener listener) {
        this.listener = listener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtResult, txtTitle;
        ProgressBar progressBar;
        View imgCopy, imgShare, shareMsg, rootView;

        ViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            txtResult = itemView.findViewById(R.id.txt_result);
            txtTitle = itemView.findViewById(R.id.txt_name);
            imgCopy = itemView.findViewById(R.id.btn_copy);
            imgShare = itemView.findViewById(R.id.btn_share);
            shareMsg = itemView.findViewById(R.id.img_share_msg);
            rootView = itemView.findViewById(R.id.root_view);
        }

    }

    public static class DecodeItem {
        String name;
        String result;
        int max, confident;

        public DecodeItem(String name, String result) {
            this.name = name;
            this.result = result;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getConfident() {
            return confident;
        }

        public void setConfident(int confident) {
            this.confident = confident;
        }

        public String getResult() {
            return result;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
