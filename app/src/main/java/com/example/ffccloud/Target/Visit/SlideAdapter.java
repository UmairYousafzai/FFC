package com.example.ffccloud.Target.Visit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.example.ffccloud.R;


class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    public SlideAdapter(Context context){
        this.context=context;
    }

    //Array
    public int[] list_images={
            R.drawable.img_brochure1,
            R.drawable.img_brochure2
    };

    @Override
    public int getCount() {
        return list_images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view==(LinearLayout)obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide,container,false);

        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.slidelinearlayout);
        ImageView img = (ImageView)view.findViewById(R.id.slideimg);


        img.setImageResource(list_images[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

}
