package com.profixerz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int RC_sign = 602;

    private LinearLayout linearLayout;
    private ImageView bgImage;
    ArrayList<GridItem> items;

    private static final int DISPLAY_HEIGHT = 01;
    //private static final int DISPLAY_LENGTH = 02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        linearLayout = (LinearLayout) findViewById(R.id.main_linearLayout1);
        bgImage = (ImageView) findViewById(R.id.main_bg_image);
        bgImage.getLayoutParams().height = getDisplay(DISPLAY_HEIGHT)/4;


        if(mAuth.getCurrentUser()==null){
            signInStart();
        }
        else {
            populateProFixerzServices();
        }


    }

    private void populateProFixerzServices() {

        //Retrieve data from the server
        //Code yet to make

        //bgImage.setImageBitmap();
        GridView gridView = (GridView) findViewById(R.id.main_grid);
        CustomAdapter adapter = new CustomAdapter(this, R.layout.grid_component, items);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    openServicePage(position);
            }
        });


    }

    private void openServicePage(int position) {
        GridItem item = items.get(position);
        /*Intent intent = new Intent(this, ServiceActivity.class)
         startActivity(intent);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_sign){
            if(resultCode == RESULT_OK){
                populateProFixerzServices();
            }
            else if(resultCode ==RESULT_CANCELED){
                Snackbar snackbar = Snackbar.make(linearLayout,R.string.sign_in_failed, Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                signInStart();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        }
    }

    private void signInStart(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, RC_sign);
    }


    // This methods return the display size of the screen
    public int getDisplay(int type){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if(type == DISPLAY_HEIGHT){
            return metrics.heightPixels;
        }else {
            return metrics.widthPixels;
        }
    }


    private class CustomAdapter extends ArrayAdapter<GridItem>{

        private final Context mContext;
        private final int layoutResourceId;
        private ArrayList<GridItem> items;


        /*
        * This class is View holder class and inner class of CustomAdapter. It refer to each element in gridview*/
        private class Component{
            private ImageView image;
            private TextView desc;

            public Component(ImageView image, TextView desc){
                this.image = image;
                this.desc = desc;
            }
        }


        public CustomAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> items){
            super(mContext,layoutResourceId,items);
            this.mContext = mContext;
            this.layoutResourceId = layoutResourceId;
            this.items = items;
        }

        /**
         * Updates grid data and refresh grid items.
         */
        public void setGridData(ArrayList<GridItem> mGridData) {
            this.items = mGridData;
            notifyDataSetChanged();
        }


        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView = view;
            Component component;
            if(gridView == null){

                gridView = new View(mContext);
                gridView = inflater.inflate(R.layout.grid_component,null);
                TextView title = (TextView) findViewById(R.id.grid_element_name);
                ImageView image = (ImageView) findViewById(R.id.grid_element_image);
                component = new Component(image,title);
                gridView.setTag(component);
            }else {
                component = (Component) gridView.getTag();
            }

            // set the item of gridview
            GridItem item = items.get(position);
            component.image.setImageBitmap(item.image);
            component.desc.setText(item.title);

            return gridView;
        }
    }

       /*
       * This class is inner to MainActivity class. This class contains the service retrieved from
       * the server*/
        private class GridItem{
            String title;
            Bitmap image;

            public GridItem(String title, Bitmap image){
                this.image = image;
                this.title = title;
            }
        }



}
