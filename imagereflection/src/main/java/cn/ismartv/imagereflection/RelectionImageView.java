package cn.ismartv.imagereflection;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by huaijie on 1/19/16.
 */
public class RelectionImageView extends ImageView {
    private static final String TAG = "RelectionImageView";


    private boolean isHorizontal;
    private String price;
    private String score;

    private int lrPaddingFlag;


    private Bitmap targetBitmap;

    private int leftFrom;
    private int topFrom;
    private String foucsText;
    private int focusTitleHeight;
    private int focusTitleTextsize;
    private int focusTitleBackground;
    private int leftTopMarkTextSize;
    private int rightTopMarkTextSize;
    private int leftTopMarkWidth;
    private int rightTopMarkWidth;


    public void setFoucsText(String foucsText) {
        this.foucsText = foucsText;
    }

    public void setIsHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public RelectionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RelectionImageView);
        leftTopMarkWidth = (int) typedArray.getDimension(R.styleable.RelectionImageView_left_top_mark_width, 0);
        rightTopMarkWidth = (int) typedArray.getDimension(R.styleable.RelectionImageView_right_top_mark_width, 0);
        focusTitleHeight = (int) typedArray.getDimension(R.styleable.RelectionImageView_focus_title_height, 0);
        focusTitleTextsize = (int) typedArray.getDimension(R.styleable.RelectionImageView_focus_title_textSize, 0);
        focusTitleBackground = typedArray.getColor(R.styleable.RelectionImageView_focus_title_background, 0);
        leftTopMarkTextSize = (int) typedArray.getDimension(R.styleable.RelectionImageView_left_top_mark_textSize, 0);
        rightTopMarkTextSize = (int) typedArray.getDimension(R.styleable.RelectionImageView_right_top_mark_textSize, 0);
        typedArray.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (lrPaddingFlag == 0) {
            super.onMeasure(widthMeasureSpec + getPaddingLeft() + getPaddingRight(), heightMeasureSpec + getPaddingTop() + getPaddingBottom());
        } else {
            super.onMeasure(widthMeasureSpec + getPaddingLeft(), heightMeasureSpec + getPaddingTop() + getPaddingBottom());
        }


    }


    public void setLrPaddingFlag(int lrPaddingFlag) {
        this.lrPaddingFlag = lrPaddingFlag;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setScore(String score) {
        this.score = score;
    }


    @Override
    public void invalidate() {
        super.invalidate();
        BitmapDrawable sourceBitmapDrawable = ((BitmapDrawable) getDrawable());

        if (sourceBitmapDrawable != null) {
            targetBitmap = sourceBitmapDrawable.getBitmap();
        }
//        targetBitmap  = getTargetBitmap();

    }

    @Override
    protected void onDraw(Canvas rawCanvas) {
        super.onDraw(rawCanvas);

        if (targetBitmap != null) {
            leftFrom = getPaddingLeft();
            topFrom = getPaddingTop();
            int imageWidth = getLayoutParams().width;
            int imageHeight = getLayoutParams().height;

            float xRate = imageWidth / (float) targetBitmap.getWidth();
            float yRate = imageHeight / (float) targetBitmap.getHeight();
            Matrix testMatrix = new Matrix();
            testMatrix.preScale(xRate, yRate);
            targetBitmap = Bitmap.createBitmap(targetBitmap, 0, 0, targetBitmap.getWidth(), targetBitmap.getHeight(), testMatrix, false);

            rawCanvas.drawBitmap(targetBitmap, leftFrom, topFrom, null);
            if (!TextUtils.isEmpty(score)) {
                Bitmap scoreMarkBitmap = getScoreMarkBitmap(score);
                rawCanvas.drawBitmap(scoreMarkBitmap, leftFrom, topFrom, null);
            }

            if (!TextUtils.isEmpty(price)) {
                Bitmap priceMarkBitmap = getPriceMarkBitmap(price);
                rawCanvas.drawBitmap(priceMarkBitmap, leftFrom + targetBitmap.getWidth() - priceMarkBitmap.getWidth(), topFrom, null);
            }

            if (!TextUtils.isEmpty(foucsText)) {
                drawText(rawCanvas, foucsText);
            }
        }

    }

    private void drawText(Canvas canvas, String foucsText) {
        Rect targetRect = new Rect(leftFrom, targetBitmap.getHeight() + topFrom - focusTitleHeight, targetBitmap.getWidth() + leftFrom, targetBitmap.getHeight() + topFrom);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(focusTitleTextsize);
        paint.setColor(focusTitleBackground);
        canvas.drawRect(targetRect, paint);
        paint.setColor(Color.WHITE);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawText(foucsText, targetRect.centerX(), baseline, paint);
    }


    private Bitmap getScoreMarkBitmap(String score) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.score_mark_icon).copy(Bitmap.Config.ARGB_8888, true);
        float xRate = leftTopMarkWidth / (float) bitmap.getWidth();
        float yRate = leftTopMarkWidth / (float) bitmap.getHeight();
        Matrix testMatrix = new Matrix();
        testMatrix.preScale(xRate, yRate);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), testMatrix, false);

        Canvas canvas = new Canvas(bitmap);

        Rect targetRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(leftTopMarkTextSize);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(targetRect, paint);
        paint.setColor(Color.WHITE);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);


        canvas.drawText(score, targetRect.centerX(), baseline, paint);

        return bitmap;
    }

    private Bitmap getPriceMarkBitmap(String price) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.price_mark_icon).copy(Bitmap.Config.ARGB_8888, true);

        float xRate = rightTopMarkWidth / (float) bitmap.getWidth();
        float yRate = rightTopMarkWidth / (float) bitmap.getHeight();
        Matrix testMatrix = new Matrix();
        testMatrix.preScale(xRate, yRate);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), testMatrix, false);

        Canvas canvas = new Canvas(bitmap);

        Rect targetRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(rightTopMarkTextSize);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(targetRect, paint);
        paint.setColor(Color.WHITE);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 3;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);


        canvas.rotate(45, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        canvas.drawText(price, targetRect.centerX(), baseline, paint);
        return bitmap;
    }


