package com.funnytoday.project.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.funnytoday.project.calendar.R;
import com.funnytoday.project.calendar.function.WriteModifyActivity;
import com.funnytoday.project.calendar.util.Contact;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sky87 on 2016-11-25.
 */

public class WriteRecyAdapter extends RecyclerView.Adapter<WriteRecyAdapter.ViewHolder> {
    private Context context;
    private Canvas canvas;
    private ArrayList starttime = new ArrayList();
    private ArrayList endtime = new ArrayList();
    private ArrayList title = new ArrayList();
    private ArrayList jsonarray = new ArrayList();
    private Calendar tablename;
    private String Year;
    private String Month;
    private String DAY;
    private String DAY_OF_WEEK;


    public WriteRecyAdapter(Context context, ArrayList starttime, ArrayList endtime, ArrayList title, ArrayList jsonarray, Calendar tablename) {
        this.context = context;
        this.starttime = starttime;
        this.endtime = endtime;
        this.title = title;
        this.jsonarray = jsonarray;
        this.tablename = tablename;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_writerecycle, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        canvas = new Canvas();
        String get_title = String.valueOf(title.get(position)); //title
        String get_starttime = String.valueOf(starttime.get(position)); // 시작 시간
        String get_endtime = String.valueOf(endtime.get(position)); // 종료 시간
        String get_jsonArray = String.valueOf(jsonarray.get(position)); //이미지, 내용
        Year = String.valueOf(tablename.get(Calendar.YEAR));
        Month = String.valueOf(tablename.get(Calendar.MONTH) + 1);
        DAY = String.valueOf(tablename.get(Calendar.DATE));
        DAY_OF_WEEK = String.valueOf(tablename.get(Calendar.DAY_OF_WEEK));
        JSONObject jsnobject = null;
        try {
            jsnobject = new JSONObject(get_jsonArray);
            JSONArray content = jsnobject.getJSONArray("content");
            String contentOne = content.get(0).toString();
            holder.card_content_view.setText(contentOne);

            JSONArray images = jsnobject.getJSONArray("images");
            String image_uri = images.get(0).toString();
            Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(image_uri));
            if (canvas.getMaximumBitmapHeight() / 8 < image_bitmap.getHeight() || canvas.getMaximumBitmapWidth() / 8 < image_bitmap.getWidth()) {
                Picasso.with(context).load(Uri.parse(image_uri)).resize(300, 500).into(holder.card_image);
            } else {
                Picasso.with(context).load(Uri.parse(image_uri)).into(holder.card_image);
            }
        } catch (JSONException e) {
        } catch (IOException e) {
        }
        String replacestarttime = get_starttime.replace("\n" + "hour", "");
        tablename.set(Calendar.HOUR, Integer.parseInt(replacestarttime));
        tablename.getTime();
        Date startDate = Calendar.getInstance().getTime();
        Date endDate = tablename.getTime();
        long duration = (endDate.getTime() - startDate.getTime()) / 1000;
        Log.e("test", String.valueOf(duration / 3600 / 24));
        int subdate = (int) (duration / 3600 / 24);
        if (subdate >= 0) {
            if (subdate == 0) {
                holder.card_sub_time_view.setText("오늘");
            } else {
                holder.card_sub_time_view.setText(subdate + "일 후");
            }
        } else if (subdate < 0) {
            holder.card_sub_time_view.setText(-subdate + "일 전");
        }

        holder.card_title_view.setText(get_title);
       /* holder.card_start_time_view.setText(get_starttime.replace("\n" + "hour", "시 ~"));
        holder.card_end_time_view.setText(" " + get_endtime.replace("\n" + "Hour", "시"));*/
        holder.card_start_time_view.setText(get_starttime + "시 ~");
        holder.card_end_time_view.setText(" " + get_endtime + "시");
        holder.card_position_view.setText(String.valueOf(position + 1));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WriteModifyActivity.class);
                intent.putExtra(Contact.YEAR, Year);
                intent.putExtra(Contact.MONTH, Month);
                intent.putExtra(Contact.DAY, DAY);
                intent.putExtra(Contact.DAY_OF_WEEK, DAY_OF_WEEK);
                intent.putExtra(Contact.POSITION, position);
                context.startActivity(intent);
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                context.sendBroadcast(new Intent(Contact.WRITE_LIST_GONE));
            }
        });
    }


    @Override
    public int getItemCount() {
        return starttime.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView card_image;
        private TextView card_position_view, card_title_view, card_content_view, card_start_time_view, card_end_time_view;
        private TextView card_sub_time_view;
        private CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            card_position_view = (TextView) itemView.findViewById(R.id.card_position_view);
            card_title_view = (TextView) itemView.findViewById(R.id.card_title_view);
            card_content_view = (TextView) itemView.findViewById(R.id.card_content_view);
            card_start_time_view = (TextView) itemView.findViewById(R.id.card_start_time_view);
            card_end_time_view = (TextView) itemView.findViewById(R.id.card_end_time_view);
            card_sub_time_view = (TextView) itemView.findViewById(R.id.card_sub_time_view);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}

