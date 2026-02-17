package me.arianb.usb_hid_client.report_senders

import android.view.KeyEvent
import me.arianb.usb_hid_client.hid_utils.KeyboardDevicePath

class KeySender(
    keyboardDevicePath: KeyboardDevicePath
) : ReportSender(
    keyboardDevicePath
) {

    fun addStandardKey(modifier: Byte, key: Byte, action: Int) {
        val report = byteArrayOf(STANDARD_KEY, modifier, 0, key, 0)
        if (action == KeyEvent.ACTION_DOWN) {
            super.addReportToChannel(report)
        } else if (action == KeyEvent.ACTION_UP) {
            val releaseReport = ByteArray(report.size)
            releaseReport[0] = report[0]
            super.addReportToChannel(releaseReport)
        }
    }

    fun addMediaKey(key: Byte) {
        super.addReportToChannel(byteArrayOf(MEDIA_KEY, key, 0))
    }

    override fun sendReport(report: ByteArray) {
        writeBytes(report)
    }

    companion object {
        // Report IDs
        private const val STANDARD_KEY: Byte = 0x01
        private const val MEDIA_KEY: Byte = 0x02
    }
}
