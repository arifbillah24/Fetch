package com.tonyodev.fetch2rx

import android.content.Context
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2.exception.FetchException
import com.tonyodev.fetch2.fetch.FetchModulesBuilder
import com.tonyodev.fetch2.util.DEFAULT_INSTANCE_NAMESPACE
import com.tonyodev.fetch2.Status
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Func

/**
 * A light weight file download manager for Android with Rx features.
 * Features: Background downloading,
 *           Queue based Priority downloading,
 *           Pause & Resume downloads,
 *           Network specific downloading and more...
 * */
interface RxFetch : Fetch {

    /**
     * Queues a request for downloading. If Fetch fails to enqueue the request,
     * the returned convertible object when converted to an observable or flowable and subscribed to,
     * will throw an exception indicating the error.
     * Errors that may cause Fetch to fail the enqueue are :
     * 1. No storage space on the device.
     * 2. Fetch is already managing the same request. This means that a request with the same url
     * and file name is already managed.
     * It would be best to check if Fetch is managing a download before enqueuing.
     * @param request Download Request. Fetch may update a request depending on the initial request's Enqueue Action.
     *             Update old request references with the returned request.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable
     * */
    fun enqueue(request: Request): Convertible<Request>

    /**
     * Queues a request list for downloading. If Fetch fails to enqueue a
     * download request because an error occurred, all other request in the list will fail and the
     * the returned convertible object when converted to an observable or flowable and subscribed to,
     * will throw an exception indicating the error.
     * Errors that may cause Fetch to fail the enqueue are :
     * 1. No storage space on the device.
     * 2. Fetch is already managing the same request. This means that a request with the same url
     * and file name is already managed.
     * It would be best to check if Fetch is managing a download before enqueuing.
     * @param requests Download Requests. Fetch may update a request depending on the initial request's Enqueue Action.
     *             Update old request references with the returned requests.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable
     * */
    fun enqueue(requests: List<Request>): Convertible<List<Request>>

    /**
     * Adds a completed download to Fetch for management. If Fetch is already managing another download with the same file as this completed download's
     * file, Fetch will replace the already managed download with this completed download.
     * @param completedDownload Completed Download
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the result as on observable or
     * flowable
     * */
    fun addCompletedDownload(completedDownload: CompletedDownload): Convertible<Download>

    /**
     * Adds a list of completed downloads to Fetch for management. If Fetch is already managing another download with the same file as this completed download's
     * file, Fetch will replace the already managed download with this completed download.
     * @param completedDownloads Completed Downloads list
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable
     * */
    fun addCompletedDownloads(completedDownloads: List<CompletedDownload>): Convertible<List<Download>>

    /** Updates an existing request.
     * @see com.tonyodev.fetch2.Request for more details.
     * @param oldRequestId Id of existing request/download
     * @param newRequest Request object
     * @param func Successful callback that the download will be returned on.
     * @param func2 Failed callback that the error will be returned on.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable
     * */
    fun updateRequest(oldRequestId: Int, newRequest: Request): Convertible<Download>

    /**
     * Gets all downloads managed by this instance of Fetch.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable*/
    fun getDownloads(): Convertible<List<Download>>

    /**
     * Gets the downloads which match an id in the list. Only successful matches will be returned.
     * @param idList Id list to perform id query against.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable*/
    fun getDownloads(idList: List<Int>): Convertible<List<Download>>

    /**
     * Gets the download which has the specified id. If the download
     * does not exist the returned convertible object when converted
     * to an observable or flowable and subscribed to, will throw an exception indicating the
     * error.
     * @param id Download id
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable*/
    fun getDownload(id: Int): Convertible<Download>

    /**
     * Gets all downloads in the specified group.
     * @param groupId group id to query.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable*/
    fun getDownloadsInGroup(groupId: Int): Convertible<List<Download>>

    /**
     * Gets all downloads with a specific status.
     * @see com.tonyodev.fetch2.Status
     * @param status Status to query.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable*/
    fun getDownloadsWithStatus(status: Status): Convertible<List<Download>>

    /**
     * Gets all downloads in a specific group with a specific status.
     * @see com.tonyodev.fetch2.Status
     * @param groupId group id to query.
     * @param status Status to query.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable*/
    fun getDownloadsInGroupWithStatus(groupId: Int, status: Status): Convertible<List<Download>>

    /**
     * Gets all downloads containing the identifier.
     * @param identifier identifier.
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable
     * */
    fun getDownloadsByRequestIdentifier(identifier: Long): Convertible<List<Download>>

    /**
     * Gets the list of download blocks belonging to a download. List may be empty if
     * blocks could not be found for the download id or download has never been processed.
     * @param downloadId: Download ID
     * @throws FetchException if this instance of Fetch has been closed.
     * @return A Convertible object that allows you to get the results as on observable or
     * flowable
     * */
    fun getDownloadBlocks(downloadId: Int): Convertible<List<DownloadBlock>>

    /**
     * RX Fetch implementation class. Use this Singleton to get instances of RxFetch or Fetch.
     * */
    companion object Impl {

        /**
         * Sets the default Configuration settings on the default Fetch instance.
         * @param context context
         * */
        fun setDefaultInstanceConfiguration(context: Context) {
            Fetch.setDefaultInstanceConfiguration(context)
        }

        /**
         * Sets the default Configuration settings on the default Fetch instance.
         * @param fetchConfiguration custom Fetch Configuration
         * */
        fun setDefaultInstanceConfiguration(fetchConfiguration: FetchConfiguration) {
            Fetch.setDefaultInstanceConfiguration(fetchConfiguration)
        }

        /**
         * Get the default Fetch Configuration set with setDefaultInstanceConfiguration(fetchConfiguration: FetchConfiguration)
         * or setDefaultInstanceConfiguration(context: Context)
         * @throws FetchException if default FetchConfiguration is not set.
         * @return default FetchConfiguration
         * */
        fun getDefaultFetchConfiguration(): FetchConfiguration {
            return Fetch.getDefaultFetchConfiguration()
        }

        /**
         * @return Get default Fetch instance
         * */
        fun getDefaultInstance(): Fetch {
            return Fetch.getDefaultInstance()
        }

        /**
         * @return Get default RxFetch instance
         * */
        fun getDefaultRxInstance(): RxFetch {
            val config = Fetch.getDefaultFetchConfiguration()
            return RxFetchImpl.newInstance(FetchModulesBuilder.buildModulesFromPrefs(config))
        }

        /**
         * Creates a custom Instance of Fetch with the given configuration and namespace.
         * @param fetchConfiguration custom Fetch Configuration
         * @return custom Fetch instance
         * */
        fun getInstance(fetchConfiguration: FetchConfiguration): Fetch {
            return Fetch.getInstance(fetchConfiguration)
        }

        /**
         * Creates a custom Instance of Fetch with the given configuration and namespace.
         * @param fetchConfiguration custom Fetch Configuration
         * @return custom RxFetch instance
         * */
        fun getRxInstance(fetchConfiguration: FetchConfiguration): RxFetch {
            return if (fetchConfiguration.namespace == DEFAULT_INSTANCE_NAMESPACE) {
                setDefaultInstanceConfiguration(fetchConfiguration)
                getDefaultRxInstance()
            } else {
                return RxFetchImpl.newInstance(FetchModulesBuilder.buildModulesFromPrefs(fetchConfiguration))
            }
        }

    }

}