package com.like.kotlinkit.udp

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import com.like.kotlinkit.DataTypeTransform
import com.like.kotlinkit.Rx
import com.like.kotlinkit.extension.hexToInt
import timber.log.Timber
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

@SuppressLint("BinaryOperationInTimber")
open class UdpDiscovery(
    mContext: Context,
    private val mMarkPort: Int,
    private val mMarkHead: String,
    private val mTargetDevice: ArrayList<Byte>,
    private val mFindListener: ((bean: UdpDevice) -> Unit)? = null,
) {
    companion object {
        val mTransform = DataTypeTransform()
    }

    class Builder(val context: Context) {
        private var markPort: Int = 6000
        private var markHead: String = "HMAUDIO"
        private var targetDevice = arrayListOf(0x72.toByte())
        private var findListener: ((bean: UdpDevice) -> Unit)? = null
        fun setMarkPort(markPort: Int): Builder {
            this.markPort = markPort
            return this
        }

        fun setMarkHead(head: String): Builder {
            this.markHead = head
            return this
        }

        fun setFindListener(listener: ((bean: UdpDevice) -> Unit)?): Builder {
            findListener = listener
            return this
        }

        fun setTargetDevice(targetDevice: ArrayList<Byte>): Builder {
            this.targetDevice = targetDevice
            return this
        }

        fun build() = UdpDiscovery(context, markPort, markHead, targetDevice, findListener)
    }


    var mIsSearch = false //是否停止发现设备
    private var mMulticastLock: WifiManager.MulticastLock
    private var mMulticastSocket: MulticastSocket = MulticastSocket()  //多播广播socket
    private var mInetAddress: InetAddress? = null


    init {
        //获取组锁，使用后记得及时释放，否则会增加耗电。为了省电，Android设备默认关闭
        val wm = mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        mMulticastLock = wm.createMulticastLock("multicastLock")
        val ip = wm.connectionInfo.ipAddress
        //这里就是将int类型的IP地址通过工具转化成String类型的，便于阅读
//        val ips = Formatter.formatIpAddress(ip)
        /*这一步就是将本机的IP地址转换成xxx.xxx.xxx.255*/
        val broadCastIP = ip or -0x1000000
        val strIp = Formatter.formatIpAddress(broadCastIP)
        mInetAddress = InetAddress.getByName(strIp)//("255.255.255.255");
    }

    ///搜索设备
    //使用前需要停止上次扫描
    fun search() {
        Rx.doIO<Boolean>(action = {
            try {
                mMulticastSocket = MulticastSocket()
                mMulticastLock.setReferenceCounted(true)
                mMulticastLock.acquire()//使用后，需要及时关闭
                Timber.i("UpnP  广播 开始查找 id：$mInetAddress 端口：$mMarkPort")
                val packet = DatagramPacket(mMarkHead.toByteArray(), mMarkHead.length, mInetAddress, mMarkPort)
                mMulticastSocket.send(packet)
                mIsSearch = true
                while (mIsSearch) {
                    this.receive()
                }
                mMulticastLock.release()
            } catch (e: IOException) {
                e.printStackTrace()
                Timber.e("搜索 UPnP协议设备 搜索异常$e")
            }
        }, observeOnMain = false).subscribe()
    }

    /// 停止发现设备
    fun stopSearch() {
        mIsSearch = false
        Timber.i("udp 停止发现设备")
    }

    /**
     * 解析数据
     * 0-6 头部-"HMAUDIO"
     * 7-12
     * 13-16 IP地址
     * 25-26 端口号
     * 43 设备类型
     */
    private fun receive(): ByteArray {
        val buf = ByteArray(1024)
        val newPacket = DatagramPacket(buf, buf.size)
        mMulticastSocket.receive(newPacket)
        val subByte = subBytes(newPacket.data, 0, newPacket.length)
        var offSet = 0
        val byteHead = ByteArray(7)
        System.arraycopy(subByte, offSet, byteHead, 0, byteHead.size)
        offSet += byteHead.size
        val byteMac = ByteArray(6)
        System.arraycopy(subByte, offSet, byteMac, 0, byteMac.size)
        offSet += byteMac.size

        val ipStrBuffer = StringBuffer()
        ipStrBuffer.append(subByte[13].hexToInt().toString() + ".")
        ipStrBuffer.append(subByte[14].hexToInt().toString() + ".")
        ipStrBuffer.append(subByte[15].hexToInt().toString() + ".")
        ipStrBuffer.append(subByte[16].hexToInt().toString())

        offSet = 25
        val portByte = ByteArray(2)
        System.arraycopy(subByte, offSet, portByte, 0, portByte.size)

        offSet = 27
        val typeByte = ByteArray(16)
        System.arraycopy(subByte, offSet, typeByte, 0, typeByte.size)
        offSet += typeByte.size

        if (mTransform.ByteArraytoString(byteHead, byteHead.size) == mMarkHead) {
            val deviceType = mTransform.ByteArraytoString(typeByte, typeByte.size)
            if (checkTargetDevice(subByte[43])) {
                mFindListener?.let {
                    it(UdpDevice().apply {
                        mIp = newPacket.address.toString().removePrefix("/")
                        mDeviceType = deviceType
                        mPort = mTransform.ByteArrayToShort(portByte).toInt()
                        Timber.i("UpnP 找到设备 $mDeviceType IP地址:$mIp  端口： $mPort  ")
                    })
                }
            }
        }
        return subByte
    }

    private fun checkTargetDevice(tag: Byte): Boolean {
        for (target in mTargetDevice) {
            if (tag == target) return true
        }
        return false
    }

    private fun subBytes(src: ByteArray, begin: Int, count: Int): ByteArray {
        val bs = ByteArray(count)
        for (i in begin until begin + count) bs[i - begin] = src[i]
        return bs
    }
}

class UdpDevice {
    var mIp: String = ""
    var mDeviceType: String = ""
    var mPort: Int = 0
}


