package com.aygames.twomonth.aybox.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.aygames.twomonth.aybox.receiver.HiddenWaiter;
import com.aygames.twomonth.aybox.util.Utils;


/**
 * http://blog.csdn.net/liuzg1220
 *
 * @author henry
 *
 */
public class Service1 extends Service {

	HandlerThread waiterthread;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					startService2();
					break;
				default:
					break;
			}

		};
	};

	/**
	 * 使用aidl 启动Service2
	 */
	private StrongService startS2 = new StrongService.Stub() {
		@Override
		public void stopService() throws RemoteException {
			Intent i = new Intent(getBaseContext(), Service2.class);
			getBaseContext().stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
			Intent i = new Intent(getBaseContext(), Service2.class);
			getBaseContext().startService(i);
		}
	};

	/**
	 * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动Service2
	 */
	@Override
	public void onTrimMemory(int level) {
		/*
		 * 启动service2
		 */
		startService2();

	}

	@Override
	public void onCreate() {
		Toast.makeText(Service1.this, "Service1 正在启动...", Toast.LENGTH_SHORT)
				.show();
		startwaiter();
		startService2();

		/*
		 * 此线程用监听Service2的状态
		 */
		new Thread() {
			public void run() {
				while (true) {
					boolean isRun = Utils.isServiceWork(Service1.this,
							"com.example.twomonth.strongservice.Service2");
					if (!isRun) {
						Message msg = Message.obtain();
						msg.what = 1;
						handler.sendMessage(msg);
					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	/**
	 * 判断Service2是否还在运行，如果不是则启动Service2
	 */
	private void startService2() {
		boolean isRun = Utils.isServiceWork(Service1.this,
				"com.example.twomonth.strongservice.Service2");
		if (isRun == false) {
			try {
				startS2.startService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) startS2;
	}

	void startwaiter() {
		Log.d("test", "尝试启动隐藏的等待线程");
		try {
			waiterthread = new HandlerThread("hiddenwaiterthread");
			waiterthread.start();
			Looper customlooper = waiterthread.getLooper();
			Handler waiterhandler = new Handler(customlooper);
			IntentFilter myfilter = new IntentFilter();
			myfilter.addAction("android.intent.action.SCREEN_OFF");
			myfilter.addAction("android.intent.action.SCREEN_ON");
			HiddenWaiter evilreceiver = new HiddenWaiter();
			registerReceiver(evilreceiver, myfilter, null, waiterhandler);//注册接收器

		} catch (Exception e) {
			Log.d("test", "启动隐藏等待线程失败 ..\n" + e.toString());
		}
//		HiddenWaiter hiddenWaiter = new HiddenWaiter();
//		IntentFilter screenStatusIF = new IntentFilter();
//		screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
//		screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
//		registerReceiver(hiddenWaiter, screenStatusIF);
	}
}
