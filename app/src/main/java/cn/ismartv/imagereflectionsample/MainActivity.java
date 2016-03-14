package cn.ismartv.imagereflectionsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import cn.ismartv.imagereflection.ReflectionTransformationBuilder;
import cn.ismartv.imagereflection.RelectionImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelectionImageView imageView = (RelectionImageView) findViewById(R.id.image);
        imageView.setScore("9.9");
        imageView.setPrice("$10");
//        http://pic2.ooopic.com/01/03/51/25b1OOOPIC19.jpg
//        imageView.setIsHorizontal(true);
        imageView.setFoucsText("神经病!!!!!");
        Transformation mTransformation = new ReflectionTransformationBuilder()
                .setIsHorizontal(true)
                .build();

        Picasso.with(this).load(R.drawable.sample_1).transform(mTransformation).into(imageView);

//        Picasso.with(this).load("http://pic2.qiyipic.com/image/20150712/af/39/v_109289985_m_601_480_270.jpg").into(imageView);
//        ImageUtil2.createReflectedImages(imageView, R.drawable.sample_1);


//        new ImageUtil().createReflectedImages(imageView, R.drawable.image);

    }
}
