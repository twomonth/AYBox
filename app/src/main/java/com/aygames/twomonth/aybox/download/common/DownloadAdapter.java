package com.aygames.twomonth.aybox.download.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aygames.twomonth.aybox.R;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.SoftReference;
import java.sql.SQLException;

import cn.woblog.android.downloader.domain.DownloadInfo;

import static cn.woblog.android.downloader.DownloadService.downloadManager;
import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_COMPLETED;
import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_REMOVED;
import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_WAIT;

/**
 * Created by renpingqing on 17/3/1.
 */

public class DownloadAdapter extends
        BaseRecyclerViewAdapter<DownloadInfo, DownloadAdapter.ViewHolder> {

  private DBController dbController;

  public DownloadAdapter(Context context) {
    super(context);
    try {
      dbController = DBController.getInstance(context.getApplicationContext());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new DownloadAdapter.ViewHolder(LayoutInflater.from(context).inflate(
        R.layout.item_download, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

    DownloadInfo data = getData(position);
    try {
      MyBusinessInfLocal myDownloadInfoById = dbController
          .findMyDownloadInfoById(data.getUri().hashCode());
      if (myDownloadInfoById != null) {
        holder.bindBaseInfo(myDownloadInfoById);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    holder.bindData(data, position, context);

//    holder.itemView.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        if (onItemClickListener != null) {
//          onItemClickListener.onItemClick(position);
//        }
//      }
//    });
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    private final ImageView iv_icon;
    private final TextView tv_size;
//    private final TextView tv_status;
    private final ProgressBar pb;
    private final TextView tv_name;
    private final Button bt_action;
    private DownloadInfo downloadInfo;

    public ViewHolder(View view) {
      super(view);
      itemView.setClickable(true);
      iv_icon = (ImageView) view.findViewById(R.id.ib_delete);
      tv_size = (TextView) view.findViewById(R.id.tv_jindu);
//      tv_status = (TextView) view.findViewById(R.id.tv_status);
      pb = (ProgressBar) view.findViewById(R.id.progressbar);
      tv_name = (TextView) view.findViewById(R.id.tv_name);
      bt_action = (Button) view.findViewById(R.id.bt_download);
    }

    @SuppressWarnings("unchecked")
    public void bindData(final DownloadInfo data, int position, final Context context) {
//      Glide.with(context).load(data.getIcon()).into(iv_icon);
//      tv_name.setText(data.getName());

      // Get download task status.
      downloadInfo = data;

      // Set a download listener
      if (downloadInfo != null) {
        downloadInfo
            .setDownloadListener(
                new MyDownloadListener(new SoftReference(DownloadAdapter.ViewHolder.this)) {
                  //  Call interval about one second.
                  @Override
                  public void onRefresh() {
                    notifyDownloadStatus();

                    if (getUserTag() != null && getUserTag().get() != null) {
                      DownloadAdapter.ViewHolder viewHolder = (DownloadAdapter.ViewHolder) getUserTag()
                          .get();
                      viewHolder.refresh();
                    }
                  }
                });

      }

      refresh();

//      Download button
      bt_action.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          if (downloadInfo != null) {

            switch (downloadInfo.getStatus()) {
              case DownloadInfo.STATUS_NONE:
              case DownloadInfo.STATUS_PAUSED:
              case DownloadInfo.STATUS_ERROR:

                //resume downloadInfo
                downloadManager.resume(downloadInfo);
                break;
              case DownloadInfo.STATUS_DOWNLOADING:
              case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
              case STATUS_WAIT:
                //pause downloadInfo
                downloadManager.pause(downloadInfo);
                break;
              case DownloadInfo.STATUS_COMPLETED:
                downloadManager.remove(downloadInfo);
                break;
            }
          }
        }
      });
      iv_icon.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          downloadManager.remove(downloadInfo);
        }
      });

    }

    private void refresh() {
      if (downloadInfo == null) {
        tv_size.setText("");
        pb.setProgress(0);
        bt_action.setText("下载");
        tv_name.setText("没有资源");
      } else {
        switch (downloadInfo.getStatus()) {
          case DownloadInfo.STATUS_NONE:
            bt_action.setText("下载");
            tv_name.setText("没有资源");
            break;
          case DownloadInfo.STATUS_PAUSED:
          case DownloadInfo.STATUS_ERROR:
            bt_action.setText("继续");
            tv_name.setText("暂停");
            try {
              pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
            } catch (Exception e) {
              e.printStackTrace();
            }
            tv_size.setText(FileUtil.formatFileSize(downloadInfo.getProgress()) + "/" + FileUtil
                .formatFileSize(downloadInfo.getSize()));
            break;

          case DownloadInfo.STATUS_DOWNLOADING:
          case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
            bt_action.setText("暂停");
            try {
              pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
            } catch (Exception e) {
              e.printStackTrace();
            }
            tv_size.setText(FileUtil.formatFileSize(downloadInfo.getProgress()) + "/" + FileUtil
                .formatFileSize(downloadInfo.getSize()));
            tv_name.setText("正在下载");
            break;
          case STATUS_COMPLETED:
            bt_action.setText("删除");
            try {
              pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
            } catch (Exception e) {
              e.printStackTrace();
            }
            tv_size.setText(FileUtil.formatFileSize(downloadInfo.getProgress()) + "/" + FileUtil
                .formatFileSize(downloadInfo.getSize()));
            tv_name.setText("成功");
            Log.i("下载成功文件路径",downloadInfo.getPath());
//            Intent install = new Intent(Intent.ACTION_VIEW);
//            install.addCategory(Intent.CATEGORY_DEFAULT);
//            install.setDataAndType(Uri.parse("file://" +downloadInfo.getPath()), "application/vnd.android.package-archive");
//            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(install);
            publishDownloadSuccessStatus();
            break;
          case STATUS_REMOVED:
            tv_size.setText("");
            pb.setProgress(0);
            bt_action.setText("下载");
            tv_name.setText("没有资源");
            publishDownloadSuccessStatus();
          case STATUS_WAIT:
            tv_size.setText("");
            pb.setProgress(0);
            bt_action.setText("等待");
            tv_name.setText("等待");
            break;
        }

      }
    }

    private void publishDownloadSuccessStatus() {
      //publish download success info.
      EventBus.getDefault().post(new DownloadStatusChanged(downloadInfo));
    }

    public void bindBaseInfo(MyBusinessInfLocal myBusinessInfLocal) {
      Glide.with(context).load(myBusinessInfLocal.getIcon()).into(iv_icon);
      tv_name.setText(myBusinessInfLocal.getName());
    }

    private void notifyDownloadStatus() {

      if (downloadInfo.getStatus() == STATUS_REMOVED) {
        try {
          dbController.deleteMyDownloadInfo(downloadInfo.getUri().hashCode());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
