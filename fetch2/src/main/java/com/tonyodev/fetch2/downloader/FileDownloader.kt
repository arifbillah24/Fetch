package com.tonyodev.fetch2.downloader

import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2core.DownloadBlock

interface FileDownloader : Runnable {

    var interrupted: Boolean
    var terminated: Boolean
    var completedDownload: Boolean
    var delegate: Delegate?
    val download: Download

    interface Delegate {

        fun onStarted(download: Download,
                      etaInMilliseconds: Long,
                      downloadedBytesPerSecond: Long)

        fun onDownloadBlockUpdated(download: Download,
                                   downloadBlock: DownloadBlock,
                                   totalBlocks: Int)

        fun onProgress(download: Download,
                       etaInMilliSeconds: Long,
                       downloadedBytesPerSecond: Long)

        fun onError(download: Download)

        fun onComplete(download: Download)

        fun saveDownloadProgress(download: Download)

    }

}