//    private Bitmap getTargetBitmap() {
//        leftFrom = getPaddingLeft();
//        topFrom = getPaddingTop();
//        BitmapDrawable sourceBitmapDrawable = ((BitmapDrawable) getDrawable());
//
//        if (sourceBitmapDrawable != null) {
//            if (isHorizontal) {
//                Bitmap sourceBitmap = sourceBitmapDrawable.getBitmap();
//                int sourceBitmapHeight = sourceBitmap.getHeight();
//                int sourceBitmapWidth = sourceBitmap.getWidth();
//
//                int imageWidth = getLayoutParams().width;
//                int imageHeight = getLayoutParams().height;
//
//                LinearGradient shader = new LinearGradient(0, sourceBitmap.getHeight(), 0, 2 * sourceBitmapHeight, 0x00000000, 0xff000000, Shader.TileMode.MIRROR);
//
//                float bitmapWithReflectionHeight = sourceBitmapWidth * imageHeight / (float) imageWidth;
//                Matrix matrix = new Matrix();
//                matrix.preScale(1, -1); // 实现图片的反转
//                Bitmap reflectionImage = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmapWidth, sourceBitmapHeight, matrix, false);
//                Bitmap bitmapWithReflection = Bitmap.createBitmap(sourceBitmapWidth, (int) bitmapWithReflectionHeight, Bitmap.Config.RGB_565); // 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍
//                Paint paint = new Paint();
//                PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
//
//
//                Bitmap myBitmap = Bitmap.createBitmap(sourceBitmapWidth, (int) bitmapWithReflectionHeight, Bitmap.Config.RGB_565);
//                Canvas canvas = new Canvas(myBitmap);
//                canvas.drawBitmap(sourceBitmap, 0, 0, null);
//                canvas.drawBitmap(reflectionImage, 0, sourceBitmapHeight, null);
//                paint.setShader(shader); // 绘制
//                paint.setXfermode(porterDuffXfermode);
//                canvas.drawRect(0, sourceBitmapHeight, sourceBitmapWidth, 2 * sourceBitmapHeight, paint);
//                paint.reset();
//                paint.setColor(Color.BLACK);
//                canvas.drawRect(0, 2 * sourceBitmapHeight, sourceBitmapWidth, bitmapWithReflection.getHeight(), paint);
//
//
//                float xRate = imageWidth / (float) myBitmap.getWidth();
//                float yRate = imageHeight / (float) myBitmap.getHeight();
//                Matrix testMatrix = new Matrix();
//                testMatrix.preScale(xRate, yRate);
//                return Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), testMatrix, false);
//            } else {
//                Bitmap sourceBitmap = sourceBitmapDrawable.getBitmap();
//                int imageWidth = getLayoutParams().width;
//                int imageHeight = getLayoutParams().height;
//
//                float xRate = imageWidth / (float) sourceBitmap.getWidth();
//                float yRate = imageHeight / (float) sourceBitmap.getHeight();
//                Matrix testMatrix = new Matrix();
//                testMatrix.preScale(xRate, yRate);
//                return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), testMatrix, false);
//            }
//        } else {
//            return null;
//        }
//    }
    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
//    private  float getFontLeading(Paint paint) {
//        Paint.FontMetrics fm = paint.getFontMetrics();
//        return fm.leading - fm.ascent;
//    }
}
