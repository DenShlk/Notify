package com.example.notify;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

	private NotificationManager notificationManager;
	private static final int NOTIFY_ID = 101;
	private static final String CHANNEL_ID = "CHANNEL_ID";
	private Button notif_but, photo_but;

	private static final int CAMERA_REQUEST = 0;
	private ImageView imageView;
	private Bitmap bmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notif_but = findViewById(R.id.notif_but);
		notif_but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
						0, notificationIntent,
						PendingIntent.FLAG_CANCEL_CURRENT);

				NotificationCompat.Builder builder =
						new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
								.setSmallIcon(R.drawable.ic_launcher_foreground)
								.setPriority(NotificationCompat.PRIORITY_DEFAULT)
								.setContentIntent(pendingIntent)
								.setLargeIcon(BitmapFactory.decodeResource(getResources(),
										R.drawable.image)) // большая картинка
								// большая картинка из ресурсов
								.addAction(R.drawable.ic_launcher_foreground, "вжух!", pendingIntent)
								.setStyle(new NotificationCompat.BigPictureStyle()
										.bigPicture(bmp));

				NotificationManagerCompat notificationManager =
						NotificationManagerCompat.from(MainActivity.this);
				notificationManager.notify(NOTIFY_ID, builder.build());
			}
		});

		photo_but = findViewById(R.id.photo_but);
		photo_but.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});

		imageView = findViewById(R.id.imageView);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			// Фотка сделана, извлекаем картинку
			bmp = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(bmp);
		}
	}

	public static void createChannelIfNeeded(NotificationManager manager) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
			manager.createNotificationChannel(notificationChannel);
		}
	}
}
