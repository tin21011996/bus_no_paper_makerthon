package com.example.nguyenantin.bususingqrcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private int QRcodeWidth = 500 ;
    private static final String IMAGE_DIRECTORY = "/QRcodeDemonuts";
    private Bitmap bitmap ;
    private static TextView txt_money;
    private ImageView imageView;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws RuntimeException{
        // Inflate the layout for this fragment
        final View homeViewFragment = inflater.inflate(R.layout.fragment_home, container, false);

        EndUser user = SharedPrefManager.getInstance(getContext()).getUser();

        imageView = (ImageView) homeViewFragment.findViewById(R.id.img_CodeQR);
        txt_money = (TextView) homeViewFragment.findViewById(R.id.txt_money);
        DecimalFormat df = new DecimalFormat("###,###,###");
        String formatted = df.format(SharedPrefManager.getInstance(getContext()).getUser().getMoney());
        txt_money.setText(formatted + " VND");
        Toast.makeText(getContext(), "Vui lòng điền tên đăng nhập", Toast.LENGTH_SHORT).show();

        try {
            bitmap = TextToImageEncode(user.getQrcode());
            imageView.setImageBitmap(bitmap);
            String path = saveImage(bitmap);  //give read write permission
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return homeViewFragment;
    }


    public String saveImage(Bitmap myBitmap) throws IOException, Exception, RuntimeException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private Bitmap TextToImageEncode(String Value) throws IOException, Exception, WriterException, RuntimeException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.colorBlack):getResources().getColor(R.color.colorWhite);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public static void refresh(String money) throws RuntimeException {
        DecimalFormat df = new DecimalFormat("###,###,###");
        String formatted = df.format(Integer.parseInt(money));
        txt_money.setText(formatted + " VND");
    }
}
