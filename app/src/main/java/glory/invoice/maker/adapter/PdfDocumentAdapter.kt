package glory.invoice.maker.adapter

import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import com.itextpdf.io.source.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.OutputStream


class PdfDocumentAdapter(private var outputStream: ByteArrayOutputStream) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        printAttributes1: PrintAttributes,
        cancellationSignal: CancellationSignal,
        layoutResultCallback: LayoutResultCallback,
        extras: Bundle?
    ) {
        if (cancellationSignal.isCanceled) layoutResultCallback.onLayoutCancelled() else {
            val builder = PrintDocumentInfo.Builder("file name")
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build()
            layoutResultCallback.onLayoutFinished(
                builder.build(),
                printAttributes1 != printAttributes1
            )
        }
    }

    override fun onWrite(
        pages: Array<PageRange?>?,
        parcelFileDescriptor: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        writeResultCallback: WriteResultCallback
    ) {
        var out: OutputStream? = null
        try {
            out = FileOutputStream(parcelFileDescriptor.fileDescriptor)
            outputStream.writeTo(out)
            if (cancellationSignal.isCanceled) writeResultCallback.onWriteCancelled() else {
                writeResultCallback.onWriteFinished(arrayOf<PageRange>(PageRange.ALL_PAGES))
            }
        } catch (e: Exception) {
            writeResultCallback.onWriteFailed(e.message)
            e.printStackTrace()
        } finally {
            out?.close()
        }
    }
}