package com.example.rasterimages.orlov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.lang.reflect.Executable;

public class MainActivity extends AppCompatActivity {

    Bitmap bmp1, bmp2;
    String fileName = "image0";
    int width, height;
    ImageView iv1, iv2;
    SeekBar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        sb = findViewById(R.id.seekBar);

        sb.setProgress(50);

        setImage();
    }

    void setImage() {
        int id = 0;
        id = getResources().getIdentifier(fileName, "drawable", getApplicationContext().getPackageName());

        if (id == 0) {
            Toast.makeText(this, "Image not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        bmp1 = BitmapFactory.decodeResource(getResources(), id);
        width = bmp1.getWidth();
        height = bmp1.getHeight();

        bmp2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        iv1.setImageBitmap(bmp1);
        iv2.setImageBitmap(bmp2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemInput) {

            AlertDialog.Builder bld = new AlertDialog.Builder(this);
            final EditText etDlg = new EditText(this);
            etDlg.setText(fileName);
            bld.setView(etDlg);
            bld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dlg, int x)
                {
                    dlg.cancel();
                }
            });
            bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dlg, int x)
                {
                    fileName = etDlg.getText().toString();
                    setImage();
                }
            });
            bld.setTitle("Write file name");
            bld.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void copy(View v) {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
            int c = bmp1.getPixel(x, y);
            bmp2.setPixel(x, y, c);
        }
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    public void invert(View v) {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int cOld = bmp1.getPixel(x, y);
                int cNew = Color.argb(255, 255 - Color.red(cOld), 255 - Color.green(cOld), 255 - Color.blue(cOld));
                bmp2.setPixel(x, y, cNew);
            }
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    public void gray(View v) {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int cOld = bmp1.getPixel(x, y);
                int r = (Color.red(cOld) + Color.green(cOld) + Color.blue(cOld))/3;
                int g = r, b = r;
                int cNew = Color.argb(255, r, g, b);
                bmp2.setPixel(x, y, cNew);
            }
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    public void blackWhite(View v) {
        gray(null);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int cOld = bmp1.getPixel(x, y), cNew;

                if ((Color.red(cOld) + Color.green(cOld) + Color.blue(cOld))/3 > 255/2) {
                    cNew = Color.argb(255, 255, 255,255);
                }
                else {
                    cNew = Color.argb(255, 0, 0, 0);
                }
                bmp2.setPixel(x, y, cNew);
            }
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    public void levelLight(View v) {
        float value = sb.getProgress()/50f - 1;
        //Toast.makeText(this, String.valueOf(value), Toast.LENGTH_SHORT).show();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int cOld = bmp1.getPixel(x, y);
                int r = (int) (Color.red(cOld) + value * 256),
                g = (int) (Color.green(cOld) + value * 256),
                b = (int) (Color.blue(cOld) + value * 256);
                if (r > 255) r = 255; if (r < 0) r = 0;
                if (g > 255) g = 255; if (g < 0) g = 0;
                if (b > 255) b = 255; if (b < 0) b = 0;
                int cNew = Color.argb(255, r, g, b);
                bmp2.setPixel(x, y, cNew);
            }
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    public void mirrorX(View v) {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int c = bmp1.getPixel(width - 1 - x, y);
                bmp2.setPixel(x, y, c);
            }
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    public void mirrorY(View v) {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int c = bmp1.getPixel(x, height - 1 - y);
                bmp2.setPixel(x, y, c);
            }
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    public void contrast(View v) {
        float value = sb.getProgress()/100f;
        //Toast.makeText(this, String.valueOf(value), Toast.LENGTH_SHORT).show();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int cOld = bmp1.getPixel(x, y);
                int r, g, b;
                if ((Color.red(cOld) + Color.green(cOld) + Color.blue(cOld))/3 > 255/2) {
                    r = (int) (Color.red(cOld) + value * 64);
                    g = (int) (Color.green(cOld) + value * 64);
                    b = (int) (Color.blue(cOld) + value * 64);
                }
                else {
                    r = (int) (Color.red(cOld) - value * 64);
                    g = (int) (Color.green(cOld) - value * 64);
                    b = (int) (Color.blue(cOld) - value * 64);
                }
                if (r > 255) r = 255; if (r < 0) r = 0;
                if (g > 255) g = 255; if (g < 0) g = 0;
                if (b > 255) b = 255; if (b < 0) b = 0;
                int cNew = Color.argb(255, r, g, b);
                bmp2.setPixel(x, y, cNew);
            }
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }
}