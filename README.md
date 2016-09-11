<h1>Codec (coder/decoder) for Digitical Selective Call</h1>

<h2>Description</h2>
This codec implements R-REC-M.493-14-201509.

For any questions please contact me on flyenj@gmail.com
<h2>Quick start</h2>

<h3>Transmission</h3>

```java
Distress distress = new Distress(
  new Mmsi(123456789),
  COLLISION,
  new Coordinates("12345", "4321"),
  TimeUTC.now(),
  F1B_J2B_TTY_FEC
);

RTPSender sender = new RTPSender();
byte[] data = distress.encode();
sender.send(data);
```

<h3>Receive</h3>
```java
public class TestReceive {

  public static void main(String[] args) throws SocketException,
    UnknownHostException {

      RtpReceiver receiver = new RtpReceiver();

      receiver.start();
  }

  static class RtpReceiver implements RTPAppIntf {

    private final String address = "192.168.4.79";

    private final int port = 7100;

    private final DscDecoder dscDecoder = new DscDecoder();

    public void start() throws SocketException, UnknownHostException {
      RTPSession rtpSession = new RTPSession(
        new DatagramSocket(port, InetAddress.getByName(address)),
        new DatagramSocket(port + 1, InetAddress.getByName(address))
    );

      rtpSession.naivePktReception(true);
      rtpSession.RTPSessionRegister(this, null, null);
    }

    @Override
    public void receiveData(DataFrame dataFrame, Participant participant) {
      byte[] data = dataFrame.getConcatenatedData();

      for (int i = 0; i < data.length; i += 2) {
        dscDecoder.onBit(data[i] == 0 ? (byte) 1 : (byte) 0);
      }
    }
  }
}
```
