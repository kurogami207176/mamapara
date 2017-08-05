package com.tinybrownmonkey.mamapara;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;

import java.util.logging.FileHandler;

/**
 * Created by alaguipo on 5/08/2017.
 */

public class FileUtil {
    public static void shareScreenshot(Activity activity, byte[] pixels) {
        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
        FileHandle fileHandler = Gdx.files.external("tmp.png");
        PixmapIO.writePNG(fileHandler, pixmap);
        pixmap.dispose();

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileHandler.file()));
        shareIntent.setType("image/*");
        activity.startActivity(Intent.createChooser(shareIntent, activity.getResources().getText(R.string.send_to)));

    }
}
