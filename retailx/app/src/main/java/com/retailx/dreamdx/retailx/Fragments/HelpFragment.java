package com.retailx.dreamdx.retailx.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.retailx.dreamdx.retailx.R;
import com.squareup.picasso.Picasso;

public class HelpFragment extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    TextView contentTextOne,contentTextTwo,contentTextThree,titleText;
    ImageView displayImage;
    int flag=0;//0 for help,1 for subscription
    LinearLayout layout;


    // newInstance constructor for creating fragment with arguments
    public static HelpFragment newInstance(int page, String title) {
        HelpFragment fragmentFirst = new HelpFragment();
        Bundle args = new Bundle();
        args.putInt("pageNo", page);
        args.putString("title", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // newInstance constructor for creating fragment with arguments
    public static HelpFragment newInstance(int page, String title,int flag) {
        HelpFragment fragmentFirst = new HelpFragment();
        Bundle args = new Bundle();
        args.putInt("pageNo", page);
        args.putString("title", title);
        args.putInt("flag", flag);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("pageNo", 0);
        title = getArguments().getString("title");
        flag=getArguments().getInt("flag", 0);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        contentTextOne=view.findViewById(R.id.content_1);
        contentTextTwo=view.findViewById(R.id.content_2);
        contentTextThree=view.findViewById(R.id.content_3);
        titleText=view.findViewById(R.id.title_text);
        displayImage=view.findViewById(R.id.display_image);
        layout=view.findViewById(R.id.pageviewer_bg);

        if(flag==0) {
            switch (page) {
                case 0:
                   //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.shopping_girl));
                    Picasso.get().load(R.drawable.shopping_girl).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("Manage products and inventory of");
                    contentTextTwo.setText("your business at your finger tips with \njust a few steps");
                    contentTextThree.setText(" Need more help? Call us on 0771749378");
                    titleText.setText("Get Started!");
                    break;
                case 1:

                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.you_saved));
                    Picasso.get().load(R.drawable.you_saved).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("Your data is secured");
                    contentTextTwo.setText("to the cloud, we have made plans for \nyou already");
                    contentTextThree.setText("Need more help? Call us on 0771749378");
                    titleText.setText("Your business in real time");
                    break;
                case 2: //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.its_easy));
                    Picasso.get().load(R.drawable.its_easy).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("Pair with a bluetooth printer to print a receipt");
                    contentTextTwo.setText("or share receipts via WhasApp,email \nor social media.");
                    contentTextThree.setText("Need more help? Call us on 0771749378");
                    titleText.setText("Print/Share Receipt!");
                    break;

            }
        }else if(flag==1){
            titleText.setText("");
            contentTextTwo.setText("");
            contentTextThree.setText("");

            switch (page) {
                case 0:
                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.shopping_girl));
                    Picasso.get().load(R.drawable.shopping_girl).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("1.Front End");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#32B1AC"));
                    break;
                case 1:

                    displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.you_saved));
                    contentTextOne.setText("Front End and Printer");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#58BF75"));
                    break;

            }
        }else if(flag==2){
            titleText.setText("");
            contentTextTwo.setText("");
            contentTextThree.setText("");

            switch (page) {
                case 0:
                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.shopping_girl));
                    Picasso.get().load(R.drawable.shopping_girl).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("1.Front End");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#32B1AC"));
                    break;
                case 1:

                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.you_saved));
                    Picasso.get().load(R.drawable.you_saved).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("Front End and Printer");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#58BF75"));
                    break;

                case 2:

                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.you_saved));
                    Picasso.get().load(R.drawable.you_saved).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("Front End and Printer");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#1B478A"));
                    break;

            }
        }else if(flag==3){
            titleText.setText("");
            contentTextTwo.setText("");
            contentTextThree.setText("");


            switch (page) {
                case 0:
                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.shopping_girl));
                    Picasso.get().load(R.drawable.shopping_girl).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("Do you know you can print using bluetooth printer ?");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#32B1AC"));
                    break;
                case 1:

                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.you_saved));
                    Picasso.get().load(R.drawable.you_saved).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);

                    contentTextOne.setText("Do you know you get alerted when your stock is finishing ?");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#58BF75"));
                    break;

                case 2:

                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.you_saved));
                    Picasso.get().load(R.drawable.you_saved).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("Do you know you can accept card through your RetailX ?");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#1B478A"));
                    break;

                case 3:

                    //displayImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.you_saved));
                    Picasso.get().load(R.drawable.you_saved).placeholder(R.drawable.no_image)// Place holder image from drawable folder
                            .error(R.drawable.no_image).resize(110, 110).centerCrop()
                            .into(displayImage);
                    contentTextOne.setText("Don't have a printer ? Share the receipt ");
                    contentTextOne.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.parseColor("#BC76C8"));
                    break;

            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
